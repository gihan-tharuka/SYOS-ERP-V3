<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Admin Dashboard</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <h1>Admin Dashboard</h1>
        <div class="welcome-message">
            Welcome, <%= session.getAttribute("username") %>!
        </div>
        <div class="dashboard-menu">
            <h2>Menu Options</h2>
            <ul>
                <li><a href="user-management.jsp">User Management</a></li>
                <li><a href="../item-management">Item Management</a></li>
                <li><a href="main-stock.jsp">Main Stock Management</a></li>
                <li><a href="shelf-stock.jsp">Shelf Stock Management</a></li>
                <li><a href="sales.jsp">Sales Management</a></li>
                <li><a href="reports.jsp">Reports</a></li>
            </ul>
        </div>
        <div class="logout">
            <a href="../logout.jsp">Logout</a>
        </div>
    </div>
</body>
</html> 