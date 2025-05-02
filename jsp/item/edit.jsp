<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Item</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-container {
            max-width: 500px;
            margin: 20px auto;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Edit Item</h2>
        
        <div class="form-container">
            <form action="${pageContext.request.contextPath}/items/edit" method="post">
                <div class="mb-3">
                    <label for="itemCode" class="form-label">Item Code</label>
                    <input type="text" class="form-control" id="itemCode" name="itemCode" 
                           value="${item.itemCode}" readonly>
                </div>
                
                <div class="mb-3">
                    <label for="itemName" class="form-label">Item Name</label>
                    <input type="text" class="form-control" id="itemName" name="itemName" 
                           value="${item.itemName}" required>
                </div>
                
                <div class="mb-3">
                    <label for="price" class="form-label">Price</label>
                    <input type="number" step="0.01" class="form-control" id="price" name="price" 
                           value="${item.price}" required>
                </div>
                
                <div class="mb-3">
                    <label for="discount" class="form-label">Discount</label>
                    <input type="number" step="0.01" class="form-control" id="discount" name="discount" 
                           value="${item.discount}">
                </div>
                
                <div class="mb-3">
                    <button type="submit" class="btn btn-primary">Update Item</button>
                    <a href="${pageContext.request.contextPath}/items" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 