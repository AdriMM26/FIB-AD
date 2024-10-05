 <%-- 
    Document   : eliminarImagen
    Created on : 20 sept 2024, 10:43:43
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%
        if(session.getAttribute("username") == null) {
            session.setAttribute("errorMessage", "User unknown");
            response.sendRedirect("error.jsp");
        }
        else {
            String title = request.getParameter("title");
            String id = request.getParameter("id");
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Image</title>
    </head>
    <body>
        <% out.println("<h1>" + title + "</h1>"); %>
    </body>
    <% } %>
</html>
