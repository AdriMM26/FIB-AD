<%-- 
    Document   : logout
    Created on : 12 oct 2024, 14:14:08
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    session.removeAttribute("username");
    session.invalidate(); 
    response.sendRedirect("login.jsp");
%>
