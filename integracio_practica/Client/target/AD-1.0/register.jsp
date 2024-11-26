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
                    <input type="text" class="input-field" name="uname" placeholder="Write your username" required>
                </div>  
                <div class="input-box">
                    <label>Password:</label>
                    <input type="password" class="input-field" name="pw" placeholder="Write your password" required>
                </div>
                <div class="input-box button">
                    <button class="button-personalized button-accept" type="Submit">Register</button>
                </div>
                <div class="text">
                <h3>Have an account? <a href="login.jsp">Sign in</a> </h3> 
                </div>
            </form>
        </div>
        <footer>
            <p>&copy; 2024 Javier & Adrià @ AD Q1-2425</p>
        </footer>
    </body>
</html>
