package servlet;

import database.ConnectDB;
import database.OperationsDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.sql.Connection;
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
            try {
            Connection connection = ConnectDB.open_connection();
 
            String id = request.getParameter("id");
            String oldTitle = request.getParameter("otitle");
            String title = request.getParameter("title");
            String description = request.getParameter("descp");
            String keywords = request.getParameter("keyw");
            String author = request.getParameter("ath");
            String creationDate = request.getParameter("cdate");
            
            String oldImageName = oldTitle+"_"+id;
            String newImageName = title+"_"+id;
                 
            Boolean updated = OperationsDB.modify_image(id, title, description, keywords, author, creationDate, connection);
            if (updated) {
                File oldfile = new File("/var/webapp/imageDB/" + oldImageName);
                File newfile = new File("/var/webapp/imageDB/" + newImageName);
                if(oldfile.renameTo(newfile)) {
                    session.setAttribute("successMessage", "Image updated");
                    session.setAttribute("origin","Menu");
                    response.sendRedirect("success.jsp");
                }
                else {
                    Boolean aux = OperationsDB.modify_image(id, oldTitle, description, keywords, author, creationDate, connection);
                    session.setAttribute("errorMessage", "Error updating the image title, try again");
                    session.setAttribute("origin","Menu");
                    response.sendRedirect("error.jsp");
                }
            }
            else {
                session.setAttribute("errorMessage", "Error updating the image, try again");
                session.setAttribute("origin","Menu");
                response.sendRedirect("error.jsp");
            }     
            ConnectDB.close_connection(connection);
            
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
    }// </editor-fold>

}
