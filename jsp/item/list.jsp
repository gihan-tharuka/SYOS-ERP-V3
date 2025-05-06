<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Item Management</title>
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body>
    <div class="container">
        <h1>Item Management</h1>
        
        <div class="action-buttons">
            <a href="item-management?action=add" class="btn">Add New Item</a>
        </div>
        
        <table class="data-table">
            <thead>
                <tr>
                    <th>Code</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Discount</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${items}">
                    <tr>
                        <td>${item.itemCode}</td>
                        <td>${item.itemName}</td>
                        <td>${item.price}</td>
                        <td>${item.discount}</td>
                        <td>
                            <a href="item-management?action=edit&code=${item.itemCode}" class="btn-edit">Edit</a>
                            <a href="item-management?action=delete&code=${item.itemCode}" class="btn-delete" 
                               onclick="return confirm('Are you sure you want to delete this item?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <div class="back-link">
            <a href="../../admin/dashboard.jsp">Back to Dashboard</a>
        </div>
    </div>
</body>
</html> 