INSERT INTO suppliers (supplier_id, username, company_name, contact_person, email, mobile) VALUES
    (1, 'supplier1', 'ABC Supply Co.', 'John Doe', 'john.doe@abc.com', '1234567890'),
    (2, 'supplier2', 'XYZ Enterprises', 'Jane Smith', 'jane.smith@xyz.com', '0987654321');

INSERT INTO products (product_id, product_code, product_name, price, discount, image_path) VALUES
    (1, 'ITEM001', 'Laptop', 1500.00, 10.00, NULL),
    (2, 'ITEM002', 'Smartphone', 800.00, 5.00, NULL),
    (3, 'SOAP001', 'Lux', 80.00, 0.00, NULL),
    (4, 'WATER001', 'Water', 100.00, 0.00, NULL),
    (5, 'BAG001', 'School Bag', 3500.00, 250.00, NULL),
    (12, 'RICE001', 'White rice', 120.00, 10.00, NULL);

INSERT INTO main_stock_batches (
    stock_batch_id,
    product_id,
    supplier_id,
    batch_code,
    purchase_date,
    purchase_price,
    quantity,
    expiry_date
) VALUES
    (1, 1, 1, 'BATCH001', '2024-12-12', 50.00, 50, '2025-02-02'),
    (2, 5, 2, 'BATCH002', '2024-12-24', 500.00, 70, '2025-12-12'),
    (3, 5, 2, 'BATCH003', '2025-01-01', 600.00, 100, '2025-01-30'),
    (6, 4, 1, 'WATERB001', '2025-01-01', 300.00, 50, '2025-06-01'),
    (9, 12, 1, 'BATCH0111', '2025-01-01', 200.00, 60, '2025-03-01');

INSERT INTO shelf_stock (shelf_stock_id, product_id, shelf_capacity, current_quantity) VALUES
    (1, 1, 50, 39),
    (2, 5, 40, 19),
    (3, 12, 50, 0);

INSERT INTO reorder_levels (reorder_level_id, product_id, threshold_quantity, total_stock) VALUES
    (1, 5, 50, 30),
    (2, 4, 50, 50),
    (3, 12, 50, 60);

INSERT INTO sales (sale_id, sale_date, transaction_type) VALUES
    (1, '2025-01-25', 'POS'),
    (2, '2025-01-25', 'POS'),
    (3, '2025-02-01', 'POS');

INSERT INTO bills (
    serial_number,
    sale_id,
    total_price,
    discount,
    cash_tendered,
    change_amount,
    bill_date,
    payment_method
) VALUES
    (1, 1, 35000.00, 0.00, 40000.00, 5000.00, '2025-01-25', 'CASH'),
    (2, 2, 5000.00, 0.00, 10000.00, 5000.00, '2025-01-25', 'CASH'),
    (3, 3, 2400.00, 0.00, 3000.00, 600.00, '2025-02-01', 'CASH');

INSERT INTO bill_items (
    bill_item_id,
    bill_id,
    product_id,
    product_name_snapshot,
    quantity,
    unit_price,
    line_total
) VALUES
    (1, 1, 1, 'Laptop', 7, 1500.00, 10500.00),
    (2, 1, 5, 'School Bag', 7, 3500.00, 24500.00),
    (3, 2, 1, 'Laptop', 1, 1500.00, 1500.00),
    (4, 2, 5, 'School Bag', 1, 3500.00, 3500.00),
    (5, 3, 12, 'White rice', 20, 120.00, 2400.00);

ALTER TABLE suppliers AUTO_INCREMENT = 3;
ALTER TABLE products AUTO_INCREMENT = 13;
ALTER TABLE main_stock_batches AUTO_INCREMENT = 10;
ALTER TABLE shelf_stock AUTO_INCREMENT = 4;
ALTER TABLE reorder_levels AUTO_INCREMENT = 4;
ALTER TABLE sales AUTO_INCREMENT = 4;
ALTER TABLE bills AUTO_INCREMENT = 4;
ALTER TABLE bill_items AUTO_INCREMENT = 6;
