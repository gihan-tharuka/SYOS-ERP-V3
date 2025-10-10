<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Web Store</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        // Store initial quantities for comparison
        let initialQuantities = {};
        let pollInterval = 3000;
        let pollTimeout = null;
        let isPolling = true;
        
        // Function to update quantities on the page
        function updateQuantities(newData) {
    Object.entries(newData).forEach(([itemId, data]) => {
        const itemElement = document.querySelector(`[data-item-id="${itemId}"]`);
        if (!itemElement) {
            console.error(`Item element not found for ID ${itemId}`);
            return;
        }

        // Update available quantity display
        const quantityDisplay = itemElement.querySelector('.quantity-display');
        if (quantityDisplay) {
            quantityDisplay.textContent = data.currentQuantity;
        }

        // Update quantity input
        const quantityInput = itemElement.querySelector('input[name="quantity"]');
        if (quantityInput) {
            quantityInput.max = data.currentQuantity;
            if (parseInt(quantityInput.value) > data.currentQuantity) {
                quantityInput.value = data.currentQuantity;
            }
        }

        // Update add button
        const addButton = itemElement.querySelector('button[type="submit"]');
        if (addButton) {
            const isOutOfStock = data.currentQuantity === 0;
            addButton.disabled = isOutOfStock;
            addButton.classList.toggle('opacity-50', isOutOfStock);
            addButton.classList.toggle('cursor-not-allowed', isOutOfStock);
            addButton.textContent = isOutOfStock ? 'Out of Stock' : 'Add to Cart';
        }
    });
}

function pollForUpdates() {
    if (!isPolling) return;

    fetch('${pageContext.request.contextPath}/api/stock-updates')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Received stock updates:', data);
            updateQuantities(data);
        })
        .catch(error => {
            console.error('Error fetching updates:', error);
        })
        .finally(() => {
            pollTimeout = setTimeout(pollForUpdates, pollInterval);
        });
}

        // Start polling when page loads
        document.addEventListener('DOMContentLoaded', function() {
            // Store initial quantities
            document.querySelectorAll('[data-item-id]').forEach(item => {
                const itemId = item.getAttribute('data-item-id');
                const quantity = item.querySelector('.quantity-display').textContent;
                initialQuantities[itemId] = quantity;
            });

            // Start polling immediately
            pollForUpdates();
        });

        // Clean up when page is unloaded
        window.addEventListener('beforeunload', function() {
            isPolling = false;
            if (pollTimeout) {
                clearTimeout(pollTimeout);
            }
        });
    </script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Web Store</h1>
                    <div class="flex items-center space-x-4">
                        <a href="${pageContext.request.contextPath}/jsp/customer/dashboard.jsp" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                            Dashboard
                        </a>
                        <a href="${pageContext.request.contextPath}/cart" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">
                            View Cart
                        </a>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-6 py-8 bg-gray-50">
            <c:if test="${not empty error}">
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                    <span class="block sm:inline">${error}</span>
                </div>
            </c:if>

            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3 gap-8">
                <c:forEach items="${webStocks}" var="entry">
                    <div class="bg-white rounded-xl shadow-md border border-gray-200 overflow-hidden flex flex-col h-full w-80 mx-auto" data-item-id="${entry.key.itemId}">
                        <div class="flex flex-col items-center p-6 flex-1">
                            <div class="w-full flex justify-center mb-4">
                                <c:choose>
                                    <c:when test="${not empty webStockItems[entry.key].imagePath}">
                                        <img src="${pageContext.request.contextPath}/${webStockItems[entry.key].imagePath}" alt="${webStockItems[entry.key].itemName}" class="h-40 w-40 object-cover rounded border bg-gray-100" />
                                    </c:when>
                                    <c:otherwise>
                                        <div class="h-40 w-40 flex items-center justify-center bg-gray-100 rounded border">
                                            <span class="text-gray-400 text-4xl">&#128230;</span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <h5 class="text-xl font-bold text-gray-800 mb-2 text-center">${webStockItems[entry.key].itemName}</h5>
                            <div class="space-y-1 text-gray-600 mb-4 text-center">
                                <p><span class="font-semibold">Available:</span> <span class="quantity-display">${entry.key.currentQuantity}</span></p>
                                <p><span class="font-semibold">Item Code:</span> ${entry.value}</p>
                                <p class="text-indigo-600 font-bold text-lg">Rs. ${webStockItems[entry.key].price}</p>
                            </div>
                            <form action="${pageContext.request.contextPath}/webstore" method="post" class="w-full mt-auto">
                                <input type="hidden" name="action" value="addToCart">
                                <input type="hidden" name="itemId" value="${entry.key.itemId}">
                                <div class="flex items-center gap-2 mb-2 justify-center">
                                    <label for="quantity" class="block text-sm font-medium text-gray-700 mb-0 sr-only">Quantity:</label>
                                    <input type="number" class="w-16 px-2 py-1 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm" 
                                           id="quantity" name="quantity" min="1" max="${entry.key.currentQuantity}" value="1" required>
                                    <c:set var="searchId" value=",${entry.key.itemId}," />
                                    <button type="submit"
                                        class="px-3 py-1 rounded-md font-bold transition duration-200 text-sm
                                        <c:choose>
                                            <c:when test="${entry.key.currentQuantity == 0}">bg-gray-400 text-white opacity-50 cursor-not-allowed</c:when>
                                            <c:when test="${fn:contains(cartItemIdsStr, searchId)}">bg-green-600 text-white hover:bg-green-700</c:when>
                                            <c:otherwise>bg-indigo-600 text-white hover:bg-indigo-700 shadow-md</c:otherwise>
                                        </c:choose>"
                                        <c:if test="${entry.key.currentQuantity == 0 || fn:contains(cartItemIdsStr, searchId)}">disabled</c:if>>
                                        <c:choose>
                                            <c:when test="${entry.key.currentQuantity == 0}">Out of Stock</c:when>
                                            <c:when test="${fn:contains(cartItemIdsStr, searchId)}">Added to Cart</c:when>
                                            <c:otherwise>Add to Cart</c:otherwise>
                                        </c:choose>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:forEach>
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