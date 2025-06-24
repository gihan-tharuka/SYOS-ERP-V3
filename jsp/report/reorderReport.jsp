<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="template.ReorderReport" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Reorder Report</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Reorder Report</h1>
                    <div class="flex items-center space-x-4">
                        <a href="${pageContext.request.contextPath}/reports" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                            Back to Reports
                        </a>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-7xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Code</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Name</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Current Quantity</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity to Reorder</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <%
                                    ReorderReport report = (ReorderReport) request.getAttribute("report");
                                    List<Object[]> reorderItems = report.getReorderItems();
                                    for (Object[] item : reorderItems) {
                                        String itemCode = (String) item[0];
                                        String itemName = (String) item[1];
                                        int currentQuantity = (int) item[2];
                                        int quantityToReorder = (int) item[3];
                                    %>
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= itemCode %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= itemName %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= currentQuantity %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= quantityToReorder %></td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
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