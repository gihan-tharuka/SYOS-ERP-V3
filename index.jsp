<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome Page</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen flex flex-col items-center justify-center p-4">
    <div class="max-w-md w-full bg-white rounded-xl shadow-lg p-8 text-center">
        <h1 class="text-3xl font-bold text-gray-800 mb-6">Welcome to the System!</h1>
        <p class="text-gray-600 mb-8">Please login to access your account and manage the system.</p>

        <form action="login.jsp" method="get" class="w-full">
            <button type="submit" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 px-4 rounded-lg transition duration-200 transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-opacity-50">
                Go to Login
            </button>
        </form>

        <div class="mt-6 text-sm text-gray-500">
            New to the system? <a href="#" class="text-indigo-600 hover:text-indigo-800">Contact admin</a> for access.
        </div>
    </div>
</body>
</html>