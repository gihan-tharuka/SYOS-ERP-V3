<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>User List</title>
</head>
<body>
    <h2>User List</h2>
    <a href="user?action=new">Create New User</a>
    <table border="1">
        <tr>
            <th>Username</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Actions</th>
        </tr>
        <%
            List<User> userList = (List<User>) request.getAttribute("userList");
            for (User user : userList) {
        %>
        <tr>
            <td><%= user.getUsername() %></td>
            <td><%= user.getName() %></td>
            <td><%= user.getEmail() %></td>
            <td><%= user.getRole() %></td>
            <td>
                <a href="user?action=edit&username=<%= user.getUsername() %>">Edit</a>
                <a href="user?action=delete&username=<%= user.getUsername() %>" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
        <% } %>
    </table>
</body>
</html> 