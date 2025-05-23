<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Shopping Cart</h1>
                    <a href="${pageContext.request.contextPath}/webstore" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                        Continue Shopping
                    </a>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <c:if test="${not empty error}">
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                    <span class="block sm:inline">${error}</span>
                </div>
            </c:if>

            <c:if test="${empty cart}">
                <div class="bg-white rounded-lg shadow-md p-6 text-center">
                    <div class="text-gray-600 mb-4">Your cart is empty.</div>
                    <a href="${pageContext.request.contextPath}/webstore" class="inline-block bg-indigo-600 text-white hover:bg-indigo-700 px-4 py-2 rounded-md font-medium transition duration-200">
                        Continue Shopping
                    </a>
                </div>
            </c:if>

            <c:if test="${not empty cart}">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Name</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Price</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:forEach items="${cart}" var="item" varStatus="status">
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.itemName}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.quantity}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Rs. ${item.price}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Rs. ${item.totalPrice}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <form action="${pageContext.request.contextPath}/cart" method="post" class="inline">
                                                <input type="hidden" name="action" value="remove">
                                                <input type="hidden" name="index" value="${status.index}">
                                                <button type="submit" class="text-red-600 hover:text-red-900">Remove</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot class="bg-gray-50">
                                <tr>
                                    <td colspan="3" class="px-6 py-4 text-right text-sm font-medium text-gray-900">Total Amount:</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">Rs. ${totalAmount}</td>
                                    <td></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                    <div class="p-6 border-t border-gray-200">
                        <form action="${pageContext.request.contextPath}/checkout" method="post" class="max-w-md">
                            <div class="mb-4">
                                <label for="paymentMethod" class="block text-sm font-medium text-gray-700 mb-1">Payment Method:</label>
                                <select class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500" 
                                        id="paymentMethod" name="paymentMethod" required>
                                    <option value="">Select Payment Method</option>
                                    <option value="CARD">Card</option>
                                    <option value="CASH_ON_DELIVERY">Cash on Delivery</option>
                                </select>
                            </div>
                            <button type="submit" class="w-full bg-indigo-600 text-white hover:bg-indigo-700 px-4 py-2 rounded-md font-medium transition duration-200">
                                Proceed to Checkout
                            </button>
                        </form>
                    </div>
                </div>
            </c:if>
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