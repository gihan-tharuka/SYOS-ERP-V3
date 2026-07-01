import { useEffect, useMemo, useState } from 'react';
import type { ReactNode } from 'react';
import {
  AlertTriangle,
  BarChart3,
  Boxes,
  ClipboardList,
  LayoutDashboard,
  Package,
  Plus,
  ReceiptText,
  RefreshCcw,
  Save,
  Search,
  ShoppingCart,
  Trash2,
  type LucideIcon,
} from 'lucide-react';
import { api } from './api';
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

type View = 'dashboard' | 'products' | 'inventory' | 'reports' | 'pos' | 'sales';

const navItems: { view: View; label: string; icon: LucideIcon }[] = [
  { view: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { view: 'products', label: 'Products', icon: Package },
  { view: 'inventory', label: 'Inventory', icon: Boxes },
  { view: 'reports', label: 'Reports', icon: BarChart3 },
  { view: 'pos', label: 'POS', icon: ShoppingCart },
  { view: 'sales', label: 'Sales/Bills', icon: ReceiptText },
];

const currency = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
});

function App() {
  const [view, setView] = useState<View>('dashboard');

  return (
    <div className="min-h-screen bg-[#f6f7f4] text-[#17211f]">
      <aside className="fixed inset-y-0 left-0 hidden w-64 border-r border-[#d9ded4] bg-white lg:block">
        <div className="px-6 py-6">
          <div className="text-lg font-semibold">SYOS ERP</div>
          <div className="mt-1 text-sm text-[#607066]">Spring Boot portfolio API</div>
        </div>
        <nav className="space-y-1 px-3">
          {navItems.map((item) => (
            <button
              key={item.view}
              onClick={() => setView(item.view)}
              className={`flex w-full items-center gap-3 rounded-md px-3 py-2 text-left text-sm transition ${
                view === item.view
                  ? 'bg-[#e2f0e6] text-[#123524]'
                  : 'text-[#45554b] hover:bg-[#f0f3ed]'
              }`}
            >
              <item.icon className="h-4 w-4" />
              {item.label}
            </button>
          ))}
        </nav>
      </aside>

      <div className="lg:pl-64">
        <header className="sticky top-0 z-10 border-b border-[#d9ded4] bg-white/90 px-4 py-3 backdrop-blur lg:px-8">
          <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
            <div>
              <h1 className="text-xl font-semibold">{navItems.find((item) => item.view === view)?.label}</h1>
              <p className="text-sm text-[#607066]">API base: {api.baseUrl}</p>
            </div>
            <div className="flex gap-2 overflow-x-auto lg:hidden">
              {navItems.map((item) => (
                <button
                  key={item.view}
                  onClick={() => setView(item.view)}
                  className={`shrink-0 rounded-md px-3 py-2 text-sm ${
                    view === item.view ? 'bg-[#123524] text-white' : 'bg-[#eef1eb] text-[#45554b]'
                  }`}
                >
                  {item.label}
                </button>
              ))}
            </div>
          </div>
        </header>

        <main className="px-4 py-6 lg:px-8">
          {view === 'dashboard' && <Dashboard onNavigate={setView} />}
          {view === 'products' && <Products />}
          {view === 'inventory' && <Inventory />}
          {view === 'reports' && <Reports />}
          {view === 'pos' && <POS />}
          {view === 'sales' && <SalesBills />}
        </main>
      </div>
    </div>
  );
}

function Dashboard({ onNavigate }: { onNavigate: (view: View) => void }) {
  const { data, loading, error, reload } = useDashboardData();
  const recentSales = data.sales.slice(0, 5);

  return (
    <section className="space-y-6">
      <Toolbar title="Operations Snapshot" onRefresh={reload} />
      <StateBlock loading={loading} error={error} empty={false}>
        <div className="grid gap-4 md:grid-cols-3">
          <MetricCard label="Products" value={data.products.length} icon={Package} />
          <MetricCard label="Low-stock alerts" value={data.alerts.length} icon={AlertTriangle} tone="warn" />
          <MetricCard label="Recent sales" value={recentSales.length} icon={ReceiptText} />
        </div>

        <div className="grid gap-4 lg:grid-cols-[1.4fr_1fr]">
          <Panel title="Recent Sales">
            <DataTable
              emptyText="No sales yet."
              headers={['Sale', 'Cashier', 'Time', 'Total']}
              rows={recentSales.map((sale) => [
                `#${sale.saleId}`,
                sale.cashierName,
                formatDateTime(sale.saleDateTime),
                currency.format(sale.totalAmount),
              ])}
            />
          </Panel>

          <Panel title="Quick Links">
            <div className="grid gap-2">
              {[
                ['Manage inventory', 'inventory'],
                ['Run POS sale', 'pos'],
                ['View reports', 'reports'],
                ['Review bills', 'sales'],
              ].map(([label, target]) => (
                <button
                  key={target}
                  onClick={() => onNavigate(target as View)}
                  className="rounded-md border border-[#d9ded4] bg-white px-4 py-3 text-left text-sm font-medium hover:bg-[#f5f7f2]"
                >
                  {label}
                </button>
              ))}
            </div>
          </Panel>
        </div>
      </StateBlock>
    </section>
  );
}

