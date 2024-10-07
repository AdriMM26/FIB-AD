<%-- 
    Document   : error
    Created on : 20 sept 2024, 10:45:45
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css">
        <title>Error</title>
    </head>
    <body>
        <div class = "wrapper">
            <h1>Ups, Something went wrong</h1>
            <h3>Error: <% out.println((String)session.getAttribute("errorMessage"));%></h3>
            <br>
            <% 
                if(session.getAttribute("origin") == "Menu"){ %>
                    <a class="ref" href="menu.jsp"> Go back to menu</a>
                   <%}
                else { %> <a class="ref" href="login.jsp">Go to Login</a> <% 
                } %>            
        </div>
    </body>
</html>
