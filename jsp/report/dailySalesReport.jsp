<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="template.DailySalesReport" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.sql.Date" %>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Daily Sales Report</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Daily Sales Report</h1>
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
                        <!-- Date Selection Form -->
                        <form action="${pageContext.request.contextPath}/reports/daily-sales" method="get" class="mb-6">
                            <div class="flex items-center space-x-4">
                                <label for="date" class="text-sm font-medium text-gray-700">Select Date:</label>
                                <input type="date" id="date" name="date" 
                                       value="${param.date != null ? param.date : report.reportDate}"
                                       class="rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                                <button type="submit" class="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700">
                                    Generate Report
                                </button>
                            </div>
                        </form>

                        <!-- Report Content -->
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Code</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Name</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Price</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <%
                                    DailySalesReport report = (DailySalesReport) request.getAttribute("report");
                                    Map<String, DailySalesReport.ItemAggregate> itemAggregates = report.getItemAggregates();
                                    for (DailySalesReport.ItemAggregate aggregate : itemAggregates.values()) {
                                    %>
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= aggregate.getItemCode() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= aggregate.getItemName() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= aggregate.getQuantity() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">$<%= String.format("%.2f", aggregate.getTotalPrice()) %></td>
                                    </tr>
                                    <% } %>
                                </tbody>
                                <tfoot class="bg-gray-50">
                                    <tr>
                                        <td colspan="2" class="px-6 py-4 text-right text-sm font-medium text-gray-900">Total Quantity Sold:</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"><%= report.getTotalQuantitySold() %></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" class="px-6 py-4 text-right text-sm font-medium text-gray-900">Total Revenue:</td>
                                        <td colspan="2" class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">$<%= String.format("%.2f", report.getTotalRevenue()) %></td>
                                    </tr>
                                </tfoot>
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