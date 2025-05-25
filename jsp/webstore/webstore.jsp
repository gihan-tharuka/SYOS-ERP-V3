<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Web Store</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        // Store initial quantities for comparison
        let initialQuantities = {};
        let pollInterval = 3000; // Poll every 500ms for instant updates
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
        <main class="flex-grow container mx-auto px-6 py-8">
            <c:if test="${not empty error}">
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                    <span class="block sm:inline">${error}</span>
                </div>
            </c:if>

            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <c:forEach items="${webStocks}" var="entry">
                    <div class="bg-white rounded-lg shadow-md overflow-hidden" data-item-id="${entry.key.itemId}">
                        <div class="p-6">
                            <h5 class="text-xl font-semibold text-gray-800 mb-4">${webStockItems[entry.key].itemName}</h5>
                            <div class="space-y-2 text-gray-600 mb-4">
                                <p>Available Quantity: <span class="quantity-display">${entry.key.currentQuantity}</span></p>
                                <p>Item Code: ${entry.value}</p>
                                <p class="text-indigo-600 font-semibold">Rs. ${webStockItems[entry.key].price}</p>
                            </div>
                            <form action="${pageContext.request.contextPath}/webstore" method="post">
                                <input type="hidden" name="action" value="addToCart">
                                <input type="hidden" name="itemId" value="${entry.key.itemId}">
                                <div class="mb-4">
                                    <label for="quantity" class="block text-sm font-medium text-gray-700 mb-1">Quantity:</label>
                                    <input type="number" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500" 
                                           id="quantity" name="quantity" min="1" max="${entry.key.currentQuantity}" value="1" required>
                                </div>
                                <button type="submit" class="w-full bg-indigo-600 text-white hover:bg-indigo-700 px-4 py-2 rounded-md font-medium transition duration-200"
                                        ${entry.key.currentQuantity == 0 ? 'disabled' : ''}>
                                    ${entry.key.currentQuantity == 0 ? 'Out of Stock' : 'Add to Cart'}
                                </button>
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