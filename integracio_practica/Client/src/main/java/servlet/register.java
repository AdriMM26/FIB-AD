package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alumne
 */
@WebServlet(name = "register", urlPatterns = {"/register"})
public class register extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HttpSession session = request.getSession();
            
            String username = request.getParameter("uname");
            String password = request.getParameter("pw");
            
            if(!username.isBlank() && username!=null && password!=null && !password.isBlank()){
                StringBuilder data = new StringBuilder();
                data.append("username=");
                data.append(URLEncoder.encode(username, "UTF-8"));
                data.append("&password=");
                data.append(URLEncoder.encode(password, "UTF-8"));

                URL url = new URL("http://localhost:8080/Practica4-Server/resources/api/register_user");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length",Integer.toString(data.toString().getBytes("UTF-8").length));
                connection.setDoOutput(true);
                connection.getOutputStream().write(data.toString().getBytes("UTF-8"));

                int code = connection.getResponseCode();
                connection.disconnect();

                switch(code) {
                    case 500:
                        session.setAttribute("errorMessage","Ups, Something went wrong");
                        session.setAttribute("origin","Login");
                        response.sendRedirect("error.jsp");
                        break;
                    case 409: //409 already created
                        session.setAttribute("errorMessage","User already registered");
                        session.setAttribute("origin","Login");
                        response.sendRedirect("error.jsp");
                        break;
                    default: //201
                        session.setAttribute("successMessage","User registered succesfully");
                        session.setAttribute("origin","Login");
                        response.sendRedirect("success.jsp");
                        break;
                } 
            }
            else{
                session.setAttribute("errorMessage","Username or password include whitespaces or is empty");
                session.setAttribute("origin","Login");
                response.sendRedirect("error.jsp");
            }   
        }    
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
