<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create New Sale</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <h1>Create New Sale</h1>
        <form action="${pageContext.request.contextPath}/sales/create" method="post" class="sale-form">
            <div class="form-group">
                <label for="transactionType">Transaction Type:</label>
                <select name="transactionType" id="transactionType" required>
                    <option value="over-the-counter">Over the Counter</option>
                    <option value="online">Online</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="itemCode">Item Code:</label>
                <input type="text" id="itemCode" name="itemCode" required>
            </div>
            
            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="number" id="quantity" name="quantity" min="1" required>
            </div>
            
            <div id="itemsList">
                <!-- Dynamically added items will appear here -->
            </div>
            
            <button type="button" onclick="addItem()" class="btn">Add Item</button>
            <button type="submit" class="btn">Create Sale</button>
        </form>
        
        <div class="actions">
            <a href="${pageContext.request.contextPath}/sales" class="btn">Back to Menu</a>
        </div>
    </div>
    
    <script>
        function addItem() {
            const itemCode = document.getElementById('itemCode').value;
            const quantity = document.getElementById('quantity').value;
            
            if (itemCode && quantity) {
                const itemsList = document.getElementById('itemsList');
                const itemDiv = document.createElement('div');
                itemDiv.className = 'item-entry';
                itemDiv.innerHTML = `
                    <input type="hidden" name="items[${itemsList.children.length}].itemCode" value="${itemCode}">
                    <input type="hidden" name="items[${itemsList.children.length}].quantity" value="${quantity}">
                    <p>Item: ${itemCode}, Quantity: ${quantity}</p>
                    <button type="button" onclick="this.parentElement.remove()" class="btn-small">Remove</button>
                `;
                itemsList.appendChild(itemDiv);
                
                // Clear input fields
                document.getElementById('itemCode').value = '';
                document.getElementById('quantity').value = '';
            }
        }
    </script>
</body>
</html> 