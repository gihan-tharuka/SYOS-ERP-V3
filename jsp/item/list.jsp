<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Item Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">SYOS ERP - Item Management</h1>
                    <a href="../admin/dashboard.jsp" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                        Back to Dashboard
                    </a>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-6xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <div class="flex justify-between items-center mb-6">
                            <h2 class="text-xl font-semibold text-gray-800">Item List</h2>
                            <a href="item-management?action=add" class="bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-4 rounded transition duration-200">
                                Add New Item
                            </a>
                        </div>
                        
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Code</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Price</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Discount</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Image</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <c:forEach var="item" items="${items}">
                                        <tr>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${item.itemCode}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${item.itemName}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${item.price}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${item.discount}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">
                                                <c:if test="${not empty item.imagePath}">
                                                    <img src="/${item.imagePath}" alt="Item Image" class="h-12 w-12 object-cover rounded border" />
                                                </c:if>
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                                <a href="item-management?action=edit&code=${item.itemCode}" class="text-indigo-600 hover:text-indigo-900 mr-3">Edit</a>
                                                <a href="item-management?action=delete&code=${item.itemCode}" class="text-red-600 hover:text-red-900" 
                                                   onclick="return confirm('Are you sure you want to delete this item?')">Delete</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
                <div class="mt-6 text-center">
                    <a href="../admin/dashboard.jsp" class="text-gray-600 hover:text-gray-800 font-medium">
                        ← Back to Dashboard
                    </a>
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