<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1 class="mb-4">Shopping Cart</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <c:if test="${empty cart}">
            <div class="alert alert-info">Your cart is empty.</div>
            <a href="${pageContext.request.contextPath}/webstore" class="btn btn-primary">Continue Shopping</a>
        </c:if>

        <c:if test="${not empty cart}">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Item Name</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Total</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${cart}" var="item" varStatus="status">
                            <tr>
                                <td>${item.itemName}</td>
                                <td>${item.quantity}</td>
                                <td>$${item.price}</td>
                                <td>$${item.totalPrice}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="index" value="${status.index}">
                                        <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3" class="text-end"><strong>Total Amount:</strong></td>
                            <td><strong>$${totalAmount}</strong></td>
                            <td></td>
                        </tr>
                    </tfoot>
                </table>
            </div>

            <div class="row mt-4">
                <div class="col-md-6">
                    <form action="${pageContext.request.contextPath}/checkout" method="post">
                        <div class="mb-3">
                            <label for="paymentMethod" class="form-label">Payment Method:</label>
                            <select class="form-select" id="paymentMethod" name="paymentMethod" required>
                                <option value="">Select Payment Method</option>
                                <option value="CARD">Card</option>
                                <option value="CASH_ON_DELIVERY">Cash on Delivery</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-success">Proceed to Checkout</button>
                    </form>
                </div>
            </div>

            <div class="mt-3">
                <a href="${pageContext.request.contextPath}/webstore" class="btn btn-primary">Continue Shopping</a>
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 