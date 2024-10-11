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
            <button class="button-back" type="button" onclick="window.location.href='menu.jsp'">Go back to MENU</button>
            <% out.println("<h2>Please, enter the new values for " + title + "</h2>"); %>
            <div class< = "filer">
                <form action="modificarImagen" method = "POST">
                    <div class="input-hidden-box">
                        <% out.println("<input type=\"text\" name=\"id\" value =\""+id+"\" hidden>");%>
                        <% out.println("<input type=\"text\" name=\"otitle\" value =\""+title+"\" hidden>");%>
                    </div> 
                    <div class="input-box">
                        <% out.println("<input type=\"text\" name=\"title\" placeholder=\"New title\" value=\""+title+"\">");%>
                    </div> 
                    <div class="input-box">
                        <% out.println("<input type=\"text\" name=\"descp\" placeholder=\"New description\" value=\""+descp+"\">");%>
                    </div>
                    <div class="input-box">
                        <% out.println("<input type=\"text\" name=\"keyw\" placeholder=\"New keywords\" value=\""+keyw+"\">");%>
                    </div>
                    <div class="input-box">
                        <% out.println("<input type=\"text\" name=\"ath\" placeholder=\"New author\" value=\""+auth+"\">");%>
                    </div>
                    <div class="input-box">
                        <% out.println("<input type=\"date\" name=\"cdate\" value=\""+cdate+"\">");%>
                    </div>
                    <div class="btn">
                        <button class="button-personalized" type="submit">Modify</button>
                    </div>
                    <br>
                </form>
            </div>
        </div>
    </body>
    <% } %>
</html>
