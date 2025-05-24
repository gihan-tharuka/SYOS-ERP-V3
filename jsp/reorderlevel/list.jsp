<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Reorder Level Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const eventSource = new EventSource('${pageContext.request.contextPath}/reorderlevel/events');
            
            eventSource.addEventListener('connected', function(e) {
                console.log('SSE Connected. Active clients:', e.data);
            });
            
            eventSource.addEventListener('add', function(e) {
                const reorderLevel = JSON.parse(e.data);
                addReorderLevelToTable(reorderLevel);
            });
            
            eventSource.addEventListener('update', function(e) {
                const reorderLevel = JSON.parse(e.data);
                updateReorderLevelInTable(reorderLevel);
            });
            
            eventSource.addEventListener('delete', function(e) {
                const reorderLevel = JSON.parse(e.data);
                removeReorderLevelFromTable(reorderLevel.reorderLevelId);
            });
            
            eventSource.addEventListener('error', function(e) {
                console.error('SSE Error:', e);
                eventSource.close();
                // Attempt to reconnect after 5 seconds
                setTimeout(() => {
                    window.location.reload();
                }, 5000);
            });
        });
        
        function addReorderLevelToTable(reorderLevel) {
            const tbody = document.querySelector('tbody');
            const tr = document.createElement('tr');
            tr.id = `reorder-level-${reorderLevel.reorderLevelId}`;
            tr.innerHTML = `
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${reorderLevel.itemCode}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${reorderLevel.itemName}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${reorderLevel.thresholdQuantity}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${reorderLevel.totalStock}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${reorderLevel.totalStock <= reorderLevel.thresholdQuantity ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'}">
                        ${reorderLevel.totalStock <= reorderLevel.thresholdQuantity ? 'Low Stock' : 'In Stock'}
                    </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <a href="${pageContext.request.contextPath}/reorderlevel/edit?id=${reorderLevel.reorderLevelId}" 
                       class="text-indigo-600 hover:text-indigo-900 mr-3">Edit</a>
                    <form action="${pageContext.request.contextPath}/reorderlevel" method="post" class="inline">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="reorderLevelId" value="${reorderLevel.reorderLevelId}">
                        <button type="submit" class="text-red-600 hover:text-red-900"
                                onclick="return confirm('Are you sure you want to delete this reorder level?')">
                            Delete
                        </button>
                    </form>
                </td>
            `;
            tbody.appendChild(tr);
        }
        
        function updateReorderLevelInTable(reorderLevel) {
            const tr = document.getElementById(`reorder-level-${reorderLevel.reorderLevelId}`);
            if (tr) {
                tr.innerHTML = `
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${reorderLevel.itemCode}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${reorderLevel.itemName}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${reorderLevel.thresholdQuantity}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${reorderLevel.totalStock}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm">
                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${reorderLevel.totalStock <= reorderLevel.thresholdQuantity ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'}">
                            ${reorderLevel.totalStock <= reorderLevel.thresholdQuantity ? 'Low Stock' : 'In Stock'}
                        </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <a href="${pageContext.request.contextPath}/reorderlevel/edit?id=${reorderLevel.reorderLevelId}" 
                           class="text-indigo-600 hover:text-indigo-900 mr-3">Edit</a>
                        <form action="${pageContext.request.contextPath}/reorderlevel" method="post" class="inline">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="reorderLevelId" value="${reorderLevel.reorderLevelId}">
                            <button type="submit" class="text-red-600 hover:text-red-900"
                                    onclick="return confirm('Are you sure you want to delete this reorder level?')">
                                Delete
                            </button>
                        </form>
                    </td>
                `;
            }
        }
        
        function removeReorderLevelFromTable(reorderLevelId) {
            const tr = document.getElementById(`reorder-level-${reorderLevelId}`);
            if (tr) {
                tr.remove();
            }
        }
    </script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">SYOS ERP - Reorder Level Management</h1>
                    <a href="../admin/dashboard.jsp" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                        Back to Dashboard
                    </a>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-6xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <div class="flex justify-between items-center mb-6">
                            <h2 class="text-xl font-semibold text-gray-800">Reorder Levels</h2>
                            <a href="${pageContext.request.contextPath}/reorderlevel/add" class="bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-4 rounded transition duration-200">
                                Add New Reorder Level
                            </a>
                        </div>
                        
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Code</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Item Name</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Threshold Quantity</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Stock</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <c:forEach var="level" items="${reorderLevels}">
                                        <tr>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${level.itemCode}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${level.itemName}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${level.thresholdQuantity}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-800">${level.totalStock}</td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm">
                                                <c:choose>
                                                    <c:when test="${level.totalStock <= level.thresholdQuantity}">
                                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                                                            Low Stock
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                                            In Stock
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                                <a href="${pageContext.request.contextPath}/reorderlevel/edit?id=${level.reorderLevelId}" 
                                                   class="text-indigo-600 hover:text-indigo-900 mr-3">Edit</a>
                                                <form action="${pageContext.request.contextPath}/reorderlevel" method="post" class="inline">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="reorderLevelId" value="${level.reorderLevelId}">
                                                    <button type="submit" class="text-red-600 hover:text-red-900"
                                                            onclick="return confirm('Are you sure you want to delete this reorder level?')">
                                                        Delete
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
                <div class="mt-6 text-center">
                    <a href="../admin/dashboard.jsp" class="text-gray-600 hover:text-gray-800 font-medium">
                        ← Back to Dashboard
                    </a>
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