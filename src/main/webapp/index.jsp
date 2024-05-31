<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Index Page</title>
</head>
<body>
    <h1>Welcome to the Index Page</h1>

    <%-- Retrieving the email attribute set by the servlet --%>
    <% String email = (String) request.getAttribute("email"); %>
    <% if (email != null) { %>
        <p>Your email: <%= email %></p>
    <% } else { %>
        <p>Email not found.</p>
    <% } %>

    <%-- Retrieving the errorMessage attribute set by the servlet --%>
    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% if (errorMessage != null) { %>
        <p>Error message: <%= errorMessage %></p>
    <% } %>

    <p><a href="login.jsp">Back to Login</a></p>
</body>
</html>
