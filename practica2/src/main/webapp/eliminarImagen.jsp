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
            session.setAttribute("usedID", id);
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Image</title>
    </head>
    <body>
        <h1>DELETE IMAGE</h1>
        <h2>Are you sure you want to delete the image titled: <% out.println(title); %> ?</h2>
        <div class="btn">
            <form action="eliminarImagen" method="POST">
               <button type="submit">Accept</button> 
            </form>
      
            <button type="button" onclick="window.location.href='buscarImagen.jsp'">Cancel</button>
        </div>
    </body>
    <% } %>
</html>
