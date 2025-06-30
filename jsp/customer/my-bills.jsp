<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Bills</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">My Bills</h1>
                    <a href="${pageContext.request.contextPath}/customer/dashboard.jsp" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">Back to Dashboard</a>
                </div>
            </div>
        </header>
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-md overflow-hidden">
                <div class="p-8">
                    <h2 class="text-xl font-semibold text-gray-800 mb-6">Your Bills</h2>
                    <c:if test="${not empty error}">
                        <div class="bg-red-100 text-red-700 px-4 py-2 rounded mb-4">${error}</div>
                    </c:if>
                    <c:choose>
                        <c:when test="${empty bills}">
                            <div class="text-gray-600">No bills found.</div>
                        </c:when>
                        <c:otherwise>
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Serial Number</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Payment Method</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <c:forEach items="${bills}" var="bill">
                                        <tr>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${bill.serialNumber}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><fmt:formatDate value="${bill.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Rs. ${bill.totalPrice}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${bill.paymentMethod}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                                <a href="${pageContext.request.contextPath}/customer-bills?serialNumber=${bill.serialNumber}" class="text-indigo-600 hover:underline">View</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
        <footer class="bg-gray-100 border-t py-4">
            <div class="container mx-auto px-6 text-center text-gray-500 text-sm">
                &copy; 2023 SYOS ERP System. All rights reserved.
            </div>
        </footer>
    </div>
</body>
</html> 