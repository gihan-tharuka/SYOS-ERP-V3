<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="template.ReshelveReport" %>
<%@ page import="model.BatchSelection" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Reshelve Report</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Reshelve Report</h1>
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
                        <div class="space-y-6">
                            <%
                            ReshelveReport report = (ReshelveReport) request.getAttribute("report");
                            Map<Integer, List<BatchSelection>> reshelvingInfo = report.getReshelvingInfo();
                            Map<Integer, Integer> reshelfQuantities = report.getReshelfQuantities();
                            Map<Integer, String> itemNames = report.getItemNames();

                            for (Map.Entry<Integer, List<BatchSelection>> entry : reshelvingInfo.entrySet()) {
                                int itemId = entry.getKey();
                                List<BatchSelection> selectedBatches = entry.getValue();
                                int reshelfQuantity = reshelfQuantities.get(itemId);
                                String itemName = itemNames.get(itemId);
                            %>
                            <div class="bg-gray-50 rounded-lg p-4">
                                <h3 class="text-lg font-semibold text-gray-800 mb-2">
                                    Item ID: <%= itemId %>, Item Name: <%= itemName %>, Reshelf Quantity: <%= reshelfQuantity %>
                                </h3>
                                <div class="overflow-x-auto">
                                    <table class="min-w-full divide-y divide-gray-200">
                                        <thead class="bg-white">
                                            <tr>
                                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Batch Code</th>
                                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Reshelf Quantity</th>
                                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Expiry Date</th>
                                            </tr>
                                        </thead>
                                        <tbody class="bg-white divide-y divide-gray-200">
                                            <% for (BatchSelection selection : selectedBatches) { %>
                                            <tr>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= selection.getBatch().getBatchCode() %></td>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= selection.getReshelfQuantity() %></td>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= selection.getBatch().getExpiryDate() %></td>
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <% } %>
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