<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Edit Item</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">SYOS ERP - Edit Item</h1>
                    <a href="../admin/dashboard.jsp" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                        Back to Dashboard
                    </a>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-3xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6 border-b pb-2">Edit Item</h2>
                        
                        <form action="item-management" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="edit">
                            
                            <div class="mb-4">
                                <label for="code" class="block text-gray-700 text-sm font-bold mb-2">Item Code:</label>
                                <input type="text" id="code" name="code" value="${item.itemCode}" readonly
                                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline bg-gray-100">
                            </div>
                            
                            <div class="mb-4">
                                <label for="name" class="block text-gray-700 text-sm font-bold mb-2">Item Name:</label>
                                <input type="text" id="name" name="name" value="${item.itemName}" required
                                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                            </div>
                            
                            <div class="mb-4">
                                <label for="price" class="block text-gray-700 text-sm font-bold mb-2">Price:</label>
                                <input type="number" id="price" name="price" step="0.01" value="${item.price}" required
                                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                            </div>
                            
                            <div class="mb-6">
                                <label for="discount" class="block text-gray-700 text-sm font-bold mb-2">Discount:</label>
                                <input type="number" id="discount" name="discount" step="0.01" value="${item.discount}"
                                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                            </div>
                            
                            <!-- Image Upload -->
                            <div class="mb-6">
                                <label for="image" class="block text-gray-700 text-sm font-bold mb-2">Item Image:</label>
                                <input type="file" id="image" name="image" accept="image/*"
                                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                                <c:if test="${not empty item.imagePath}">
                                    <div class="mt-2">
                                        <img src="/${item.imagePath}" alt="Current Image" class="h-24 rounded border" />
                                        <p class="text-xs text-gray-500">Current image</p>
                                    </div>
                                </c:if>
                            </div>
                            
                            <div class="flex items-center justify-between">
                                <button type="submit" class="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline transition duration-200">
                                    Update Item
                                </button>
                                <a href="item-management" class="text-gray-600 hover:text-gray-800 font-medium">
                                    Cancel
                                </a>
                            </div>
                        </form>
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