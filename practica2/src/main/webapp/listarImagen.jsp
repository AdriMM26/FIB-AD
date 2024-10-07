<%-- 
    Document   : listarImagen
    Created on : 3 oct 2024, 16:41:13
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="database.OperationsDB" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>

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
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css">
        <title>Images</title>
    </head>
    <body>
        <div class="wrapper">
            <h1>Gallery</h1>
            <div class ="gallery">
                <%
                    List<String[]> gallery = OperationsDB.get_all_images();
                    int total = gallery.size();
                     
                    for(int i = 0; i < total; ++i) {
                        String [] imageInfo = gallery.get(i);
                        out.println(imageInfo[1]);
                        out.println("<div class=\"image\"> <img src=\"http://localhost:8080/practica2/imageDB/" + imageInfo[1] + "_" + imageInfo[0] + "\" alt=\"Image not found\"/> </div>");
                    }
                %>
            </div>
        </div>
    </body>
    <% } %>
</html>
