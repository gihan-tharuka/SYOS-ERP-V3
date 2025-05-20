<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Add New Item</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
    <div class="container mx-auto px-4 py-8 max-w-3xl">
        <h1 class="text-3xl font-bold text-gray-800 mb-6 text-center">Add New Item</h1>

        <form action="item-management" method="post" class="bg-white shadow-md rounded-lg p-6">
            <input type="hidden" name="action" value="add">

            <!-- Item Code -->
            <div class="mb-4">
                <label for="code" class="block text-gray-700 text-sm font-bold mb-2">Item Code:</label>
                <input type="text" id="code" name="code" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <!-- Item Name -->
            <div class="mb-4">
                <label for="name" class="block text-gray-700 text-sm font-bold mb-2">Item Name:</label>
                <input type="text" id="name" name="name" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <!-- Price -->
            <div class="mb-4">
                <label for="price" class="block text-gray-700 text-sm font-bold mb-2">Price:</label>
                <input type="number" id="price" name="price" step="0.01" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <!-- Discount -->
            <div class="mb-6">
                <label for="discount" class="block text-gray-700 text-sm font-bold mb-2">Discount:</label>
                <input type="number" id="discount" name="discount" step="0.01" value="0.00"
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <!-- Buttons -->
            <div class="flex items-center justify-between">
                <button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                    Add Item
                </button>
                <a href="item-management" class="text-gray-600 hover:text-gray-800 font-medium">
                    Cancel
                </a>
            </div>
        </form>

        <!-- Back to Dashboard Link -->
        <div class="mt-6 text-center">
            <a href="../../admin/dashboard.jsp" class="text-gray-600 hover:text-gray-800 font-medium">
                ← Back to Dashboard
            </a>
        </div>
    </div>
</body>
</html>
