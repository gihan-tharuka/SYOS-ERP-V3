<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Edit Reorder Level</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">SYOS ERP - Edit Reorder Level</h1>
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
                        <h2 class="text-xl font-semibold text-gray-800 mb-6 border-b pb-2">Edit Reorder Level</h2>

                        <form action="${pageContext.request.contextPath}/reorderlevel" method="post">
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="reorderLevelId" value="${reorderLevel.reorderLevelId}">

                            <!-- Item ID -->
                            <div class="mb-4">
                                <label for="itemId" class="block text-gray-700 text-sm font-bold mb-2">Item ID:</label>
                                <input type="number" id="itemId" name="itemId" value="${reorderLevel.itemId}" readonly
                                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline bg-gray-100">
                            </div>

                            <!-- Threshold Quantity -->
                            <div class="mb-6">
                                <label for="thresholdQuantity" class="block text-gray-700 text-sm font-bold mb-2">Threshold Quantity:</label>
                                <input type="number" id="thresholdQuantity" name="thresholdQuantity" value="${reorderLevel.thresholdQuantity}" required min="0"
                                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                            </div>

                            <!-- Current Stock -->
                            <div class="mb-6">
                                <label class="block text-gray-700 text-sm font-bold mb-2">Current Stock:</label>
                                <div class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight bg-gray-100">
                                    ${reorderLevel.totalStock}
                                </div>
                            </div>

                            <!-- Buttons -->
                            <div class="flex items-center justify-between">
                                <button type="submit" class="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline transition duration-200">
                                    Update Reorder Level
                                </button>
                                <a href="${pageContext.request.contextPath}/reorderlevel" class="text-gray-600 hover:text-gray-800 font-medium">
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