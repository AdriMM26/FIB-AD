<%-- 
    Document   : modificarImagen
    Created on : 20 sept 2024, 10:42:51
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
            String title = request.getParameter("title");
            String id = request.getParameter("id");
            String creator = request.getParameter("creator");
            //session.removeAttribute("creator");
            String realUser = (String)session.getAttribute("username");
            if(!creator.equals(realUser)){
                session.setAttribute("errorMessage", "You are NOT the author");
                response.sendRedirect("error.jsp");
            }
            else{
                String auth = request.getParameter("auth");
                String descp = request.getParameter("descp");
                String keyw = request.getParameter("keyw");
                String cdate = request.getParameter("cdate");
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css">
        <title>Modify Image</title>
    </head>
    <body>
        <div class="wrapper">
            <h1>Modify image</h1>
            <button class="button-back" type="button" onclick="window.location.href='menu.jsp'">GO TO MENU</button>
            <% out.println("<h2>Please, enter the new values for " + title + "</h2>"); %>
            <div class< = "filer">
                <form action="modificarImagen" method = "POST">
                    <div class="input-hidden-box">
                        <% out.println("<input type=\"text\" name=\"id\" value =\""+id+"\" hidden>");%>
                        <% out.println("<input type=\"text\" name=\"otitle\" value =\""+title+"\" hidden>");%>
                    </div> 
                    <div class="input-box">
                        <% out.println("<input type=\"text\" class=\"input-field\" name=\"title\" placeholder=\"New title\" value=\""+title+"\">");%>
                    </div> 
                    <div class="input-box">
                        <% out.println("<input type=\"text\" class=\"input-field\" name=\"descp\" placeholder=\"New description\" value=\""+descp+"\">");%>
                    </div>
                    <div class="input-box">
                        <% out.println("<input type=\"text\" class=\"input-field\" name=\"keyw\" placeholder=\"New keywords\" value=\""+keyw+"\">");%>
                    </div>
                    <div class="input-box">
                        <% out.println("<input type=\"text\" class=\"input-field\" name=\"ath\" placeholder=\"New author\" value=\""+auth+"\">");%>
                    </div>
                    <div class="input-box">
                        <% out.println("<input type=\"date\" class=\"input-field\" name=\"cdate\" value=\""+cdate+"\">");%>
                    </div>
                    <div class="btn">
                        <button class="button-personalized button-submit" type="submit">Modify</button>
                    </div>
                    <br>
                </form>
            </div>
        </div>
        <footer>
            <p>&copy; 2024 Javier & Adrià @ AD Q1-2425</p>
        </footer>
    </body>
    <% } } %>
</html>
