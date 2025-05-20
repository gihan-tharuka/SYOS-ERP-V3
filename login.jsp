<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .bg-login {
            background: linear-gradient(135deg, rgba(79,70,229,0.1) 0%, rgba(255,255,255,1) 100%);
        }
    </style>
</head>
<body class="bg-login min-h-screen flex items-center justify-center p-4">
    <div class="w-full max-w-md">
        <div class="text-center mb-8">
            <h1 class="text-3xl font-bold text-indigo-600 mb-2">SYOS ERP System</h1>
            <p class="text-gray-600">Please sign in to access your account</p>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="bg-red-50 border-l-4 border-red-500 p-4 mb-6 rounded">
                <div class="flex">
                    <div class="flex-shrink-0">
                        <svg class="h-5 w-5 text-red-500" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
                        </svg>
                    </div>
                    <div class="ml-3">
                        <p class="text-sm text-red-700"><%= request.getAttribute("error") %></p>
                    </div>
                </div>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post" class="bg-white shadow-lg rounded-lg px-8 pt-8 pb-8 mb-4">
            <div class="mb-6">
                <label for="role" class="block text-gray-700 text-sm font-medium mb-2">Role</label>
                <select id="role" name="role" required class="block w-full px-3 py-3 text-gray-700 bg-gray-50 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200">
                    <option value="" disabled selected>Select Role</option>
                    <option value="admin">Admin</option>
                    <option value="cashier">Cashier</option>
                </select>
            </div>

            <div class="mb-6">
                <label for="username" class="block text-gray-700 text-sm font-medium mb-2">Username</label>
                <input type="text" id="username" name="username" required class="block w-full px-3 py-3 text-gray-700 bg-gray-50 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200" placeholder="Enter your username">
            </div>

            <div class="mb-8">
                <label for="password" class="block text-gray-700 text-sm font-medium mb-2">Password</label>
                <input type="password" id="password" name="password" required class="block w-full px-3 py-3 text-gray-700 bg-gray-50 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200" placeholder="Enter your password">
            </div>

            <div class="flex items-center justify-between">
                <button type="submit" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 px-4 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition duration-200 transform hover:scale-[1.02]">
                    Login
                </button>
            </div>
        </form>

        <div class="text-center text-sm text-gray-500">
            © 2023 SYOS ERP System. All rights reserved.
        </div>
    </div>
</body>
</html>