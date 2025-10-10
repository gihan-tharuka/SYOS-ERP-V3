<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>My Account - SYOS ERP</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4 flex justify-between items-center">
                <h1 class="text-2xl font-bold">My Account</h1>
                <a href="${pageContext.request.contextPath}/customer/dashboard.jsp" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">Back to Dashboard</a>
            </div>
        </header>
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-lg mx-auto bg-white rounded-lg shadow-md p-8">
                <h2 class="text-xl font-semibold text-gray-800 mb-6 border-b pb-2">Edit Account Information</h2>
                <form action="${pageContext.request.contextPath}/CustomerAccountServlet" method="post" class="space-y-6">
                    <div>
                        <label class="block text-gray-700 font-medium mb-2">Username</label>
                        <input type="text" name="username" value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400" required />
                    </div>
                    <div>
                        <label class="block text-gray-700 font-medium mb-2">Password</label>
                        <input type="password" name="password" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400" required />
                    </div>
                    <div>
                        <label class="block text-gray-700 font-medium mb-2">Email</label>
                        <input type="email" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400" required />
                    </div>
                    <div>
                        <label class="block text-gray-700 font-medium mb-2">Mobile</label>
                        <input type="text" name="mobile" value="<%= request.getAttribute("mobile") != null ? request.getAttribute("mobile") : "" %>" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400" required />
                    </div>
                    <button type="submit" class="w-full bg-indigo-600 text-white py-2 rounded-md font-medium hover:bg-indigo-700 transition duration-200">Update Account</button>
                </form>
                <% if (request.getAttribute("message") != null) { %>
                    <div class="mt-4 text-green-600"><%= request.getAttribute("message") %></div>
                <% } %>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="mt-4 text-red-600"><%= request.getAttribute("error") %></div>
                <% } %>
            </div>
        </main>
    </div>
</body>
</html> 