<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Shelf Stock</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Add New Shelf Stock</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/shelfstock/add" method="post" class="mt-4">
            <div class="mb-3">
                <label for="itemCode" class="form-label">Item Code:</label>
                <input type="text" class="form-control" id="itemCode" name="itemCode" required>
            </div>
            
            <div class="mb-3">
                <label for="shelfCapacity" class="form-label">Shelf Capacity:</label>
                <input type="number" class="form-control" id="shelfCapacity" name="shelfCapacity" required min="1">
            </div>
            
            <div class="mb-3">
                <button type="submit" class="btn btn-primary">Add Shelf Stock</button>
                <a href="${pageContext.request.contextPath}/shelfstock/view" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 