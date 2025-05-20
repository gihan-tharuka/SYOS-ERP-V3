<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bill Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <h1>Bill Details</h1>
        <div class="bill-details">
            <div class="bill-header">
                <p><strong>Serial Number:</strong> ${bill.serialNumber}</p>
                <p><strong>Date:</strong> ${bill.billDate}</p>
                <p><strong>Payment Method:</strong> ${bill.paymentMethod}</p>
            </div>
            
            <table class="bill-items">
                <thead>
                    <tr>
                        <th>Item Name</th>
                        <th>Quantity</th>
                        <th>Total Price</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${billItems}" var="item">
                        <tr>
                            <td>${item.itemName}</td>
                            <td>${item.quantity}</td>
                            <td>${item.itemTotalPrice}</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2"><strong>Total Price:</strong></td>
                        <td>${bill.totalPrice}</td>
                    </tr>
                    <tr>
                        <td colspan="2"><strong>Discount:</strong></td>
                        <td>${bill.discount}</td>
                    </tr>
                    <tr>
                        <td colspan="2"><strong>Cash Tendered:</strong></td>
                        <td>${bill.cashTendered}</td>
                    </tr>
                    <tr>
                        <td colspan="2"><strong>Change Amount:</strong></td>
                        <td>${bill.changeAmount}</td>
                    </tr>
                </tfoot>
            </table>
        </div>
        
        <div class="actions">
            <a href="${pageContext.request.contextPath}/sales/viewAll" class="btn">Back to Sales List</a>
            <button onclick="window.print()" class="btn">Print Bill</button>
        </div>
    </div>
</body>
</html> 