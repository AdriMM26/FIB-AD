<%-- 
    Document   : menu
    Created on : 20 sept 2024, 10:41:17
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
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Menu</title>
    </head>
    <body>
        <div class="wrapper">
            <h1>Welcome <% out.println((String)session.getAttribute("username"));%></h1>
            <div class="card">
                <h3> <a href="buscarImagen.jsp">Search an Image</a> </h3>
            </div>
            <div class="card">
                <h3> <a href="listarImagen.jsp">List all Images</a></h3>
            </div>
            <div class="card">
                <h3> <a href="registrarImagen.jsp">Register an Image</a> </h3>
            </div>
        </div>
    </body>
    <% } %>
</html>
