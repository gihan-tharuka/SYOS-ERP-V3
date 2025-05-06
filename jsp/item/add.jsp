<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Add New Item</title>
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body>
    <div class="container">
        <h1>Add New Item</h1>
        
        <form action="item-management" method="post" class="item-form">
            <input type="hidden" name="action" value="add">
            
            <div class="form-group">
                <label for="code">Item Code:</label>
                <input type="text" id="code" name="code" required>
            </div>
            
            <div class="form-group">
                <label for="name">Item Name:</label>
                <input type="text" id="name" name="name" required>
            </div>
            
            <div class="form-group">
                <label for="price">Price:</label>
                <input type="number" id="price" name="price" step="0.01" required>
            </div>
            
            <div class="form-group">
                <label for="discount">Discount:</label>
                <input type="number" id="discount" name="discount" step="0.01" value="0.00">
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn">Add Item</button>
                <a href="item-management" class="btn-cancel">Cancel</a>
            </div>
        </form>
        
        <div class="back-link">
            <a href="../../admin/dashboard.jsp">Back to Dashboard</a>
        </div>
    </div>
</body>
</html> 