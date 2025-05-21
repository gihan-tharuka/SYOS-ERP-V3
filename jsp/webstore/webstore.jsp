<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Web Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .item-card {
            margin-bottom: 20px;
        }
        .cart-button {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1000;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1 class="mb-4">Web Store</h1>
        
        <a href="${pageContext.request.contextPath}/cart" class="btn btn-primary cart-button">
            View Cart
        </a>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="row">
            <c:forEach items="${webStocks}" var="entry">
                <div class="col-md-4">
                    <div class="card item-card">
                        <div class="card-body">
                            <h5 class="card-title">${entry.key.itemName}</h5>
                            <p class="card-text">
                                Available Quantity: ${entry.key.currentQuantity}<br>
                                Item Code: ${entry.value}
                            </p>
                            <form action="${pageContext.request.contextPath}/webstore" method="post">
                                <input type="hidden" name="action" value="addToCart">
                                <input type="hidden" name="itemId" value="${entry.key.itemId}">
                                <div class="mb-3">
                                    <label for="quantity" class="form-label">Quantity:</label>
                                    <input type="number" class="form-control" id="quantity" name="quantity" 
                                           min="1" max="${entry.key.currentQuantity}" value="1" required>
                                </div>
                                <button type="submit" class="btn btn-primary">Add to Cart</button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 