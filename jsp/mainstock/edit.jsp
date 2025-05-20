<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Main Stock</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Edit Main Stock</h1>
                    <a href="${pageContext.request.contextPath}/mainstock/view" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                        Back to List
                    </a>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-2xl mx-auto">
                <div class="bg-white rounded-lg shadow-md p-6">
                    <form action="${pageContext.request.contextPath}/mainstock/edit" method="POST" class="space-y-6">
                        <input type="hidden" name="stockId" value="${mainStock.stockId}">
                        
                        <!-- Item Selection -->
                        <div>
                            <label for="itemCode" class="block text-sm font-medium text-gray-700">Item Code</label>
                            <select id="itemCode" name="itemCode" required class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                                <c:forEach items="${items}" var="item">
                                    <option value="${item.itemCode}" ${item.itemCode == mainStock.itemCode ? 'selected' : ''}>
                                        ${item.itemCode} - ${item.itemName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Supplier Username -->
                        <div>
                            <label for="supplierUsername" class="block text-sm font-medium text-gray-700">Supplier Username</label>
                            <input type="text" id="supplierUsername" name="supplierUsername" value="${mainStock.supplierUsername}" required
                                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                        </div>

                        <!-- Batch Code -->
                        <div>
                            <label for="batchCode" class="block text-sm font-medium text-gray-700">Batch Code</label>
                            <input type="text" id="batchCode" name="batchCode" value="${mainStock.batchCode}" required
                                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                        </div>

                        <!-- Purchase Date -->
                        <div>
                            <label for="purchaseDate" class="block text-sm font-medium text-gray-700">Purchase Date</label>
                            <input type="date" id="purchaseDate" name="purchaseDate" 
                                value="<fmt:formatDate value='${mainStock.purchaseDate}' pattern='yyyy-MM-dd'/>" required
                                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                        </div>

                        <!-- Purchase Price -->
                        <div>
                            <label for="purchasePrice" class="block text-sm font-medium text-gray-700">Purchase Price</label>
                            <input type="number" id="purchasePrice" name="purchasePrice" step="0.01" value="${mainStock.purchasePrice}" required
                                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                        </div>

                        <!-- Quantity -->
                        <div>
                            <label for="quantity" class="block text-sm font-medium text-gray-700">Quantity</label>
                            <input type="number" id="quantity" name="quantity" value="${mainStock.quantity}" required
                                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                        </div>

                        <!-- Expiry Date -->
                        <div>
                            <label for="expiryDate" class="block text-sm font-medium text-gray-700">Expiry Date (Optional)</label>
                            <input type="date" id="expiryDate" name="expiryDate" 
                                value="<fmt:formatDate value='${mainStock.expiryDate}' pattern='yyyy-MM-dd'/>"
                                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                        </div>

                        <!-- Submit Button -->
                        <div class="flex justify-end">
                            <button type="submit" class="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition duration-200">
                                Update Main Stock
                            </button>
                        </div>
                    </form>
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