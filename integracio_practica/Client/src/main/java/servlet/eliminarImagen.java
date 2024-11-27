package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alumne
 */
@WebServlet(name = "eliminarImagen", urlPatterns = {"/eliminarImagen"})
public class eliminarImagen extends HttpServlet {

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
        
        HttpSession session = request.getSession(false);
        if(session == null || request.getContentType()==null) 
        {
            session = request.getSession(true);
            session.setAttribute("errorMessage", "Invalid session");
            response.sendRedirect("error.jsp");     
        }
        else {
            try (PrintWriter out = response.getWriter()) {
                
                String id = request.getParameter("id");
                String creator = session.getAttribute("username").toString();
                
                if(!id.isBlank() && id!=null && !creator.isBlank() && creator!=null){
                    StringBuilder data = new StringBuilder();
                    data.append("id=");
                    data.append(URLEncoder.encode(id, "UTF-8"));
                    data.append("&creator=");
                    data.append(URLEncoder.encode(creator, "UTF-8"));

                    URL url = new URL("http://localhost:8080/Practica4-Server/resources/api/delete");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length",Integer.toString(data.toString().getBytes("UTF-8").length));
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(data.toString().getBytes("UTF-8"));

                    int code = connection.getResponseCode();
                    connection.disconnect();

                    if (code == 200) {
                        session.setAttribute("successMessage", "Image was deleted correctly!");
                        session.setAttribute("origin","Menu");
                        response.sendRedirect("success.jsp");
                    } else {
                        session.setAttribute("errorMessage", "Error deleting image");
                        session.setAttribute("origin","Menu");
                        response.sendRedirect("error.jsp");
                    }
                }
                else{
                    session.setAttribute("errorMessage", "Error deleting image");
                    session.setAttribute("origin","Menu");
                    response.sendRedirect("error.jsp");
                }
            } 
        }
    }  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
