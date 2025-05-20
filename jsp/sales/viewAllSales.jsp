<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Sales</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">All Sales</h1>
                    <div class="flex items-center space-x-4">
                        <a href="${pageContext.request.contextPath}/sales" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                            Back to Sales Menu
                        </a>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-6xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <div class="flex justify-between items-center mb-6">
                            <h2 class="text-xl font-semibold text-gray-800">Sales Records</h2>
                            <a href="${pageContext.request.contextPath}/sales/create" 
                               class="bg-indigo-600 text-white hover:bg-indigo-700 px-4 py-2 rounded-md font-medium transition duration-200">
                                Create New Sale
                            </a>
                        </div>
                        
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Sale ID</th>
                                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Transaction Type</th>
                                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <c:forEach items="${sales}" var="sale">
                                        <tr class="hover:bg-gray-50">
                                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                ${sale.saleId}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                ${sale.saleDate}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                                    ${sale.transactionType}
                                                </span>
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                <a href="${pageContext.request.contextPath}/sales/viewBill?billId=${sale.saleId}" 
                                                   class="text-indigo-600 hover:text-indigo-900 font-medium">
                                                    View Bill
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
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