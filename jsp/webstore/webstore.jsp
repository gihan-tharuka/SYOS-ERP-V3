<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Web Store</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Web Store</h1>
                    <div class="flex items-center space-x-4">
                        <a href="${pageContext.request.contextPath}/jsp/customer/dashboard.jsp" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                            Dashboard
                        </a>
                        <a href="${pageContext.request.contextPath}/cart" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                            View Cart
                        </a>
                    </div>
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

            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <c:forEach items="${webStocks}" var="entry">
                    <div class="bg-white rounded-lg shadow-md overflow-hidden">
                        <div class="p-6">
                            <h5 class="text-xl font-semibold text-gray-800 mb-4">${webStockItems[entry.key].itemName}</h5>
                            <div class="space-y-2 text-gray-600 mb-4">
                                <p>Available Quantity: ${entry.key.currentQuantity}</p>
                                <p>Item Code: ${entry.value}</p>
                                <p class="text-indigo-600 font-semibold">Rs. ${webStockItems[entry.key].price}</p>
                            </div>
                            <form action="${pageContext.request.contextPath}/webstore" method="post">
                                <input type="hidden" name="action" value="addToCart">
                                <input type="hidden" name="itemId" value="${entry.key.itemId}">
                                <div class="mb-4">
                                    <label for="quantity" class="block text-sm font-medium text-gray-700 mb-1">Quantity:</label>
                                    <input type="number" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500" 
                                           id="quantity" name="quantity" min="1" max="${entry.key.currentQuantity}" value="1" required>
                                </div>
                                <button type="submit" class="w-full bg-indigo-600 text-white hover:bg-indigo-700 px-4 py-2 rounded-md font-medium transition duration-200">
                                    Add to Cart
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
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