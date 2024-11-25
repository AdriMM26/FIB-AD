package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author alumne
 */
@WebServlet(name = "descargarImagen", urlPatterns = {"/descargarImagen"})
public class descargarImagen extends HttpServlet {

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
                
                if(!id.isBlank() && id!=null){
                    
                    StringBuilder data = new StringBuilder();
                    data.append("http://localhost:8080/Backend/resources/jakartaee9/download/");
                    data.append(URLEncoder.encode(id, "UTF-8"));

                    URL url = new URL(data.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);

                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    reader.read();
                    
                    int code = connection.getResponseCode();
                    connection.disconnect();

                    if (code == 200) {
                        session.setAttribute("successMessage", "Image was downloaded correctly!");
                        session.setAttribute("origin","Menu");
                        response.sendRedirect("success.jsp");
                    } else {
                        session.setAttribute("errorMessage", "Error downloading image");
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
