<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Sale" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sales List</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4 flex justify-between items-center">
                <h1 class="text-2xl font-bold">Sales Management</h1>
                <a href="sales?action=new" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">Create New Sale</a>
            </div>
        </header>
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-5xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6">Sales List</h2>
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Sale ID</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Transaction Type</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <% List<Sale> sales = (List<Sale>) request.getAttribute("sales");
                                       if (sales != null) for (Sale sale : sales) { %>
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= sale.getSaleId() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= sale.getSaleDate() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= sale.getTransactionType() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <a href="#" class="bg-yellow-500 text-white hover:bg-yellow-600 px-3 py-1 rounded-md text-sm font-medium transition duration-200 mr-2">View</a>
                                            <a href="sales?action=delete&id=<%= sale.getSaleId() %>" onclick="return confirm('Are you sure?')" class="bg-red-600 text-white hover:bg-red-700 px-3 py-1 rounded-md text-sm font-medium transition duration-200">Delete</a>
                                        </td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <footer class="bg-gray-100 border-t py-4">
            <div class="container mx-auto px-6 text-center text-gray-500 text-sm">
                &copy; 2023 SYOS ERP System. All rights reserved.
            </div>
        </footer>
    </div>
</body>
</html> 