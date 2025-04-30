-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 01, 2025 at 05:17 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `authentication`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `admin_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`admin_id`, `username`, `password`, `email`) VALUES
(1, 'admin1', 'password123', 'admin1@example.com'),
(2, 'admin2', 'password456', 'admin2@example.com'),
(4, 'admingayan', 'gayan123', 'gayan@gmail.com'),
(5, 'nimesha', 'nimesha123', 'nimesha@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `bills`
--

CREATE TABLE `bills` (
  `serial_number` int(11) NOT NULL,
  `sale_id` int(11) NOT NULL,
  `total_price` double NOT NULL,
  `discount` double NOT NULL,
  `cash_tendered` double NOT NULL,
  `change_amount` double NOT NULL,
  `bill_date` date NOT NULL,
  `payment_method` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bills`
--

INSERT INTO `bills` (`serial_number`, `sale_id`, `total_price`, `discount`, `cash_tendered`, `change_amount`, `bill_date`, `payment_method`) VALUES
(1, 1, 35000, 0, 40000, 5000, '2025-01-25', 'Cash'),
(2, 2, 5000, 0, 10000, 5000, '2025-01-25', 'Cash'),
(3, 3, 5000, 0, 10000, 5000, '2025-01-27', 'Cash'),
(4, 4, 10000, 0, 12000, 2000, '2025-01-27', 'Cash'),
(5, 5, 2400, 0, 3000, 600, '2025-02-01', 'Cash');

-- --------------------------------------------------------

--
-- Table structure for table `bill_items`
--

