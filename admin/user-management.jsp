<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - User Management</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <h1>User Management</h1>
        <div class="welcome-message">
            Welcome, <%= session.getAttribute("username") %>!
        </div>
        
        <div class="user-management-menu">
            <h2>User Management Options</h2>
            <ul>
                <li><a href="jsp/users/list.jsp">View All Users</a></li>
                <li><a href="jsp/users/add.jsp">Add New User</a></li>
                <li><a href="jsp/users/search.jsp">Search Users</a></li>
            </ul>
        </div>
        
        <div class="back-link">
            <a href="dashboard.jsp">Back to Dashboard</a>
        </div>
    </div>
</body>
</html> 