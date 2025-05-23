<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bill</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @media print {
            .no-print {
                display: none;
            }
        }
    </style>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Bill</h1>
                    <div class="no-print">
                        <button onclick="window.print()" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                            Print Bill
                        </button>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-3xl mx-auto bg-white rounded-lg shadow-md overflow-hidden">
                <div class="p-8">
                    <div class="text-center mb-8">
                        <h2 class="text-2xl font-bold text-gray-800 mb-2">Bill</h2>
                        <p class="text-gray-600">Serial Number: ${bill.serialNumber}</p>
                        <p class="text-gray-600">Date: <fmt:formatDate value="${bill.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                    </div>

                    <div class="mb-6">
                        <p class="text-gray-700"><span class="font-medium">Payment Method:</span> ${bill.paymentMethod}</p>
                    </div>

                    <div class="mb-8">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Name</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Price</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:forEach items="${billItems}" var="item">
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.itemName}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.quantity}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Rs. ${item.price}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Rs. ${item.totalPrice}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot class="bg-gray-50">
                                <tr>
                                    <td colspan="3" class="px-6 py-4 text-right text-sm font-medium text-gray-900">Total Amount:</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">Rs. ${bill.totalPrice}</td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                    <div class="text-center no-print">
                        <a href="${pageContext.request.contextPath}/webstore" class="inline-block bg-indigo-600 text-white hover:bg-indigo-700 px-4 py-2 rounded-md font-medium transition duration-200">
                            Back to Store
                        </a>
                    </div>
                </div>
            </div>
        </main>

        <!-- Footer -->
        <footer class="bg-gray-100 border-t py-4">
            <div class="container mx-auto px-6 text-center text-gray-500 text-sm">
                &copy; 2023 SYOS ERP System. All rights reserved.
            </div>
        </footer>
    </div>
</body>
</html> 