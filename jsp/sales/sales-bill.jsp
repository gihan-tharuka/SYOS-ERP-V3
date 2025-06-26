<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Bill" %>
<%@ page import="model.BillItem" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sale Bill</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4 flex justify-between items-center">
                <h1 class="text-2xl font-bold">Sale Bill</h1>
                <a href="sales" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">Back to Sales</a>
            </div>
        </header>
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-2xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6">Bill Details</h2>
                        <% Bill bill = (Bill) request.getAttribute("bill");
                           List<BillItem> billItems = (List<BillItem>) request.getAttribute("billItems");
                           double balance = (request.getAttribute("balance") != null) ? (Double) request.getAttribute("balance") : 0.0;
                        %>
                        <div class="mb-4">
                            <div><span class="font-medium">Bill No:</span> <%= bill.getSerialNumber() %></div>
                            <div><span class="font-medium">Date:</span> <%= bill.getBillDate() %></div>
                            <div><span class="font-medium">Payment Method:</span> <%= bill.getPaymentMethod() %></div>
                        </div>
                        <div class="overflow-x-auto mb-4">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Name</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Price</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <% if (billItems != null) for (BillItem item : billItems) { %>
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= item.getItemName() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= item.getQuantity() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">Rs<%= String.format("%.2f", item.getItemTotalPrice()) %></td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                        <div class="mb-2"><span class="font-medium">Total:</span> Rs<%= String.format("%.2f", bill.getTotalPrice()) %></div>
                        <div class="mb-2"><span class="font-medium">Cash Tendered:</span> Rs<%= String.format("%.2f", bill.getCashTendered()) %></div>
                        <div class="mb-2"><span class="font-medium">Balance:</span> Rs<%= String.format("%.2f", balance) %></div>
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