<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .bg-login {
            background: linear-gradient(135deg, rgba(79,70,229,0.1) 0%, rgba(255,255,255,1) 100%);
        }
        .loading {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(255, 255, 255, 0.8);
            z-index: 1000;
        }
        .loading-spinner {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
    </style>
</head>
<body class="bg-login min-h-screen flex items-center justify-center p-4">
    <div class="loading">
        <div class="loading-spinner">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
            <p class="mt-4 text-indigo-600">Processing login request...</p>
        </div>
    </div>

    <div class="w-full max-w-md">
        <div class="text-center mb-8">
            <h1 class="text-3xl font-bold text-indigo-600 mb-2">SYOS ERP System</h1>
            <p class="text-gray-600">Please sign in to access your account</p>
        </div>

        <div id="error-message" class="bg-red-50 border-l-4 border-red-500 p-4 mb-6 rounded hidden">
            <div class="flex">
                <div class="flex-shrink-0">
                    <svg class="h-5 w-5 text-red-500" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
                    </svg>
                </div>
                <div class="ml-3">
                    <p class="text-sm text-red-700" id="error-text"></p>
                </div>
            </div>
        </div>

        <form id="loginForm" class="bg-white shadow-lg rounded-lg px-8 pt-8 pb-8 mb-4">
            <div class="mb-6">
                <label for="role" class="block text-gray-700 text-sm font-medium mb-2">Role</label>
                <select id="role" name="role" required class="block w-full px-3 py-3 text-gray-700 bg-gray-50 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200">
                    <option value="" disabled selected>Select Role</option>
                    <option value="admin">Admin</option>

                    <option value="customer">Customer</option>
                </select>
            </div>

            <div class="mb-6">
                <label for="username" class="block text-gray-700 text-sm font-medium mb-2">Username</label>
                <input type="text" id="username" name="username" required class="block w-full px-3 py-3 text-gray-700 bg-gray-50 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200" placeholder="Enter your username">
            </div>

            <div class="mb-8">
                <label for="password" class="block text-gray-700 text-sm font-medium mb-2">Password</label>
                <input type="password" id="password" name="password" required class="block w-full px-3 py-3 text-gray-700 bg-gray-50 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition duration-200" placeholder="Enter your password">
            </div>

            <div class="flex items-center justify-between">
                <button type="submit" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 px-4 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition duration-200 transform hover:scale-[1.02]">
                    Login
                </button>
            </div>
        </form>

        <div class="text-center text-sm text-gray-500">
            © 2023 SYOS ERP System. All rights reserved.
        </div>
    </div>

    <script>
        // Track active requests
        let activeRequests = new Set();
        const MAX_CONCURRENT_REQUESTS = 3;

        // Show/hide loading spinner
        function showLoading() {
            document.querySelector('.loading').style.display = 'block';
        }

        function hideLoading() {
            document.querySelector('.loading').style.display = 'none';
        }

        // Show error message
        function showError(message) {
            const errorDiv = document.getElementById('error-message');
            const errorText = document.getElementById('error-text');
            errorText.textContent = message;
            errorDiv.classList.remove('hidden');
        }

        // Hide error message
        function hideError() {
            document.getElementById('error-message').classList.add('hidden');
        }

        // Handle form submission
        document.getElementById('loginForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            // Hide any previous error
            hideError();

            // Check if we've reached the maximum concurrent requests
            if (activeRequests.size >= MAX_CONCURRENT_REQUESTS) {
                showError('Too many login attempts. Please wait a moment and try again.');
                return;
            }

            const formData = new FormData(this);
            const loginData = {
                username: formData.get('username'),
                password: formData.get('password'),
                role: formData.get('role')
            };

            // Create a unique request ID
            const requestId = Date.now();
            activeRequests.add(requestId);

            try {
                showLoading();

                const response = await fetch('${pageContext.request.contextPath}/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams(loginData)
                });

                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    const result = await response.text();
                    if (result.includes('error')) {
                        showError(result);
                    } else {
                        // Let the servlet handle the redirect
                        window.location.href = response.url;
                    }
                }
            } catch (error) {
                showError('An error occurred during login. Please try again.');
            } finally {
                activeRequests.delete(requestId);
                hideLoading();
            }
        });
    </script>
</body>
</html>