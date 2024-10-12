<%-- 
    Document   : successOperation
    Created on : 30 sept 2024, 11:23:31
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css">
        <title>Success</title>
    </head>
    <body>
        <div class = "wrapper">
            <h1>The operation was successfull!</h1>
            <h3>Success: <% out.println((String)session.getAttribute("successMessage"));%></h3 
            <br>
            <% 
                if(session.getAttribute("origin") == "Menu"){ %>
                    <a class="ref" href="menu.jsp"> Go back to menu</a>
                   <%}
                else { %> <a class="ref" href="login.jsp">Go to Login</a> <% 
                } %>            
        </div>
        <footer>
            <p>&copy; 2024 Javier & Adrià @ AD Q1-2425</p>
        </footer>
    </body>
</html>
