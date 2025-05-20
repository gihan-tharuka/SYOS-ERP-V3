<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sales Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Sales Management</h1>
                    <div class="flex items-center space-x-4">
                        <a href="${pageContext.request.contextPath}/admin/dashboard.jsp" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                            Back to Dashboard
                        </a>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-4xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6 border-b pb-2">Sales Operations</h2>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <a href="${pageContext.request.contextPath}/sales/viewAll" 
                               class="block p-6 border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-200 transition duration-200">
                                <div class="flex items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                                    </svg>
                                    <div>
                                        <h3 class="text-lg font-medium text-gray-900">View All Sales</h3>
                                        <p class="mt-1 text-sm text-gray-500">View and manage all sales records</p>
                                    </div>
                                </div>
                            </a>

                            <a href="${pageContext.request.contextPath}/sales/create" 
                               class="block p-6 border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-200 transition duration-200">
                                <div class="flex items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-indigo-500 mr-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                                    </svg>
                                    <div>
                                        <h3 class="text-lg font-medium text-gray-900">Create New Sale</h3>
                                        <p class="mt-1 text-sm text-gray-500">Process a new sales transaction</p>
                                    </div>
                                </div>
                            </a>
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