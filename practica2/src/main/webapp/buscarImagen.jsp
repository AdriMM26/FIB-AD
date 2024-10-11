<%-- 
    Document   : buscarImagen
    Created on : 20 sept 2024, 10:44:31
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
    <script>
    function modifyImage(id, title, descp, keyw, auth, cdate) {
        window.location.href = "modificarImagen.jsp?title=" + encodeURIComponent(title) + "&id=" + encodeURIComponent(id)  + "&descp=" + encodeURIComponent(descp)
                                + "&keyw=" + encodeURIComponent(keyw) + "&auth=" + encodeURIComponent(auth)  + "&cdate=" + encodeURIComponent(cdate);
    };
    function deleteImage(id, title) {
       window.location.href = "eliminarImagen.jsp?title=" + encodeURIComponent(title) + "&id=" + encodeURIComponent(id);
    };
    
    </script>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css">
        <title>Search Images</title>
    </head>
    <body>
        <div class="wrapper">
            <h1>Search in Gallery</h1>
            <button class="button-logout" type="button" onclick="window.location.href='login.jsp'">LOG OUT</button>
            <button class="button-back" type="button" onclick="window.location.href='menu.jsp'">GO TO MENU</button>
            <div class = "filter">
                <form action="buscarImagen" method = "POST">
                    <div class="input-box">
                    <input type="text" name="title" placeholder="Image title">
                    </div> 
                    <div class="input-box">
                        <input type="text" name="descp" placeholder="Image description">
                    </div>
                    <div class="input-box">
                        <input type="text" name="keyw" placeholder="Image keywords">
                    </div>
                    <div class="input-box">
                        <input type="text" name="ath" placeholder="Image author">
                    </div>
                    <div class="input-box">
                        <input type="date" name="cdate" placeholder="Creation date">
                    </div>
                    <div class="btn">
                        <button class="button-personalized" type="submit">Search</button>
                    </div>
                    <br>
                </form>
            </div>
            <div class ="gallery">
                <%
                    if(session.getAttribute("images") != null)  {
                        List<String[]> gallery = (List<String[]>)session.getAttribute("images");
                        String username = (String)session.getAttribute("username");
                        int total = gallery.size();
                         
                        for(int i = 0; i < total; ++i) {
                            String [] imageInfo = gallery.get(i);
                            if(imageInfo[5].equals(username)) {
                                out.println("<div class=\"image-container\"> <h3>"+imageInfo[1]+"</h3><img src=\"http://localhost:8080/practica2/imageDB/" + imageInfo[1] + "_" + imageInfo[0] + "\" alt=\"Image not found\"/>");
                                out.println("<br>");
                                out.println("<button onClick= \"modifyImage('"+ imageInfo[0] +"','"+ imageInfo[1] +"','"+ imageInfo[2] +"','"+ imageInfo[3] +"','"+ imageInfo[4] +"','"+ imageInfo[6] +"')\">Modify</button>");
                                out.println("<button onClick= \"deleteImage('"+ imageInfo[0] +"','"+ imageInfo[1] +"')\">Delete</button>");
                                out.println("<br> </div>");   
                            }
                            else  {
                                out.println("<div class=\"image-container\"> <img src=\"http://localhost:8080/practica2/imageDB/"+ imageInfo[1] +"_"+ imageInfo[0] +"\" alt=\"Image not found\"/>");
                                out.println("<br> </div>");
                            }
                        }
                    }
                %>
            </div>
        </div>
        <footer>
            <p>&copy; 2024 Javier & Adrià @ AD Q1-2425</p>
        </footer>
    </body>
    <% } %>
</html>

