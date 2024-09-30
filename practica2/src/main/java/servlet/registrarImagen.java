package servlet;

import database.ConnectDB;
import database.OperationsDB;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alumne
 */
@WebServlet(name = "registrarImagen", urlPatterns = {"/registrarImagen"})
@MultipartConfig
public class registrarImagen extends HttpServlet {

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
        else{
            try {
            /*Open a connection with DB */
            Connection connection = ConnectDB.open_connection();
            
            /* Get data from input box */
            String title = request.getParameter("title");
            String description = request.getParameter("descp");
            String keywords = request.getParameter("keyw");
            String author = request.getParameter("ath");
            String creator = session.getAttribute("username").toString();
            String creationDate = request.getParameter("cdate");
            String uploadDate = LocalDate.now().toString();
            /* Esta clase representa una parte o elemento de formulario que se recibió dentro de una solicitud POST con multipart/form-data. */
            Part filePart = request.getPart("file");
            
            if (title != null && description != null && keywords != null && author != null && creator != null && creationDate != null && uploadDate != null && filePart != null) {
                   boolean insert = OperationsDB.upload_image(title, description, keywords, author, creator, creationDate, uploadDate, filePart, connection);
                   if (insert) {
                       session.setAttribute("successMessage", "Image was uploaded correctly!");
                       session.setAttribute("origin","Menu");
                       response.sendRedirect("successOperation.jsp");
                   }
                   else {
                       session.setAttribute("errorMessage", "Error uploading the image");
                       session.setAttribute("origin","Menu");
                       response.sendRedirect("error.jsp");
                   }
                   ConnectDB.close_connection(connection);
            }
            else{
                session.setAttribute("errorMessage", "No field can be left empty");
                session.setAttribute("origin","Menu");
                response.sendRedirect("error.jsp");
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
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
    }// </editor-fold

}