function Products() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editing, setEditing] = useState<Product | null>(null);
  const [form, setForm] = useState<ProductRequest>(emptyProduct());

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      setProducts(await api.products.list());
    } catch (err) {
      setError(errorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, []);

  const saveProduct = async () => {
    setError('');
    if (!form.productCode.trim() || !form.productName.trim() || Number(form.price) <= 0) {
      setError('Product code, name, and a positive price are required.');
      return;
    }
    try {
      if (editing) {
        await api.products.update(editing.id, coerceProduct(form));
      } else {
        await api.products.create(coerceProduct(form));
      }
      setEditing(null);
      setForm(emptyProduct());
      await load();
    } catch (err) {
      setError(errorMessage(err));
    }
  };

  const editProduct = (product: Product) => {
    setEditing(product);
    setForm({
      productCode: product.productCode,
      productName: product.productName,
      price: product.price,
      discount: product.discount,
      imagePath: product.imagePath ?? null,
    });
  };

  const deleteProduct = async (id: number) => {
    setError('');
    try {
      await api.products.delete(id);
      await load();
    } catch (err) {
      setError(errorMessage(err));
    }
  };

  return (
    <section className="grid gap-6 xl:grid-cols-[360px_1fr]">
      <Panel title={editing ? 'Edit Product' : 'Create Product'}>
        <div className="space-y-3">
          <TextInput label="Code" value={form.productCode} onChange={(productCode) => setForm({ ...form, productCode })} />
          <TextInput label="Name" value={form.productName} onChange={(productName) => setForm({ ...form, productName })} />
          <NumberInput label="Price" value={form.price} onChange={(price) => setForm({ ...form, price })} min={0.01} />
          <NumberInput label="Discount" value={form.discount} onChange={(discount) => setForm({ ...form, discount })} min={0} />
          <div className="flex gap-2">
            <Button onClick={saveProduct} icon={Save}>{editing ? 'Save Changes' : 'Create Product'}</Button>
            {editing && <Button variant="secondary" onClick={() => { setEditing(null); setForm(emptyProduct()); }}>Cancel</Button>}
          </div>
          {error && <ErrorText message={error} />}
        </div>
      </Panel>

      <Panel title="Products">
        <StateBlock loading={loading} error={error && !products.length ? error : ''} empty={!products.length}>
          <div className="overflow-x-auto">
            <table className="min-w-full text-sm">
              <thead>
                <tr className="border-b border-[#d9ded4] text-left text-[#607066]">
                  <th className="py-2 pr-4">Code</th>
                  <th className="py-2 pr-4">Name</th>
                  <th className="py-2 pr-4">Price</th>
                  <th className="py-2 pr-4">Discount</th>
                  <th className="py-2 text-right">Actions</th>
                </tr>
              </thead>
              <tbody>
                {products.map((product) => (
                  <tr key={product.id} className="border-b border-[#eef1eb]">
                    <td className="py-3 pr-4 font-medium">{product.productCode}</td>
                    <td className="py-3 pr-4">{product.productName}</td>
                    <td className="py-3 pr-4 tabular">{currency.format(product.price)}</td>
                    <td className="py-3 pr-4 tabular">{currency.format(product.discount)}</td>
                    <td className="py-3 text-right">
                      <div className="flex justify-end gap-2">
                        <Button variant="secondary" onClick={() => editProduct(product)}>Edit</Button>
                        <IconButton label="Delete" onClick={() => void deleteProduct(product.id)} icon={Trash2} />
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </StateBlock>
      </Panel>
    </section>
  );
}

function Inventory() {
  const [mainStock, setMainStock] = useState<MainStockBatch[]>([]);
  const [shelfStock, setShelfStock] = useState<ShelfStock[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [result, setResult] = useState<ReshelveResponse | null>(null);
  const [form, setForm] = useState({ mainStockBatchId: '', shelfStockId: '', quantity: 1 });

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const [main, shelf] = await Promise.all([api.inventory.mainStock(), api.inventory.shelfStock()]);
      setMainStock(main);
      setShelfStock(shelf);
    } catch (err) {
      setError(errorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, []);

  const submit = async () => {
    setError('');
    setResult(null);
    try {
      const response = await api.inventory.reshelve({
        mainStockBatchId: Number(form.mainStockBatchId),
        shelfStockId: Number(form.shelfStockId),
        quantity: Number(form.quantity),
      });
      setResult(response);
      await load();
    } catch (err) {
      setError(errorMessage(err));
    }
  };

  return (
    <section className="space-y-6">
      <Toolbar title="Inventory Movement" onRefresh={load} />
      <div className="grid gap-6 xl:grid-cols-[360px_1fr]">
        <Panel title="Reshelve Stock">
          <div className="space-y-3">
            <SelectInput
              label="Main stock batch"
              value={form.mainStockBatchId}
              onChange={(mainStockBatchId) => setForm({ ...form, mainStockBatchId })}
              options={mainStock.map((stock) => ({
                value: String(stock.id),
                label: `${stock.productCode} / ${stock.batchCode} (${stock.quantity})`,
              }))}
            />
            <SelectInput
              label="Shelf stock"
              value={form.shelfStockId}
              onChange={(shelfStockId) => setForm({ ...form, shelfStockId })}
              options={shelfStock.map((stock) => ({
                value: String(stock.id),
                label: `${stock.productCode} (${stock.currentQuantity}/${stock.shelfCapacity})`,
              }))}
            />
            <NumberInput label="Quantity" value={form.quantity} onChange={(quantity) => setForm({ ...form, quantity })} min={1} />
            <Button onClick={submit} icon={RefreshCcw}>Move to Shelf</Button>
            {error && <ErrorText message={error} />}
            {result && (
              <div className="rounded-md bg-[#e2f0e6] p-3 text-sm">
                Moved {result.movedQuantity} units of {result.productCode}. Main stock now {result.updatedMainStockQuantity}; shelf now {result.updatedShelfStockQuantity}.
              </div>
            )}
          </div>
        </Panel>

        <div className="grid gap-6">
          <Panel title="Main Stock">
            <StateBlock loading={loading} error={error && !mainStock.length ? error : ''} empty={!mainStock.length}>
              <DataTable
                emptyText="No main stock found."
                headers={['Product', 'Batch', 'Supplier', 'Qty', 'Expiry']}
                rows={mainStock.map((stock) => [
                  `${stock.productCode} - ${stock.productName}`,
                  stock.batchCode,
                  stock.supplierName,
                  String(stock.quantity),
                  stock.expiryDate ?? '-',
                ])}
              />
            </StateBlock>
          </Panel>
          <Panel title="Shelf Stock">
            <StateBlock loading={loading} error="" empty={!shelfStock.length}>
              <DataTable
                emptyText="No shelf stock found."
                headers={['Product', 'Current', 'Capacity', 'Remaining']}
                rows={shelfStock.map((stock) => [
                  `${stock.productCode} - ${stock.productName}`,
                  String(stock.currentQuantity),
                  String(stock.shelfCapacity),
                  String(stock.shelfCapacity - stock.currentQuantity),
                ])}
              />
            </StateBlock>
          </Panel>
        </div>
      </div>
    </section>
  );
}

function Reports() {
  const { stock, alerts, loading, error, reload } = useReportData();

  return (
    <section className="space-y-6">
      <Toolbar title="Reports" onRefresh={reload} />
      <StateBlock loading={loading} error={error} empty={false}>
        <Panel title="Reorder Alerts">
          <DataTable
            emptyText="No low-stock alerts."
            headers={['Product', 'Threshold', 'Current', 'Status']}
            rows={alerts.map((alert) => [
              `${alert.productCode} - ${alert.productName}`,
              String(alert.thresholdQuantity),
              String(alert.currentTotalStock),
              alert.status,
            ])}
          />
        </Panel>
        <Panel title="Stock Summary">
          <DataTable
            emptyText="No stock summary available."
            headers={['Product', 'Main', 'Shelf', 'Available', 'Threshold', 'Status']}
            rows={stock.map((summary) => [
              `${summary.productCode} - ${summary.productName}`,
              String(summary.totalMainStockQuantity),
              String(summary.totalShelfStockQuantity),
              String(summary.totalAvailableQuantity),
              summary.reorderThreshold === null ? '-' : String(summary.reorderThreshold),
              summary.status,
            ])}
          />
        </Panel>
      </StateBlock>
    </section>
  );
}

function POS() {
  const [products, setProducts] = useState<Product[]>([]);
  const [cashierName, setCashierName] = useState('Demo Cashier');
  const [lines, setLines] = useState([{ productId: '', quantity: 1 }]);
  const [bill, setBill] = useState<Sale | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    api.products.list()
      .then(setProducts)
      .catch((err) => setError(errorMessage(err)))
      .finally(() => setLoading(false));
  }, []);

  const addLine = () => setLines([...lines, { productId: '', quantity: 1 }]);
  const submitSale = async () => {
    setError('');
    setBill(null);
    const items = lines
      .filter((line) => line.productId)
      .map((line) => ({ productId: Number(line.productId), quantity: Number(line.quantity) }));
    if (!cashierName.trim() || !items.length) {
      setError('Cashier name and at least one product line are required.');
      return;
    }
    try {
      setBill(await api.sales.create({ cashierName, items }));
      setLines([{ productId: '', quantity: 1 }]);
    } catch (err) {
      setError(errorMessage(err));
    }
  };

  return (
    <section className="grid gap-6 xl:grid-cols-[420px_1fr]">
      <Panel title="Create POS Sale">
        <StateBlock loading={loading} error="" empty={false}>
          <div className="space-y-3">
            <TextInput label="Cashier" value={cashierName} onChange={setCashierName} />
            {lines.map((line, index) => (
              <div key={index} className="grid grid-cols-[1fr_96px] gap-2">
                <SelectInput
                  label={index === 0 ? 'Product' : ''}
                  value={line.productId}
                  onChange={(productId) => setLines(lines.map((entry, i) => i === index ? { ...entry, productId } : entry))}
                  options={products.map((product) => ({ value: String(product.id), label: `${product.productCode} - ${product.productName}` }))}
                />
                <NumberInput
                  label={index === 0 ? 'Qty' : ''}
                  value={line.quantity}
                  onChange={(quantity) => setLines(lines.map((entry, i) => i === index ? { ...entry, quantity } : entry))}
                  min={1}
                />
              </div>
            ))}
            <div className="flex gap-2">
              <Button variant="secondary" onClick={addLine} icon={Plus}>Add Line</Button>
              <Button onClick={submitSale} icon={ShoppingCart}>Submit Sale</Button>
            </div>
            {error && <ErrorText message={error} />}
          </div>
        </StateBlock>
      </Panel>

      <Panel title="Bill Details">
        {bill ? <BillDetail bill={bill} /> : <EmptyState text="Submit a sale to see the generated bill." />}
      </Panel>
    </section>
  );
}

function SalesBills() {
  const [sales, setSales] = useState<Sale[]>([]);
  const [billId, setBillId] = useState('');
  const [bill, setBill] = useState<Bill | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      setSales(await api.sales.list());
    } catch (err) {
      setError(errorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, []);

  const lookupBill = async () => {
    setError('');
    setBill(null);
    try {
      setBill(await api.bills.find(Number(billId)));
    } catch (err) {
      setError(errorMessage(err));
    }
  };

  return (
    <section className="space-y-6">
      <Toolbar title="Sales and Bills" onRefresh={load} />
      <Panel title="Bill Lookup">
        <div className="flex max-w-md gap-2">
          <input
            value={billId}
            onChange={(event) => setBillId(event.target.value)}
            className="min-h-10 flex-1 rounded-md border border-[#cfd7cc] px-3 text-sm outline-none focus:border-[#3d6f53]"
            placeholder="Bill serial number"
          />
          <Button onClick={lookupBill} icon={Search}>Find</Button>
        </div>
        {error && <div className="mt-3"><ErrorText message={error} /></div>}
        {bill && <div className="mt-4"><BillDetail bill={bill} /></div>}
      </Panel>
      <Panel title="Sales">
        <StateBlock loading={loading} error={error && !sales.length ? error : ''} empty={!sales.length}>
          <DataTable
            emptyText="No sales found."
            headers={['Sale', 'Bill', 'Cashier', 'Time', 'Total']}
            rows={sales.map((sale) => [
              `#${sale.saleId}`,
              `#${sale.billSerialNumber}`,
              sale.cashierName,
              formatDateTime(sale.saleDateTime),
              currency.format(sale.totalAmount),
            ])}
          />
        </StateBlock>
      </Panel>
    </section>
  );
}

function useDashboardData() {
  const [products, setProducts] = useState<Product[]>([]);
  const [alerts, setAlerts] = useState<ReorderAlert[]>([]);
  const [sales, setSales] = useState<Sale[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const reload = async () => {
    setLoading(true);
    setError('');
    try {
      const [productsData, alertsData, salesData] = await Promise.all([
        api.products.list(),
        api.reports.reorder(),
        api.sales.list(),
      ]);
      setProducts(productsData);
      setAlerts(alertsData);
      setSales(salesData);
    } catch (err) {
      setError(errorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void reload();
  }, []);

  return { data: { products, alerts, sales }, loading, error, reload };
}

function useReportData() {
  const [stock, setStock] = useState<StockSummary[]>([]);
  const [alerts, setAlerts] = useState<ReorderAlert[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const reload = async () => {
    setLoading(true);
    setError('');
    try {
      const [stockData, alertData] = await Promise.all([api.reports.stock(), api.reports.reorder()]);
      setStock(stockData);
      setAlerts(alertData);
    } catch (err) {
      setError(errorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void reload();
  }, []);

  return { stock, alerts, loading, error, reload };
}

function BillDetail({ bill }: { bill: Bill }) {
  const totalQuantity = useMemo(() => bill.items.reduce((sum, item) => sum + item.quantity, 0), [bill.items]);
  return (
    <div className="space-y-4">
      <div className="grid gap-3 md:grid-cols-4">
        <MetricCard label="Bill" value={`#${bill.billSerialNumber}`} icon={ReceiptText} />
        <MetricCard label="Sale" value={`#${bill.saleId}`} icon={ClipboardList} />
        <MetricCard label="Items" value={totalQuantity} icon={Package} />
        <MetricCard label="Total" value={currency.format(bill.totalAmount)} icon={ShoppingCart} />
      </div>
      <DataTable
        emptyText="No bill items."
        headers={['Product', 'Qty', 'Unit', 'Line total']}
        rows={bill.items.map((item) => [
          `${item.productCode} - ${item.productName}`,
          String(item.quantity),
          currency.format(item.unitPrice),
          currency.format(item.lineTotal),
        ])}
      />
    </div>
  );
}

function Panel({ title, children }: { title: string; children: ReactNode }) {
  return (
    <section className="rounded-md border border-[#d9ded4] bg-white p-4 shadow-sm">
      <h2 className="mb-4 text-base font-semibold">{title}</h2>
      {children}
    </section>
  );
}

function Toolbar({ title, onRefresh }: { title: string; onRefresh: () => void }) {
  return (
    <div className="flex items-center justify-between">
      <h2 className="text-lg font-semibold">{title}</h2>
      <IconButton label="Refresh" icon={RefreshCcw} onClick={() => void onRefresh()} />
    </div>
  );
}

function MetricCard({ label, value, icon: Icon, tone = 'default' }: { label: string; value: string | number; icon: LucideIcon; tone?: 'default' | 'warn' }) {
  return (
    <div className="rounded-md border border-[#d9ded4] bg-white p-4 shadow-sm">
      <div className="flex items-center justify-between">
        <div>
          <div className="text-sm text-[#607066]">{label}</div>
          <div className="mt-1 text-2xl font-semibold tabular">{value}</div>
        </div>
        <div className={`rounded-md p-2 ${tone === 'warn' ? 'bg-[#fff3d9] text-[#8a5b00]' : 'bg-[#e2f0e6] text-[#123524]'}`}>
          <Icon className="h-5 w-5" />
        </div>
      </div>
    </div>
  );
}

function DataTable({ headers, rows, emptyText }: { headers: string[]; rows: string[][]; emptyText: string }) {
  if (!rows.length) {
    return <EmptyState text={emptyText} />;
  }
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full text-sm">
        <thead>
          <tr className="border-b border-[#d9ded4] text-left text-[#607066]">
            {headers.map((header) => <th key={header} className="py-2 pr-4 font-medium">{header}</th>)}
          </tr>
        </thead>
        <tbody>
          {rows.map((row, index) => (
            <tr key={index} className="border-b border-[#eef1eb] last:border-0">
              {row.map((cell, cellIndex) => (
                <td key={cellIndex} className="py-3 pr-4 tabular">{cell}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function StateBlock({ loading, error, empty, children }: { loading: boolean; error: string; empty: boolean; children: ReactNode }) {
  if (loading) return <div className="rounded-md bg-white p-6 text-sm text-[#607066]">Loading...</div>;
  if (error) return <ErrorText message={error} />;
  if (empty) return <EmptyState text="No records found." />;
  return <>{children}</>;
}

function EmptyState({ text }: { text: string }) {
  return <div className="rounded-md border border-dashed border-[#cfd7cc] bg-[#f8faf6] p-6 text-sm text-[#607066]">{text}</div>;
}

function TextInput({ label, value, onChange }: { label: string; value: string; onChange: (value: string) => void }) {
  return (
    <label className="block text-sm">
      {label && <span className="mb-1 block text-[#45554b]">{label}</span>}
      <input value={value} onChange={(event) => onChange(event.target.value)} className="min-h-10 w-full rounded-md border border-[#cfd7cc] px-3 outline-none focus:border-[#3d6f53]" />
    </label>
  );
}

function NumberInput({ label, value, onChange, min }: { label: string; value: number; onChange: (value: number) => void; min: number }) {
  return (
    <label className="block text-sm">
      {label && <span className="mb-1 block text-[#45554b]">{label}</span>}
      <input type="number" min={min} value={value} onChange={(event) => onChange(Number(event.target.value))} className="min-h-10 w-full rounded-md border border-[#cfd7cc] px-3 outline-none focus:border-[#3d6f53]" />
    </label>
  );
}

function SelectInput({ label, value, onChange, options }: { label: string; value: string; onChange: (value: string) => void; options: { value: string; label: string }[] }) {
  return (
    <label className="block text-sm">
      {label && <span className="mb-1 block text-[#45554b]">{label}</span>}
      <select value={value} onChange={(event) => onChange(event.target.value)} className="min-h-10 w-full rounded-md border border-[#cfd7cc] bg-white px-3 outline-none focus:border-[#3d6f53]">
        <option value="">Select...</option>
        {options.map((option) => <option key={option.value} value={option.value}>{option.label}</option>)}
      </select>
    </label>
  );
}

function Button({ children, onClick, icon: Icon, variant = 'primary' }: { children: ReactNode; onClick: () => void; icon?: LucideIcon; variant?: 'primary' | 'secondary' }) {
  return (
    <button
      onClick={onClick}
      className={`inline-flex min-h-10 items-center justify-center gap-2 rounded-md px-4 text-sm font-medium transition ${
        variant === 'primary'
          ? 'bg-[#123524] text-white hover:bg-[#1e5138]'
          : 'border border-[#cfd7cc] bg-white text-[#17211f] hover:bg-[#f5f7f2]'
      }`}
    >
      {Icon && <Icon className="h-4 w-4" />}
      {children}
    </button>
  );
}

function IconButton({ label, icon: Icon, onClick }: { label: string; icon: LucideIcon; onClick: () => void }) {
  return (
    <button
      title={label}
      aria-label={label}
      onClick={onClick}
      className="inline-flex h-10 w-10 items-center justify-center rounded-md border border-[#cfd7cc] bg-white text-[#45554b] hover:bg-[#f5f7f2]"
    >
      <Icon className="h-4 w-4" />
    </button>
  );
}

function ErrorText({ message }: { message: string }) {
  return <div className="rounded-md border border-[#f1c9c0] bg-[#fff2ef] p-3 text-sm text-[#8a2f1d]">{message}</div>;
}

function emptyProduct(): ProductRequest {
  return { productCode: '', productName: '', price: 0, discount: 0, imagePath: null };
}

function coerceProduct(product: ProductRequest): ProductRequest {
  return {
    ...product,
    price: Number(product.price),
    discount: Number(product.discount),
  };
}

function errorMessage(err: unknown) {
  return err instanceof Error ? err.message : 'Something went wrong.';
}

function formatDateTime(value: string) {
  return new Date(value).toLocaleString();
}

export default App;
