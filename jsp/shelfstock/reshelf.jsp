<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reshelf Stock</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Reshelf Stock</h2>
        
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="card mt-4">
            <div class="card-body">
                <h5 class="card-title">Reshelving Information</h5>
                <p class="card-text">This operation will automatically reshelf items from main stock to shelf stock based on available quantities and expiry dates.</p>
                
                <form action="${pageContext.request.contextPath}/shelfstock/reshelf" method="post">
                    <div class="mb-3">
                        <button type="submit" class="btn btn-warning" onclick="return confirm('Are you sure you want to proceed with reshelving?')">
                            Proceed with Reshelving
                        </button>
                        <a href="${pageContext.request.contextPath}/shelfstock/view" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>

        <c:if test="${not empty reshelvingInfo}">
            <div class="mt-4">
                <h4>Reshelving Details</h4>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Item Name</th>
                            <th>Reshelf Quantity</th>
                            <th>Batch Code</th>
                            <th>Expiry Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${reshelvingInfo}" var="entry">
                            <c:forEach items="${entry.value}" var="batch">
                                <tr>
                                    <td>${itemNames[entry.key]}</td>
                                    <td>${batch.reshelfQuantity}</td>
                                    <td>${batch.batch.batchCode}</td>
                                    <td>${batch.batch.expiryDate}</td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 