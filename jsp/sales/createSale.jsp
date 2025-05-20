<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create New Sale</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">Create New Sale</h1>
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
            <div class="max-w-4xl mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <form action="${pageContext.request.contextPath}/sales/create" method="post" class="space-y-6" onsubmit="return validateForm()">
                            <!-- Error Message -->
                            <c:if test="${not empty error}">
                                <div class="bg-red-50 border-l-4 border-red-400 p-4 mb-4">
                                    <div class="flex">
                                        <div class="flex-shrink-0">
                                            <svg class="h-5 w-5 text-red-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                                                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
                                            </svg>
                                        </div>
                                        <div class="ml-3">
                                            <p class="text-sm text-red-700">${error}</p>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <!-- Transaction Type -->
                            <div>
                                <label for="transactionType" class="block text-sm font-medium text-gray-700">Transaction Type</label>
                                <select name="transactionType" id="transactionType" required
                                        class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                                    <option value="over-the-counter">Over the Counter</option>
                                    <option value="online">Online</option>
                                </select>
                            </div>

                            <!-- Item Entry Section -->
                            <div class="border-t border-gray-200 pt-6">
                                <h3 class="text-lg font-medium text-gray-900 mb-4">Add Items</h3>
                                
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                                    <div>
                                        <label for="itemCode" class="block text-sm font-medium text-gray-700">Item Code</label>
                                        <input type="text" id="itemCode" name="itemCode"
                                               class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                                    </div>
                                    <div>
                                        <label for="quantity" class="block text-sm font-medium text-gray-700">Quantity</label>
                                        <input type="number" id="quantity" name="quantity" min="1"
                                               class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                                    </div>
                                </div>
                                
                                <button type="button" onclick="addItem()"
                                        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                    Add Item
                                </button>
                            </div>

                            <!-- Items List -->
                            <div id="itemsList" class="space-y-4">
                                <!-- Dynamically added items will appear here -->
                            </div>

                            <!-- Sale Summary -->
                            <div id="saleSummary" class="mt-6 border-t border-gray-200 pt-6 hidden">
                                <h3 class="text-lg font-medium text-gray-900 mb-4">Sale Summary</h3>
                                <div class="bg-gray-50 rounded-lg p-4">
                                    <div class="grid grid-cols-2 gap-4">
                                        <div>
                                            <p class="text-sm font-medium text-gray-500">Total Items</p>
                                            <p id="totalItems" class="text-lg font-semibold text-gray-900">0</p>
                                        </div>
                                        <div>
                                            <p class="text-sm font-medium text-gray-500">Total Amount</p>
                                            <p id="totalAmount" class="text-lg font-semibold text-gray-900">$0.00</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Payment Section -->
                            <div id="paymentSection" class="mt-6 border-t border-gray-200 pt-6 hidden">
                                <h3 class="text-lg font-medium text-gray-900 mb-4">Payment Details</h3>
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <div>
                                        <label for="cashTendered" class="block text-sm font-medium text-gray-700">Cash Tendered</label>
                                        <input type="number" id="cashTendered" name="cashTendered" min="0" step="0.01"
                                               class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                                    </div>
                                    <div>
                                        <label class="block text-sm font-medium text-gray-700">Change Amount</label>
                                        <p id="changeAmount" class="mt-1 text-lg font-semibold text-gray-900">$0.00</p>
                                    </div>
                                </div>
                            </div>

                            <!-- Hidden fields for form submission -->
                            <div id="hiddenFields"></div>

                            <!-- Submit Button -->
                            <div class="flex justify-end mt-6">
                                <button type="submit" id="createSaleBtn"
                                        class="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                    Create Sale
                                </button>
                            </div>
                        </form>
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

    <script>
        let itemCount = 0;
        const items = [];
        let totalAmount = 0;

        function addItem() {
            const itemCode = document.getElementById('itemCode').value.trim();
            const quantity = document.getElementById('quantity').value.trim();
            
            console.log('Adding item:', { itemCode, quantity }); // Debug log
            
            if (!itemCode || !quantity) {
                alert('Please enter both item code and quantity.');
                return;
            }

            if (isNaN(quantity) || parseInt(quantity) <= 0) {
                alert('Please enter a valid quantity greater than 0.');
                return;
            }

            // For now, let's add the item directly without fetching price
            const item = {
                itemCode,
                quantity: parseInt(quantity),
                price: 10.00, // Temporary fixed price for testing
                total: 10.00 * parseInt(quantity)
            };
            
            items.push(item);
            updateSaleSummary();
            renderItems();
            updateHiddenFields();
            
            // Show payment section if items are added
            document.getElementById('paymentSection').classList.remove('hidden');
            
            // Clear input fields
            document.getElementById('itemCode').value = '';
            document.getElementById('quantity').value = '';
            
            console.log('Item added successfully:', item); // Debug log
            console.log('Current items:', items); // Debug log
        }

        function removeItem(index) {
            console.log('Removing item at index:', index); // Debug log
            items.splice(index, 1);
            updateSaleSummary();
            renderItems();
            updateHiddenFields();
            
            // Hide payment section if no items
            if (items.length === 0) {
                document.getElementById('paymentSection').classList.add('hidden');
            }
            
            console.log('Items after removal:', items); // Debug log
        }

        function renderItems() {
            console.log('Rendering items:', items); // Debug log
            const itemsList = document.getElementById('itemsList');
            itemsList.innerHTML = '';
            
            items.forEach((item, index) => {
                const itemDiv = document.createElement('div');
                itemDiv.className = 'flex items-center justify-between p-4 bg-gray-50 rounded-lg mb-4';
                itemDiv.innerHTML = `
                    <div class="flex-1">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-sm font-medium text-gray-900">Item Code: <span class="font-bold">${item.itemCode}</span></p>
                                <p class="text-sm text-gray-500">Quantity: <span class="font-bold">${item.quantity}</span></p>
                                <p class="text-sm text-gray-500">Price: <span class="font-bold">$${item.price.toFixed(2)}</span></p>
                                <p class="text-sm text-gray-500">Total: <span class="font-bold">$${item.total.toFixed(2)}</span></p>
                            </div>
                            <button type="button" onclick="removeItem(${index})" 
                                    class="inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-red-700 bg-red-100 hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
                                Remove
                            </button>
                        </div>
                    </div>
                `;
                itemsList.appendChild(itemDiv);
            });
        }

        function updateSaleSummary() {
            console.log('Updating sale summary'); // Debug log
            totalAmount = items.reduce((sum, item) => sum + item.total, 0);
            document.getElementById('totalItems').textContent = items.length;
            document.getElementById('totalAmount').textContent = `$${totalAmount.toFixed(2)}`;
            document.getElementById('saleSummary').classList.remove('hidden');
            console.log('Sale summary updated:', { totalItems: items.length, totalAmount }); // Debug log
        }

        function updateHiddenFields() {
            console.log('Updating hidden fields'); // Debug log
            const hiddenFields = document.getElementById('hiddenFields');
            hiddenFields.innerHTML = '';
            
            items.forEach(item => {
                const itemCodeInput = document.createElement('input');
                itemCodeInput.type = 'hidden';
                itemCodeInput.name = 'itemCodes';
                itemCodeInput.value = item.itemCode;
                hiddenFields.appendChild(itemCodeInput);
                
                const quantityInput = document.createElement('input');
                quantityInput.type = 'hidden';
                quantityInput.name = 'quantities';
                quantityInput.value = item.quantity;
                hiddenFields.appendChild(quantityInput);
            });
            console.log('Hidden fields updated'); // Debug log
        }

        // Add event listener for cash tendered input
        document.getElementById('cashTendered').addEventListener('input', function(e) {
            const cashTendered = parseFloat(e.target.value) || 0;
            const change = cashTendered - totalAmount;
            document.getElementById('changeAmount').textContent = `$${Math.max(0, change).toFixed(2)}`;
        });

        function validateForm() {
            if (items.length === 0) {
                alert('Please add at least one item to the sale.');
                return false;
            }

            const cashTendered = parseFloat(document.getElementById('cashTendered').value) || 0;
            if (cashTendered < totalAmount) {
                alert('Cash tendered must be greater than or equal to the total amount.');
                return false;
            }
            
            // Log the form data before submission
            const form = document.querySelector('form');
            const formData = new FormData(form);
            console.log('Form data before submission:');
            for (let [key, value] of formData.entries()) {
                console.log(key + ': ' + value);
            }
            
            return true;
        }
    </script>
</body>
</html> 