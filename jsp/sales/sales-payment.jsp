<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sale Payment</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <div class="min-h-screen flex flex-col">
        <header class="bg-indigo-600 text-white shadow-md">
            <div class="container mx-auto px-6 py-4 flex justify-between items-center">
                <h1 class="text-2xl font-bold">Sale Payment</h1>
                <a href="sales?action=new" class="bg-white text-indigo-600 hover:bg-indigo-50 px-4 py-2 rounded-md font-medium transition duration-200">Back to Cart</a>
            </div>
        </header>
        <main class="flex-grow container mx-auto px-6 py-8">
            <div class="max-w-md mx-auto">
                <div class="bg-white rounded-lg shadow-md overflow-hidden">
                    <div class="p-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6">Payment</h2>
                        <form action="sales" method="post">
                            <input type="hidden" name="action" value="complete" />
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700">Total</label>
                                <input type="text" name="total" value="$<%= String.format("%.2f", request.getAttribute("total")) %>" readonly class="mt-1 block w-full border-gray-300 rounded-md shadow-sm bg-gray-100" />
                            </div>
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700">Cash Tendered</label>
                                <input type="number" name="cashTendered" min="0" step="0.01" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500" />
                            </div>
                            <button type="submit" class="bg-green-600 text-white hover:bg-green-700 px-4 py-2 rounded-md font-medium transition duration-200">Complete Sale</button>
                        </form>
                    </div>
                </div>
            </div>
        </main>
        <footer class="bg-gray-100 border-t py-4">
            <div class="container mx-auto px-6 text-center text-gray-500 text-sm">
                &copy; 2023 SYOS ERP System. All rights reserved.
            </div>
        </footer>
    </div>
</body>
</html> 