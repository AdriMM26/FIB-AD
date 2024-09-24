<%-- 
    Document   : login
    Created on : 17 sept 2024, 17:45:27
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <div class="wrapper">
            <h1>Login</h1>
            <form action="login" method = "POST">
                
                <div class="input-box">
                    <input type="text" name="uname" placeholder="Write your username" required>
                </div>  
                <div class="input-box">
                    <input type="password" name="pw" placeholder="Write your password" required>
                </div>
                <div class="input-box button">
                    <input type="Submit" value="Login">
                </div>
                <div class="text">
                <h3>Not registered yet? <a href="register.jsp">Sign Up</a> </h3> 
                </div>
            </form>
        </div>
    </body>
</html>
