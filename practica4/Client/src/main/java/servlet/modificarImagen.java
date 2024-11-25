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
@WebServlet(name = "modificarImagen", urlPatterns = {"/modificarImagen"})
public class modificarImagen extends HttpServlet {

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
                String title = request.getParameter("title");
                String description = request.getParameter("descp");
                String keywords = request.getParameter("keyw");
                String author = request.getParameter("ath");
                String creator = session.getAttribute("username").toString();
                String creationDate = request.getParameter("cdate");

                if (!title.isBlank() && title != null && description != null && keywords != null && author != null && creator != null && creationDate != null) {
                    StringBuilder data = new StringBuilder();
                    data.append("id=");
                    data.append(URLEncoder.encode(id, "UTF-8"));
                    data.append("&title=");
                    data.append(URLEncoder.encode(title, "UTF-8"));
                    data.append("&description=");
                    data.append(URLEncoder.encode(description, "UTF-8"));
                    data.append("&keywords=");
                    data.append(URLEncoder.encode(keywords, "UTF-8"));
                    data.append("&author=");
                    data.append(URLEncoder.encode(author, "UTF-8"));
                    data.append("&creator=");
                    data.append(URLEncoder.encode(creator, "UTF-8"));
                    data.append("&capture=");
                    data.append(URLEncoder.encode(creationDate, "UTF-8"));

                    URL url = new URL("http://localhost:8080/Backend/resources/jakartaee9/modify");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length",Integer.toString(data.toString().getBytes("UTF-8").length));
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(data.toString().getBytes("UTF-8"));

                    int code = connection.getResponseCode();
                    connection.disconnect();

                    if (code == 200) {
                        session.setAttribute("successMessage", "Image updated");
                        session.setAttribute("origin","Menu");
                        response.sendRedirect("success.jsp");
                    }
                    else {
                        session.setAttribute("errorMessage", "Error updating the image, try again");
                        session.setAttribute("origin","Menu");
                        response.sendRedirect("error.jsp");
                    }   
                }
                else {
                        session.setAttribute("errorMessage", "No field can be left empty or blank");
                        session.setAttribute("origin","Menu");
                        response.sendRedirect("error.jsp");
                    }   
                    
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
