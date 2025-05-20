<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Admin Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">SYOS ERP - Admin Dashboard</h1>
                    <div class="flex items-center space-x-4">
                        <span class="text-indigo-100">Welcome, <%= session.getAttribute("username") %>!</span>
                        <a href="../logout.jsp" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                            Logout
                        </a>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-4xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <!-- Dashboard Menu -->
                    <div class="p-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6 border-b pb-2">Menu Options</h2>
                        <ul class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <li>
                                <a href="user-management.jsp" class="block p-4 border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-200 transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-indigo-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                                        </svg>
                                        <span class="font-medium text-gray-700">User Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="../item-management" class="block p-4 border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-200 transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-indigo-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                                        </svg>
                                        <span class="font-medium text-gray-700">Item Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/mainstock/view" class="block p-4 border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-200 transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-indigo-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4" />
                                        </svg>
                                        <span class="font-medium text-gray-700">Main Stock Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/shelfstock/view" class="block p-4 border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-200 transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-indigo-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4" />
                                        </svg>
                                        <span class="font-medium text-gray-700">Shelf Stock Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/sales" class="block p-4 border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-200 transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-indigo-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                                        </svg>
                                        <span class="font-medium text-gray-700">Sales Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="reports.jsp" class="block p-4 border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-200 transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-indigo-500 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                        </svg>
                                        <span class="font-medium text-gray-700">Reports</span>
                                    </div>
                                </a>
                            </li>
                        </ul>
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