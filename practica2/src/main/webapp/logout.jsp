<%-- 
    Document   : logout
    Created on : 12 oct 2024, 14:14:08
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Remove specific session attributes if necessary
    session.removeAttribute("yourAttributeName"); // Replace 'yourAttributeName' with the actual session attribute you want to delete

    // Invalidate the entire session to log out the user
    session.invalidate(); 
    
    // Redirect the user to the login page
    response.sendRedirect("login.jsp");
%>
