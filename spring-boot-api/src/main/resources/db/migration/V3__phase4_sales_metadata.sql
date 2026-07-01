ALTER TABLE sales
    ADD COLUMN cashier_name VARCHAR(255) NOT NULL DEFAULT 'Legacy Cashier',
    ADD COLUMN sale_timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

UPDATE sales
SET sale_timestamp = TIMESTAMP(sale_date, '00:00:00')
WHERE sale_timestamp IS NOT NULL;

CREATE INDEX idx_sales_sale_timestamp ON sales (sale_timestamp);
