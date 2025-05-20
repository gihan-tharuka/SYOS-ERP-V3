<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Item Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
    <div class="container mx-auto px-4 py-8">
        <h1 class="text-3xl font-bold text-gray-800 mb-6">Item Management</h1>
        
        <div class="mb-6">
            <a href="item-management?action=add" class="bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded transition duration-200">
                Add New Item
            </a>
        </div>
        
        <div class="bg-white shadow-md rounded-lg overflow-hidden">
            <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Code</th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Price</th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Discount</th>
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
        
        <div class="mt-6">
            <a href="../../admin/dashboard.jsp" class="text-gray-600 hover:text-gray-800 font-medium">
                ← Back to Dashboard
            </a>
        </div>
    </div>
</body>
</html>