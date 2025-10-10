<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Cashier Dashboard</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <h1>Cashier Dashboard</h1>
        <div class="welcome-message">
            Welcome, <%= session.getAttribute("username") %>!
        </div>
        <div class="dashboard-menu">
            <h2>Menu Options</h2>
            <ul>
                <li><a href="main-stock.jsp">View Main Stock</a></li>
                <li><a href="shelf-stock.jsp">View Shelf Stock</a></li>
                <li><a href="sales.jsp">Process Sales</a></li>
                <li><a href="reports.jsp">View Reports</a></li>
            </ul>
        </div>
        <div class="logout">
            <a href="../logout.jsp">Logout</a>
        </div>
    </div>
</body>
</html> 