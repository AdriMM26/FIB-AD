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
            
            String id = request.getParameter("id");
            String title = request.getParameter("title");
            String creator = request.getParameter("creator");
            //session.removeAttribute("creator");
            String realUser = (String)session.getAttribute("username");
            if(!creator.equals(realUser)){
                session.setAttribute("errorMessage", "You are NOT the author");
                response.sendRedirect("error.jsp");
            }
            else{
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css">
        <title>Delete Image</title>
    </head>
    <body>
        <h1>Delete image</h1>
        <button class="button-back" type="button" onclick="window.location.href='menu.jsp'">Go back to MENU</button>
        <h2>Are you sure you want to delete the image titled: <% out.println(title); %> ?</h2>
        <%out.println("<div class=\"image-container\"> <img src=\"http://localhost:8080/practica2/imageDB/" + title + "_" + id + "\" alt=\"Image not found\"/></div>");%>
        <div class="btn">
            <form action="eliminarImagen" method="POST">
               <%
               out.println("<input name = \"title\" type=\"text\" value =\""+title+"\" hidden/>");
               out.println("<input name = \"id\" type=\"text\" value =\""+id+"\"hidden/>");
               out.println("<button type=\"submit\">Accept</button>");
               %>
            </form>
      
            <button type="button" onclick="window.location.href='buscarImagen.jsp'">Cancel</button>
        </div>
    </body>
    <% } } %>
</html>
