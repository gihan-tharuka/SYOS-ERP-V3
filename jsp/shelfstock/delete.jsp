<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Delete Shelf Stock</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Delete Shelf Stock</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="card mt-4">
            <div class="card-body">
                <h5 class="card-title">Confirm Deletion</h5>
                <p class="card-text">Are you sure you want to delete this shelf stock? This action cannot be undone.</p>
                
                <form action="${pageContext.request.contextPath}/shelfstock/delete" method="post">
                    <input type="hidden" name="itemId" value="${param.itemId}">
                    <div class="mb-3">
                        <button type="submit" class="btn btn-danger">Confirm Delete</button>
                        <a href="${pageContext.request.contextPath}/shelfstock/view" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 