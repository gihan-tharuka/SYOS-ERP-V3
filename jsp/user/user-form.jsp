<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.User" %>
<%
    User user = (User) request.getAttribute("user");
    boolean isEdit = user != null;
%>
<html>
<head>
    <title><%= isEdit ? "Edit User" : "Create User" %></title>
</head>
<body>
    <h2><%= isEdit ? "Edit User" : "Create User" %></h2>
    <form action="user" method="post">
        <input type="hidden" name="action" value="<%= isEdit ? "update" : "create" %>"/>
        <label>Username:</label>
        <input type="text" name="username" value="<%= isEdit ? user.getUsername() : "" %>" <%= isEdit ? "readonly" : "" %> required/><br/>
        <label>Password:</label>
        <input type="password" name="password" value="<%= isEdit ? user.getPassword() : "" %>" required/><br/>
        <label>Name:</label>
        <input type="text" name="name" value="<%= isEdit ? user.getName() : "" %>" required/><br/>
        <label>Email:</label>
        <input type="email" name="email" value="<%= isEdit ? user.getEmail() : "" %>" required/><br/>
        <label>Role:</label>
        <select name="role" <%= isEdit ? "readonly" : "" %>>
            <option value="admin" <%= isEdit && user.getRole().equals("admin") ? "selected" : "" %>>Admin</option>
            <option value="cashier" <%= isEdit && user.getRole().equals("cashier") ? "selected" : "" %>>Cashier</option>
            <option value="supplier" <%= isEdit && user.getRole().equals("supplier") ? "selected" : "" %>>Supplier</option>
            <option value="customer" <%= isEdit && user.getRole().equals("customer") ? "selected" : "" %>>Customer</option>
        </select><br/>
        <input type="submit" value="<%= isEdit ? "Update" : "Create" %>"/>
    </form>
    <a href="user">Back to List</a>
</body>
</html> 