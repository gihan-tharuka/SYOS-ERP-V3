<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="template.StockReport" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Date" %>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Stock Report</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Stock Report</h1>
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
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Batch Code</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Expiry Date</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <%
                                    StockReport report = (StockReport) request.getAttribute("report");
                                    List<Object[]> stockDetails = report.getStockDetails();
                                    for (Object[] stock : stockDetails) {
                                        String itemCode = (String) stock[0];
                                        String itemName = (String) stock[1];
                                        String batchCode = (String) stock[2];
                                        int quantity = (int) stock[3];
                                        Date expiryDate = (Date) stock[4];
                                    %>
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= itemCode %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= itemName %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= batchCode %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= quantity %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= expiryDate %></td>
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