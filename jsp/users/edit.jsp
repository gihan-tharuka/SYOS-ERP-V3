<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SYOS ERP - Edit User</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <h1>Edit User</h1>
        
        <form action="/users/edit" method="post" class="user-form">
            <input type="hidden" name="role" value="${param.role}">
            <input type="hidden" name="username" value="${user.username}">
            
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" value="${user.username}" disabled>
            </div>
            
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${user.email}" required>
            </div>
            
            <c:if test="${param.role == 'cashier'}">
                <div class="form-group">
                    <label for="fullName">Full Name:</label>
                    <input type="text" id="fullName" name="fullName" value="${user.fullName}" required>
                </div>
                <div class="form-group">
                    <label for="mobile">Mobile:</label>
                    <input type="text" id="mobile" name="mobile" value="${user.mobile}" required>
                </div>
            </c:if>
            
            <c:if test="${param.role == 'supplier'}">
                <div class="form-group">
                    <label for="companyName">Company Name:</label>
                    <input type="text" id="companyName" name="companyName" value="${user.companyName}" required>
                </div>
                <div class="form-group">
                    <label for="contactPerson">Contact Person:</label>
                    <input type="text" id="contactPerson" name="contactPerson" value="${user.contactPerson}" required>
                </div>
                <div class="form-group">
                    <label for="mobile">Mobile:</label>
                    <input type="text" id="mobile" name="mobile" value="${user.mobile}" required>
                </div>
            </c:if>
            
            <div class="form-actions">
                <button type="submit" class="button">Update User</button>
                <a href="/users?role=${param.role}" class="button">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html> 