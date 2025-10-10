# SYOS-ERP-V3 (Synex Outlet Store System)

## Overview

SYOS-ERP-V3 is a comprehensive Enterprise Resource Planning (ERP) system designed specifically for retail outlet management. The system handles inventory management, sales operations, user management, reporting, and customer account management, providing a complete solution for managing retail operations efficiently.

## Features

- **Multi-User Role Support**:
  - Admin Dashboard
  - Cashier Operations
  - Customer Portal

- **Inventory Management**:
  - Main Stock Management
  - Shelf Stock Management
  - Web Stock Management
  - Automatic Reordering System
  - Item Cataloging

- **Sales Management**:
  - Sales Bill Generation
  - Payment Processing
  - Sales History Tracking
  - Web Store Integration

- **Reporting System**:
  - Daily Sales Reports
  - Stock Reports
  - Reorder Level Reports
  - Bill Reports
  - Reshelve Reports

- **Customer Management**:
  - Registration
  - Account Management
  - Bill History

- **Security**:
  - Role-based Authentication
  - Secure Login System

## Technology Stack

- **Backend**: Java Servlets, JSP
- **Database**: MariaDB/MySQL
- **Server**: Apache Tomcat 10
- **Frontend**: JSP, HTML, CSS
- **Architecture**: MVC pattern with additional design patterns

## Design Patterns

- **Command Pattern**: For encapsulating request operations
- **Observer Pattern**: For implementing reordering notifications
- **Strategy Pattern**: For batch selection and processing
- **DAO Pattern**: For database operations
- **MVC Architecture**: For separation of concerns

## System Requirements

- Java Development Kit (JDK) 17 or higher
- Apache Tomcat 10.x
- MariaDB 10.4.x or MySQL equivalent
- Minimum 4GB RAM
- 50GB storage space (recommended)

## Installation

### Prerequisites
1. Install Java Development Kit (JDK) 17+
2. Install Apache Tomcat 10.x
3. Install MariaDB 10.4.x or MySQL

### Database Setup
1. Create a new database named `authentication`
2. Import the database schema from `resources/authentication.sql`:
   ```
   mysql -u yourusername -p authentication < resources/authentication.sql
   ```

### Application Setup
1. Clone the repository:
   ```
   git clone https://github.com/gihan-tharuka/SYOS-ERP-V3.git
   ```
2. Deploy the application to Tomcat:
   - Copy the project folder to Tomcat's `webapps` directory
   - OR create a WAR file and deploy it through the Tomcat Manager

3. Configure database connection:
   - Modify the connection parameters in the `ConnectionPool.java` file if necessary

4. Start Tomcat server:
   ```
   <tomcat-dir>/bin/startup.bat   # For Windows
   <tomcat-dir>/bin/startup.sh    # For Linux/Mac
   ```

5. Access the application:
   ```
   http://localhost:8080/SYOS-ERP-V3/
   ```

## Usage Guide

### Login Access
- **Admin**: Use admin credentials to access administrative features
- **Cashier**: Use cashier credentials for sales and inventory operations
- **Customer**: Customers can register and access their account information

### Key Operations

#### For Administrators
- Manage users (add, edit, delete)
- Manage items and inventory
- Generate and view reports
- Configure system settings

#### For Cashiers
- Process sales transactions
- Manage shelf stock
- Verify inventory
- Generate bills

#### For Customers
- Browse products in web store
- Add items to cart
- Complete purchases
- View order history

## Project Structure

```
SYOS-ERP-V3/
├── admin/             # Admin interfaces
├── cashier/           # Cashier interfaces
├── jsp/               # JSP files for all views
├── lib/               # Required libraries
├── resources/         # Resources including SQL scripts and images
├── src/               # Source code
│   ├── authentication/    # Authentication handlers
│   ├── command/           # Command pattern implementations
│   ├── controller/        # MVC controllers
│   ├── dao/               # Data Access Objects
│   ├── factory/           # Factory pattern implementations
│   ├── model/             # Data models
│   ├── observer/          # Observer pattern for reordering
│   ├── servlets/          # Servlet implementations
│   ├── strategy/          # Strategy pattern implementations
│   ├── template/          # Template pattern implementations
│   └── view/              # View interfaces
├── WEB-INF/           # Web configuration
└── user/              # User-specific content
```

## Database Schema

The system uses a relational database with the following key tables:
- Users (Admin, Cashier, Customer)
- Items and Inventory
- Main Stock
- Shelf Stock
- Web Stock
- Sales and Billing information
- Reorder levels

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Authors

- **Gihan Tharuka** - *Initial work and maintenance* - [gihan-tharuka](https://github.com/gihan-tharuka)

## Acknowledgments

- Thanks to all contributors who have helped with the development of this system
- Inspiration from best practices in retail ERP systems
- Open-source community for providing tools and frameworks

## Changelog

### Version 3.0.0 (Current)
- Added web store functionality
- Improved reporting system
- Enhanced user interface
- Added customer portal
- Implemented reorder notification system

### Version 2.0.0
- Added cashier management
- Enhanced inventory tracking
- Improved sales workflow

### Version 1.0.0
- Initial release with basic functionality

## Contact Information

For questions, support, or contributions, please contact:
- **Email**: [gihantharuka2499@gmail.com](mailto:gihantharuka2499@gmail.com)
- **GitHub**: [github.com/gihan-tharuka](https://github.com/gihan-tharuka)
