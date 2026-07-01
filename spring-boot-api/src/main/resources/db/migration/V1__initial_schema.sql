CREATE TABLE suppliers (
    supplier_id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    mobile VARCHAR(20),
    PRIMARY KEY (supplier_id),
    CONSTRAINT uk_suppliers_username UNIQUE (username),
    CONSTRAINT uk_suppliers_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE products (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    product_code VARCHAR(100) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    discount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    image_path VARCHAR(500),
    PRIMARY KEY (product_id),
    CONSTRAINT uk_products_code UNIQUE (product_code),
    CONSTRAINT uk_products_name UNIQUE (product_name),
    CONSTRAINT chk_products_price_non_negative CHECK (price >= 0),
    CONSTRAINT chk_products_discount_non_negative CHECK (discount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE main_stock_batches (
    stock_batch_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    batch_code VARCHAR(100) NOT NULL,
    purchase_date DATE NOT NULL,
    purchase_price DECIMAL(12,2) NOT NULL,
    quantity INT NOT NULL,
    expiry_date DATE,
    PRIMARY KEY (stock_batch_id),
    CONSTRAINT uk_main_stock_product_batch UNIQUE (product_id, batch_code),
    CONSTRAINT fk_main_stock_product FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT fk_main_stock_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers (supplier_id),
    CONSTRAINT chk_main_stock_purchase_price_non_negative CHECK (purchase_price >= 0),
    CONSTRAINT chk_main_stock_quantity_non_negative CHECK (quantity >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_main_stock_product_id ON main_stock_batches (product_id);
CREATE INDEX idx_main_stock_supplier_id ON main_stock_batches (supplier_id);
CREATE INDEX idx_main_stock_expiry_date ON main_stock_batches (expiry_date);

CREATE TABLE shelf_stock (
    shelf_stock_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    shelf_capacity INT NOT NULL,
    current_quantity INT NOT NULL,
    PRIMARY KEY (shelf_stock_id),
    CONSTRAINT uk_shelf_stock_product UNIQUE (product_id),
    CONSTRAINT fk_shelf_stock_product FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT chk_shelf_stock_capacity_non_negative CHECK (shelf_capacity >= 0),
    CONSTRAINT chk_shelf_stock_current_non_negative CHECK (current_quantity >= 0),
    CONSTRAINT chk_shelf_stock_current_within_capacity CHECK (current_quantity <= shelf_capacity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE reorder_levels (
    reorder_level_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    threshold_quantity INT NOT NULL DEFAULT 50,
    total_stock INT NOT NULL DEFAULT 0,
    PRIMARY KEY (reorder_level_id),
    CONSTRAINT uk_reorder_levels_product UNIQUE (product_id),
    CONSTRAINT fk_reorder_levels_product FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT chk_reorder_threshold_non_negative CHECK (threshold_quantity >= 0),
    CONSTRAINT chk_reorder_total_non_negative CHECK (total_stock >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sales (
    sale_id BIGINT NOT NULL AUTO_INCREMENT,
    sale_date DATE NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    PRIMARY KEY (sale_id),
    INDEX idx_sales_sale_date (sale_date),
    INDEX idx_sales_transaction_type (transaction_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE bills (
    serial_number BIGINT NOT NULL AUTO_INCREMENT,
    sale_id BIGINT NOT NULL,
    total_price DECIMAL(12,2) NOT NULL,
    discount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    cash_tendered DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    change_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    bill_date DATE NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    PRIMARY KEY (serial_number),
    CONSTRAINT uk_bills_sale UNIQUE (sale_id),
    CONSTRAINT fk_bills_sale FOREIGN KEY (sale_id) REFERENCES sales (sale_id),
    CONSTRAINT chk_bills_total_non_negative CHECK (total_price >= 0),
    CONSTRAINT chk_bills_discount_non_negative CHECK (discount >= 0),
    CONSTRAINT chk_bills_cash_non_negative CHECK (cash_tendered >= 0),
    CONSTRAINT chk_bills_change_non_negative CHECK (change_amount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_bills_bill_date ON bills (bill_date);

CREATE TABLE bill_items (
    bill_item_id BIGINT NOT NULL AUTO_INCREMENT,
    bill_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name_snapshot VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    line_total DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (bill_item_id),
    CONSTRAINT fk_bill_items_bill FOREIGN KEY (bill_id) REFERENCES bills (serial_number),
    CONSTRAINT fk_bill_items_product FOREIGN KEY (product_id) REFERENCES products (product_id),
    CONSTRAINT chk_bill_items_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_bill_items_unit_price_non_negative CHECK (unit_price >= 0),
    CONSTRAINT chk_bill_items_line_total_non_negative CHECK (line_total >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_bill_items_bill_id ON bill_items (bill_id);
CREATE INDEX idx_bill_items_product_id ON bill_items (product_id);
