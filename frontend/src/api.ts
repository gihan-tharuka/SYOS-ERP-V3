import type {
  Bill,
  MainStockBatch,
  Product,
  ProductRequest,
  ReorderAlert,
  ReshelveResponse,
  Sale,
  ShelfStock,
  StockSummary,
} from './types';

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080').replace(/\/$/, '');

type RequestOptions = {
  method?: string;
  body?: unknown;
};

async function request<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: options.method ?? 'GET',
    headers: options.body ? { 'Content-Type': 'application/json' } : undefined,
    body: options.body ? JSON.stringify(options.body) : undefined,
  });

  if (!response.ok) {
    let message = `Request failed with status ${response.status}`;
    try {
      const error = (await response.json()) as { message?: string; validationErrors?: Record<string, string> };
      const details = error.validationErrors ? Object.values(error.validationErrors).join(', ') : '';
      message = [error.message, details].filter(Boolean).join(': ') || message;
    } catch {
      // Keep the generic HTTP error.
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export const api = {
  baseUrl: API_BASE_URL,
  products: {
    list: () => request<Product[]>('/api/products'),
    create: (body: ProductRequest) => request<Product>('/api/products', { method: 'POST', body }),
    update: (id: number, body: ProductRequest) => request<Product>(`/api/products/${id}`, { method: 'PUT', body }),
    delete: (id: number) => request<void>(`/api/products/${id}`, { method: 'DELETE' }),
  },
  inventory: {
    mainStock: () => request<MainStockBatch[]>('/api/inventory/main-stock'),
    shelfStock: () => request<ShelfStock[]>('/api/inventory/shelf-stock'),
    reshelve: (body: { mainStockBatchId: number; shelfStockId: number; quantity: number }) =>
      request<ReshelveResponse>('/api/inventory/shelf-stock/reshelve', { method: 'POST', body }),
  },
  reports: {
    stock: () => request<StockSummary[]>('/api/reports/stock'),
    reorder: () => request<ReorderAlert[]>('/api/reports/reorder'),
  },
  sales: {
    list: () => request<Sale[]>('/api/sales'),
    create: (body: { cashierName: string; items: { productId: number; quantity: number }[] }) =>
      request<Sale>('/api/sales', { method: 'POST', body }),
  },
  bills: {
    find: (serialNumber: number) => request<Bill>(`/api/bills/${serialNumber}`),
  },
};
