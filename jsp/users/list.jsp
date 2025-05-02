<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS ERP - User List</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <div class="container">
        <h1>User List</h1>
        
        <div class="role-filter">
            <form action="/users" method="get">
                <label for="role">Filter by Role:</label>
                <select name="role" id="role" onchange="this.form.submit()">
                    <option value="admin" ${param.role == 'admin' ? 'selected' : ''}>Admin</option>
                    <option value="cashier" ${param.role == 'cashier' ? 'selected' : ''}>Cashier</option>
                    <option value="supplier" ${param.role == 'supplier' ? 'selected' : ''}>Supplier</option>
                </select>
            </form>
        </div>

        <div class="user-list">
            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Email</th>
                        <c:if test="${param.role == 'cashier'}">
                            <th>Full Name</th>
                            <th>Mobile</th>
                        </c:if>
                        <c:if test="${param.role == 'supplier'}">
                            <th>Company Name</th>
                            <th>Contact Person</th>
                            <th>Mobile</th>
                        </c:if>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="user">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <c:if test="${param.role == 'cashier'}">
                                <td>${user.fullName}</td>
                                <td>${user.mobile}</td>
                            </c:if>
                            <c:if test="${param.role == 'supplier'}">
                                <td>${user.companyName}</td>
                                <td>${user.contactPerson}</td>
                                <td>${user.mobile}</td>
                            </c:if>
                            <td>
                                <a href="/users/edit?username=${user.username}&role=${param.role}">Edit</a>
                                <a href="/users/delete?username=${user.username}&role=${param.role}" 
                                   onclick="return confirm('Are you sure you want to delete this user?')">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="action-buttons">
            <a href="/users/add?role=${param.role}" class="button">Add New User</a>
            <a href="/admin/user-management.jsp" class="button">Back to User Management</a>
        </div>
    </div>
</body>
</html> 