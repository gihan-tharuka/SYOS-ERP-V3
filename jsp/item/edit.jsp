<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Item</title>
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body>
    <div class="container">
        <h1>Edit Item</h1>
        
        <form action="item-management" method="post" class="item-form">
            <input type="hidden" name="action" value="edit">
            
            <div class="form-group">
                <label for="code">Item Code:</label>
                <input type="text" id="code" name="code" value="${item.code}" readonly>
            </div>
            
            <div class="form-group">
                <label for="name">Item Name:</label>
                <input type="text" id="name" name="name" value="${item.name}" required>
            </div>
            
            <div class="form-group">
                <label for="description">Description:</label>
                <textarea id="description" name="description" rows="3">${item.description}</textarea>
            </div>
            
            <div class="form-group">
                <label for="price">Price:</label>
                <input type="number" id="price" name="price" step="0.01" value="${item.price}" required>
            </div>
            
            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="number" id="quantity" name="quantity" value="${item.quantity}" required>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn">Update Item</button>
                <a href="item-management" class="btn-cancel">Cancel</a>
            </div>
        </form>
        
        <div class="back-link">
            <a href="../../admin/dashboard.jsp">Back to Dashboard</a>
        </div>
    </div>
</body>
</html> 