CREATE TABLE `bill_items` (
  `bill_item_id` int(11) NOT NULL,
  `bill_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL,
  `item_total_price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bill_items`
--

INSERT INTO `bill_items` (`bill_item_id`, `bill_id`, `item_id`, `item_name`, `quantity`, `item_total_price`) VALUES
(1, 1, 1, 'Laptop', 7, 10500),
(2, 1, 5, 'School Bag', 7, 24500),
(3, 2, 1, 'Laptop', 1, 1500),
(4, 2, 5, 'School Bag', 1, 3500),
(5, 3, 1, 'Laptop', 1, 1500),
(6, 3, 5, 'School Bag', 1, 3500),
(7, 4, 1, 'Laptop', 2, 3000),
(8, 4, 5, 'School Bag', 2, 7000),
(9, 5, 12, 'White rice', 20, 2400);

-- --------------------------------------------------------

--
-- Table structure for table `cashiers`
--

CREATE TABLE `cashiers` (
  `cashier_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cashiers`
--

INSERT INTO `cashiers` (`cashier_id`, `username`, `password`, `full_name`, `email`, `mobile`) VALUES
(1, 'cashier1', 'password789', 'John Doe', 'cashier1@gmail.com', '1234567890'),
(3, 'cashierkenula', 'kenula123', 'kenula richard', 'kenula@gmail.com', '011212222');

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `item_id` int(11) NOT NULL,
  `item_code` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `discount` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`item_id`, `item_code`, `item_name`, `price`, `discount`) VALUES
(1, 'ITEM001', 'Laptop', 1500, 10),
(2, 'ITEM002', 'Smartphone', 800, 5),
(3, 'SOAP001', 'Lux', 80, 0),
(4, 'WATER001', 'Water', 100, 0),
(5, 'BAG001', 'School Bag', 3500, 250),
(7, 'JUICE001', 'Mango', 200, 20),
(8, 'PEN001', 'Pem', 100, 10),
(9, 'HAT001', 'HAT', 2000, 20),
(12, 'RICE001', 'White rice', 120, 10);

-- --------------------------------------------------------

--
-- Table structure for table `main_stock`
--

CREATE TABLE `main_stock` (
  `stock_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `supplier_id` int(11) NOT NULL,
  `batch_code` varchar(255) NOT NULL,
  `purchase_date` date NOT NULL,
  `purchase_price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `expiry_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `main_stock`
--

INSERT INTO `main_stock` (`stock_id`, `item_id`, `supplier_id`, `batch_code`, `purchase_date`, `purchase_price`, `quantity`, `expiry_date`) VALUES
(1, 1, 1, 'BATCH001', '2024-12-12', 50, 50, '2025-02-02'),
(2, 5, 2, 'BATCH002', '2024-12-24', 500, 70, '2025-12-12'),
(3, 5, 2, 'BATCH003', '2025-01-01', 600, 100, '2025-01-30'),
(6, 4, 1, 'WATERB001', '2025-01-01', 300, 50, '2025-06-01'),
(7, 4, 2, 'WATERB02', '2000-02-02', 200, 20, '2025-02-02'),
(9, 12, 1, 'BATCH0111', '2025-01-01', 200, 60, '2025-03-01');

-- --------------------------------------------------------

--
-- Table structure for table `reorder_levels`
--

CREATE TABLE `reorder_levels` (
  `reorder_level_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `threshold_quantity` int(11) DEFAULT 50,
  `total_stock` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reorder_levels`
--

INSERT INTO `reorder_levels` (`reorder_level_id`, `item_id`, `threshold_quantity`, `total_stock`) VALUES
(1, 5, 50, 30),
(2, 9, 50, 0),
(3, 12, 50, 60);

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `sale_id` int(11) NOT NULL,
  `sale_date` date NOT NULL,
  `transaction_type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`sale_id`, `sale_date`, `transaction_type`) VALUES
(1, '2025-01-25', ''),
(2, '2025-01-25', ''),
(3, '2025-01-27', ''),
(4, '2025-01-27', ''),
(5, '2025-02-01', '');

-- --------------------------------------------------------

--
-- Table structure for table `shelf_stock`
--

CREATE TABLE `shelf_stock` (
  `shelf_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `shelf_capacity` int(11) NOT NULL,
  `current_quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `shelf_stock`
--

INSERT INTO `shelf_stock` (`shelf_id`, `item_id`, `shelf_capacity`, `current_quantity`) VALUES
(1, 1, 50, 39),
(2, 5, 40, 19),
(3, 12, 50, 0);

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `contact_person` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `username`, `password`, `company_name`, `contact_person`, `email`, `mobile`) VALUES
(1, 'supplier1', 'password123', 'ABC Supply Co.', 'John Doe', 'john.doe@abc.com', '1234567890'),
(2, 'supplier2', 'password456', 'XYZ Enterprises', 'Jane Smith', 'jane.smith@xyz.com', '0987654321'),
(3, 'supplierchamma', 'chamma123', 'chammaholdings', 'kadiya', 'chamma@gmail.com', '011222222'),
(4, 'suphirusha', 'hirusha123', 'hirushaholdings', 'tharusha', 'hirusha@gmail.com', '0887659898'),
(5, 'nemitha', 'nemitha', 'nemithsons', 'nomath', 'nemitha@gmail.com', '08877663');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `bills`
--
ALTER TABLE `bills`
  ADD PRIMARY KEY (`serial_number`),
  ADD KEY `sale_id` (`sale_id`);

--
-- Indexes for table `bill_items`
--
ALTER TABLE `bill_items`
  ADD PRIMARY KEY (`bill_item_id`),
  ADD KEY `bill_id` (`bill_id`),
  ADD KEY `item_id` (`item_id`);

--
-- Indexes for table `cashiers`
--
ALTER TABLE `cashiers`
  ADD PRIMARY KEY (`cashier_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`item_id`),
  ADD UNIQUE KEY `item_name` (`item_name`);

--
-- Indexes for table `main_stock`
--
ALTER TABLE `main_stock`
  ADD PRIMARY KEY (`stock_id`),
  ADD KEY `item_id` (`item_id`),
  ADD KEY `supplier_id` (`supplier_id`);

--
-- Indexes for table `reorder_levels`
--
ALTER TABLE `reorder_levels`
  ADD PRIMARY KEY (`reorder_level_id`),
  ADD KEY `item_id` (`item_id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`sale_id`);

--
-- Indexes for table `shelf_stock`
--
ALTER TABLE `shelf_stock`
  ADD PRIMARY KEY (`shelf_id`),
  ADD KEY `item_id` (`item_id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `bills`
--
ALTER TABLE `bills`
  MODIFY `serial_number` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `bill_items`
--
ALTER TABLE `bill_items`
  MODIFY `bill_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `cashiers`
--
ALTER TABLE `cashiers`
  MODIFY `cashier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `main_stock`
--
ALTER TABLE `main_stock`
  MODIFY `stock_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `reorder_levels`
--
ALTER TABLE `reorder_levels`
  MODIFY `reorder_level_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
  MODIFY `sale_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `shelf_stock`
--
ALTER TABLE `shelf_stock`
  MODIFY `shelf_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bills`
--
ALTER TABLE `bills`
  ADD CONSTRAINT `bills_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`sale_id`);

--
-- Constraints for table `bill_items`
--
ALTER TABLE `bill_items`
  ADD CONSTRAINT `bill_items_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`serial_number`),
  ADD CONSTRAINT `bill_items_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`);

--
-- Constraints for table `main_stock`
--
ALTER TABLE `main_stock`
  ADD CONSTRAINT `main_stock_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`),
  ADD CONSTRAINT `main_stock_ibfk_2` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`);

--
-- Constraints for table `reorder_levels`
--
ALTER TABLE `reorder_levels`
  ADD CONSTRAINT `reorder_levels_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`);

--
-- Constraints for table `shelf_stock`
--
ALTER TABLE `shelf_stock`
  ADD CONSTRAINT `shelf_stock_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
