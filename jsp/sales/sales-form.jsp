<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.BillItem" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>New Sale</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4 flex justify-between items-center">
                <h1 class="text-2xl font-bold">New Sale</h1>
                <a href="sales" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">Back to Sales</a>
            </div>
        </header>
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-3xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6">Add Items</h2>
                        <form action="sales" method="post" class="mb-6 flex flex-col md:flex-row md:items-end gap-4">
                            <input type="hidden" name="action" value="addItem" />
                            <div>
                                <label class="block text-sm font-medium text-gray-700">Item Code</label>
                                <input type="text" name="itemCode" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-medium text-gray-700">Quantity</label>
                                <input type="number" name="quantity" min="1" value="1" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500" />
                            </div>
                            <button type="submit" class="bg-indigo-600 text-white hover:bg-indigo-700 px-4 py-2 rounded-md font-medium transition duration-200">Add Item</button>
                        </form>
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200 mb-6">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Name</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Price</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <% List<BillItem> cart = (List<BillItem>) request.getAttribute("cart");
                                       if (cart != null) for (int i = 0; i < cart.size(); i++) {
                                           BillItem item = cart.get(i); %>
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= item.getItemName() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= item.getQuantity() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">$<%= String.format("%.2f", item.getItemTotalPrice()) %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <form action="sales" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="removeItem" />
                                                <input type="hidden" name="index" value="<%= i %>" />
                                                <button type="submit" class="bg-red-600 text-white hover:bg-red-700 px-3 py-1 rounded-md text-sm font-medium transition duration-200">Remove</button>
                                            </form>
                                        </td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                        <form action="sales" method="post">
                            <input type="hidden" name="action" value="checkout" />
                            <button type="submit" class="bg-green-600 text-white hover:bg-green-700 px-4 py-2 rounded-md font-medium transition duration-200">Proceed to Checkout</button>
                        </form>
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