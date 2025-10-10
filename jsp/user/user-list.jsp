<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4 flex justify-between items-center">
                <h1 class="text-2xl font-bold">User Management</h1>
                <a href="user?action=new" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">Create New User</a>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-5xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6">User List</h2>
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Username</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Role</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <% List<User> userList = (List<User>) request.getAttribute("userList");
                                       for (User user : userList) { %>
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= user.getUsername() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <% if (user instanceof model.Cashier) { %>
                                                <%= ((model.Cashier)user).getFullName() %>
                                            <% } else if (user instanceof model.Supplier) { %>
                                                <%= ((model.Supplier)user).getCompanyName() %>
                                            <% } else { %>
                                                <%= user.getEmail() %>
                                            <% } %>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= user.getEmail() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <% if (user instanceof model.Admin) { %>
                                                Admin
                                            <% } else if (user instanceof model.Cashier) { %>
                                                Cashier
                                            <% } else if (user instanceof model.Supplier) { %>
                                                Supplier
                                            <% } else if (user instanceof model.Customer) { %>
                                                Customer
                                            <% } else { %>
                                                Unknown
                                            <% } %>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            <a href="user?action=edit&username=<%= user.getUsername() %>" class="bg-yellow-500 text-white hover:bg-yellow-600 px-3 py-1 rounded-md text-sm font-medium transition duration-200 mr-2">Edit</a>
                                            <a href="user?action=delete&username=<%= user.getUsername() %>" onclick="return confirm('Are you sure?')" class="bg-red-600 text-white hover:bg-red-700 px-3 py-1 rounded-md text-sm font-medium transition duration-200">Delete</a>
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

        <!-- Footer -->
        <footer class="bg-gray-100 border-t py-4">
            <div class="container mx-auto px-6 text-center text-gray-500 text-sm">
                &copy; 2023 SYOS ERP System. All rights reserved.
            </div>
        </footer>
    </div>
</body>
</html> 