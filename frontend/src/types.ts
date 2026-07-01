export type Product = {
  id: number;
  productCode: string;
  productName: string;
  price: number;
  discount: number;
  imagePath?: string | null;
};

export type ProductRequest = Omit<Product, 'id'>;

export type MainStockBatch = {
  id: number;
  productId: number;
  productCode: string;
  productName: string;
  supplierId: number;
  supplierName: string;
  batchCode: string;
  purchaseDate: string;
  purchasePrice: number;
  quantity: number;
  expiryDate?: string | null;
};

export type ShelfStock = {
  id: number;
  productId: number;
  productCode: string;
  productName: string;
  shelfCapacity: number;
  currentQuantity: number;
};

export type ReshelveResponse = {
  productId: number;
  productCode: string;
  productName: string;
  mainStockBatchId: number;
  updatedMainStockQuantity: number;
  shelfStockId: number;
  updatedShelfStockQuantity: number;
  movedQuantity: number;
};

export type StockSummary = {
  productId: number;
  productCode: string;
  productName: string;
  totalMainStockQuantity: number;
  totalShelfStockQuantity: number;
  totalAvailableQuantity: number;
  reorderThreshold: number | null;
  status: 'LOW_STOCK' | 'OK';
};

export type ReorderAlert = {
  productId: number;
  productCode: string;
  productName: string;
  thresholdQuantity: number;
  currentTotalStock: number;
  status: 'LOW_STOCK' | 'OK';
};

export type BillItem = {
  id: number;
  productId: number;
  productCode: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  lineTotal: number;
};

export type Sale = {
  saleId: number;
  cashierName: string;
  saleDateTime: string;
  billSerialNumber: number;
  totalAmount: number;
  items: BillItem[];
};

export type Bill = Sale;
