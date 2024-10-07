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
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css">
        <title>Register</title>
    </head>
    <body>
        <div class = "wrapper">
            <h1>Register</h1>
            <form action="register" method = "POST">

                <div class="input-box">
                    <label>Username:</label>
                    <input type="text" name="uname" placeholder="Write your username" required>
                </div>  
                <div class="input-box">
                    <label>Password:</label>
                    <input type="password" name="pw" placeholder="Write your password" required>
                </div>
                <div class="input-box button">
                    <input type="Submit" value="Register">
                </div>
                <div class="text">
                <h3>Have an account? <a href="login.jsp">Sign on</a> </h3> 
                </div>
            </form>
        </div>
    </body>
</html>
