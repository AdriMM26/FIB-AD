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
        <title>Modify Image</title>
    </head>
    <body>
        <% out.println("<h1>Please, enter the new values for " + title + "</h1>"); %>
        <div class = "filer">
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
                        <button type="submit">Modify</button>
                    </div>
                    <br>
                </form>
            </div>
    </body>
    <% } %>
</html>
