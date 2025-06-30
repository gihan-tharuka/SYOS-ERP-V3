<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Admin Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gradient-to-tr from-indigo-50 via-white to-indigo-100 min-h-screen">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-700 text-white shadow-lg">
            <div class="container mx-auto px-6 py-4 flex justify-between items-center">
                <div class="flex items-center space-x-3">
                    <span class="inline-flex items-center justify-center w-10 h-10 bg-white bg-opacity-20 rounded-full">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                        </svg>
                    </span>
                    <h1 class="text-2xl font-extrabold tracking-tight">SYOS ERP <span class="font-light">Admin</span></h1>
                </div>
                <div class="flex items-center space-x-4">
                    <span class="text-indigo-100">Welcome, <%= session.getAttribute("username") %>!</span>
                    <a href="../logout.jsp" class="bg-white text-indigo-700 hover:bg-indigo-50 px-4 py-2 rounded-md font-semibold shadow transition duration-200">
                        Logout
                    </a>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-10">
            <div class="max-w-5xl mx-auto">
                <!-- Quick Stats -->
                <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6 mb-8">
                    <div class="bg-white rounded-xl shadow p-5 flex flex-col items-center hover:shadow-lg transition">
                        <div class="bg-indigo-100 p-3 rounded-full mb-2">
                            <svg class="h-6 w-6 text-indigo-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a4 4 0 00-3-3.87M9 20H4v-2a4 4 0 013-3.87m9-4a4 4 0 10-8 0 4 4 0 008 0z" /></svg>
                        </div>
                        <div class="text-lg font-bold">Users</div>
                        <div class="text-2xl font-extrabold text-indigo-700">-</div>
                    </div>
                    <div class="bg-white rounded-xl shadow p-5 flex flex-col items-center hover:shadow-lg transition">
                        <div class="bg-indigo-100 p-3 rounded-full mb-2">
                            <svg class="h-6 w-6 text-indigo-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V7a2 2 0 00-2-2H6a2 2 0 00-2 2v6m16 0v6a2 2 0 01-2 2H6a2 2 0 01-2-2v-6m16 0H4" /></svg>
                        </div>
                        <div class="text-lg font-bold">Items</div>
                        <div class="text-2xl font-extrabold text-indigo-700">-</div>
                    </div>
                    <div class="bg-white rounded-xl shadow p-5 flex flex-col items-center hover:shadow-lg transition">
                        <div class="bg-indigo-100 p-3 rounded-full mb-2">
                            <svg class="h-6 w-6 text-indigo-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h2l.4 2M7 21h10l4-8H5.4M7 21L5.4 12M7 21l-2.293-2.293c-.63-.63-.184-1.707.707-1.707H17m0 0a2 2 0 100-4 2 2 0 000 4zm-8-2a2 2 0 11-4 0 2 2 0 014 0z" /></svg>
                        </div>
                        <div class="text-lg font-bold">Sales</div>
                        <div class="text-2xl font-extrabold text-indigo-700">-</div>
                    </div>
                    <div class="bg-white rounded-xl shadow p-5 flex flex-col items-center hover:shadow-lg transition">
                        <div class="bg-indigo-100 p-3 rounded-full mb-2">
                            <svg class="h-6 w-6 text-indigo-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg>
                        </div>
                        <div class="text-lg font-bold">Reports</div>
                        <div class="text-2xl font-extrabold text-indigo-700">-</div>
                    </div>
                </div>
                <div class="bg-white rounded-2xl shadow-lg overflow-hidden">
                    <!-- Dashboard Menu -->
                    <div class="p-8">
                        <h2 class="text-2xl font-semibold text-gray-800 mb-8 border-b pb-3">Menu Options</h2>
                        <ul class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <li>
                                <a href="../user" class="block p-6 border border-gray-200 rounded-xl bg-gradient-to-br from-indigo-50 to-white hover:from-indigo-100 hover:to-indigo-50 hover:shadow-xl transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                                        </svg>
                                        <span class="font-medium text-lg text-gray-700">User Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="../item-management" class="block p-6 border border-gray-200 rounded-xl bg-gradient-to-br from-indigo-50 to-white hover:from-indigo-100 hover:to-indigo-50 hover:shadow-xl transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                                        </svg>
                                        <span class="font-medium text-lg text-gray-700">Item Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/mainstock/view" class="block p-6 border border-gray-200 rounded-xl bg-gradient-to-br from-indigo-50 to-white hover:from-indigo-100 hover:to-indigo-50 hover:shadow-xl transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4" />
                                        </svg>
                                        <span class="font-medium text-lg text-gray-700">Main Stock Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/shelfstock/view" class="block p-6 border border-gray-200 rounded-xl bg-gradient-to-br from-indigo-50 to-white hover:from-indigo-100 hover:to-indigo-50 hover:shadow-xl transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4" />
                                        </svg>
                                        <span class="font-medium text-lg text-gray-700">Shelf Stock Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/webstock/view" class="block p-6 border border-gray-200 rounded-xl bg-gradient-to-br from-indigo-50 to-white hover:from-indigo-100 hover:to-indigo-50 hover:shadow-xl transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9" />
                                        </svg>
                                        <span class="font-medium text-lg text-gray-700">Web Stock Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/reorderlevel" class="block p-6 border border-gray-200 rounded-xl bg-gradient-to-br from-indigo-50 to-white hover:from-indigo-100 hover:to-indigo-50 hover:shadow-xl transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                                        </svg>
                                        <span class="font-medium text-lg text-gray-700">Reorder Level Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="../sales" class="block p-6 border border-gray-200 rounded-xl bg-gradient-to-br from-indigo-50 to-white hover:from-indigo-100 hover:to-indigo-50 hover:shadow-xl transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                                        </svg>
                                        <span class="font-medium text-lg text-gray-700">Sales Management</span>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/jsp/report/reportMenu.jsp" class="block p-6 border border-gray-200 rounded-xl bg-gradient-to-br from-indigo-50 to-white hover:from-indigo-100 hover:to-indigo-50 hover:shadow-xl transition duration-200">
                                    <div class="flex items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                        </svg>
                                        <span class="font-medium text-lg text-gray-700">Reports</span>
                                    </div>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </main>

        <!-- Footer -->
        <footer class="bg-white border-t py-5 mt-10 shadow-inner">
            <div class="container mx-auto px-6 text-center text-gray-400 text-sm">
                &copy; 2023 SYOS ERP System. All rights reserved.
            </div>
        </footer>
    </div>
</body>
</html>