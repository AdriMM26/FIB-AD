<%-- 
    Document   : buscarImagen
    Created on : 20 sept 2024, 10:44:31
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    function modifyImage(id, title, creator, descp, keyw, auth, cdate) {
        // Create a form dynamically
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = 'modificarImagen.jsp'; // The JSP that handles the deletion

        // Add hidden fields for all variables
        const inputId = document.createElement('input');
        inputId.type = 'hidden';
        inputId.name = 'id';
        inputId.value = id;

        const inputTitle = document.createElement('input');
        inputTitle.type = 'hidden';
        inputTitle.name = 'title';
        inputTitle.value = title;

        const inputCreator = document.createElement('input');
        inputCreator.type = 'hidden';
        inputCreator.name = 'creator';
        inputCreator.value = creator;
        
        const inputDesc = document.createElement('input');
        inputDesc.type = 'hidden';
        inputDesc.name = 'descp';
        inputDesc.value = descp;
        
        const inputKeyw = document.createElement('input');
        inputKeyw.type = 'hidden';
        inputKeyw.name = 'keyw';
        inputKeyw.value = keyw;
        
        const inputAuth = document.createElement('input');
        inputAuth.type = 'hidden';
        inputAuth.name = 'auth';
        inputAuth.value = auth;
        
        const inputDate = document.createElement('input');
        inputDate.type = 'hidden';
        inputDate.name = 'cdate';
        inputDate.value = cdate;

        // Append inputs to the form
        form.appendChild(inputId);
        form.appendChild(inputTitle);
        form.appendChild(inputCreator);
        form.appendChild(inputDesc);
        form.appendChild(inputKeyw);
        form.appendChild(inputAuth);
        form.appendChild(inputDate);

        // Append the form to the body and submit it
        document.body.appendChild(form);
        form.submit();
    };
    function deleteImage(id, title, creator, filename) {
        // Create a form dynamically
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = 'eliminarImagen.jsp'; // The JSP that handles the deletion

        // Add hidden fields for id, title, and creator
        const inputId = document.createElement('input');
        inputId.type = 'hidden';
        inputId.name = 'id';
        inputId.value = id;

        const inputTitle = document.createElement('input');
        inputTitle.type = 'hidden';
        inputTitle.name = 'title';
        inputTitle.value = title;

        const inputCreator = document.createElement('input');
        inputCreator.type = 'hidden';
        inputCreator.name = 'creator';
        inputCreator.value = creator;
        
        const inputFilename = document.createElement('input');
        inputFilename.type = 'hidden';
        inputFilename.name = 'filename';
        inputFilename.value = filename;

        // Append inputs to the form
        form.appendChild(inputId);
        form.appendChild(inputTitle);
        form.appendChild(inputCreator);
        form.appendChild(inputFilename);

        // Append the form to the body and submit it
        document.body.appendChild(form);
        form.submit();
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
            <button class="button-logout" type="button" onclick="window.location.href='logout.jsp'">LOG OUT</button>
            <button class="button-back" type="button" onclick="window.location.href='menu.jsp'">GO TO MENU</button>
            <div class = "filter">
                <form action="buscarImagen" method = "POST"> 
                    <div class="input-box">
                    <input type="text" class="input-field" name="title" placeholder="Image title">
                    </div> 
                    <div class="input-box">
                        <input type="text" class="input-field" name="descp" placeholder="Image description">
                    </div>
                    <div class="input-box">
                        <input type="text" class="input-field" name="keyw" placeholder="Image keywords">
                    </div>
                    <div class="input-box">
                        <input type="text" class="input-field" name="ath" placeholder="Image author">
                    </div>
                    <div class="input-box">
                        <input type="date" class="input-field" name="cdate" placeholder="Creation date">
                    </div>
                    <div class="btn">
                        <button class="button-personalized button-submit" type="submit">Search</button>
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
                                out.println("<div class=\"image-container\"> <h3 class=\"title\">"+imageInfo[1]+"</h3> <img src=\"http://localhost:8080/Client/imageDB/" + imageInfo[8] + "_" + imageInfo[0] + "\" alt=\"Image not found\"/> <br> <label class=\"author\">Author: "+imageInfo[4]+"</label> <br> ");
                                out.println("<br>");
                                out.println("<div class=\"button-display\">");
                                out.println("<button class=\"button-personalized button-modify\" onClick= \"modifyImage('"+ imageInfo[0] +"','"+ imageInfo[1] +"','"+ imageInfo[5] + "','" + imageInfo[2] +"','"+ imageInfo[3] +"','"+ imageInfo[4] +"','"+ imageInfo[6] +"')\">Modify</button>");
                                out.println("<button class=\"button-personalized button-delete\" onClick= \"deleteImage('"+ imageInfo[0] +"','"+ imageInfo[1] +"','"+ imageInfo[5] + "','" + imageInfo[8] +"')\">Delete</button>");
                                out.println("</div>");
                                out.println("<br> </div>");   
                            }
                            else  {
                                out.println("<div class=\"image-container\"> <h3 class=\"title\">"+imageInfo[1]+"</h3> <img src=\"http://localhost:8080/Client/imageDB/"+ imageInfo[1] +"_"+ imageInfo[0] +"\" alt=\"Image not found\"/> <br> <label class=\"author\">Author: "+imageInfo[4]+"</label> <br> ");
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

