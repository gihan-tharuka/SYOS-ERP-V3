<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bill</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .bill-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .bill-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .bill-details {
            margin-bottom: 20px;
        }
        .bill-items {
            margin-bottom: 20px;
        }
        .bill-total {
            text-align: right;
            font-weight: bold;
        }
        @media print {
            .no-print {
                display: none;
            }
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <div class="bill-container">
            <div class="bill-header">
                <h2>Bill</h2>
                <p>Serial Number: ${bill.serialNumber}</p>
                <p>Date: <fmt:formatDate value="${bill.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            </div>

            <div class="bill-details">
                <p><strong>Payment Method:</strong> ${bill.paymentMethod}</p>
            </div>

            <div class="bill-items">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Item Name</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${billItems}" var="item">
                            <tr>
                                <td>${item.itemName}</td>
                                <td>${item.quantity}</td>
                                <td>$${item.price}</td>
                                <td>$${item.totalPrice}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3" class="text-end"><strong>Total Amount:</strong></td>
                            <td><strong>$${bill.totalPrice}</strong></td>
                        </tr>
                    </tfoot>
                </table>
            </div>

            <div class="text-center mt-4 no-print">
                <button onclick="window.print()" class="btn btn-primary">Print Bill</button>
                <a href="${pageContext.request.contextPath}/webstore" class="btn btn-secondary">Back to Store</a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 