<%-- 
    Document   : register
    Created on : 20 sept 2024, 11:53:46
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Register</h1>
        <form action="register" method="post">
            <label>Username:</label>
            <input type="text" placeholder="Enter Username" name="uname" required><!-- comment -->
            <br><br><label>Password:</label>
            <input type="password" placeholder="Enter Password" name="psw" required><!-- comment -->
            <br><br><br><br><button type="submit">Register</button>
        </form>
    </body>
</html>
