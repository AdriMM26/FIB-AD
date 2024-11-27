package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

//nous imports pel lab4
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
        else {
            try (PrintWriter out = response.getWriter()) {
                
                String title = request.getParameter("title");
                String description = request.getParameter("descp");
                String keywords = request.getParameter("keyw");
                String author = request.getParameter("ath");
                String creator = session.getAttribute("username").toString();
                String creationDate = request.getParameter("cdate");
                final Part filePart = request.getPart("file");
                String filename = filePart.getSubmittedFileName();
                
                if (!title.isBlank() && description != null && keywords != null && author != null && creator != null && creationDate != null && filePart != null) {
                    
                    final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
                    StreamDataBodyPart fileP = new StreamDataBodyPart("file", filePart.getInputStream());
                    FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
                    final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart
                        .field("title", title, MediaType.TEXT_PLAIN_TYPE)
                        .field("description", description, MediaType.TEXT_PLAIN_TYPE)
                        .field("keywords", keywords, MediaType.TEXT_PLAIN_TYPE)
                        .field("author", author, MediaType.TEXT_PLAIN_TYPE)
                        .field("creator", creator, MediaType.TEXT_PLAIN_TYPE)
                        .field("capture", creationDate, MediaType.TEXT_PLAIN_TYPE)
                        .field("filename", filename, MediaType.TEXT_PLAIN_TYPE)
                        .bodyPart(fileP);
                    
                    final WebTarget target = client.target("http://localhost:8080/Practica4-Server/resources/api/register");
                    final Response resp = target.request().post(Entity.entity(multipart, multipart.getMediaType()));
                    int code = resp.getStatus();
                
                    formDataMultiPart.close();
                    multipart.close();
                    
                    if (code == 200) {
                         session.setAttribute("successMessage", "Image was uploaded correctly!");
                         session.setAttribute("origin","Menu");
                         response.sendRedirect("success.jsp");

                    }
                    else {
                        session.setAttribute("errorMessage", "Error uploading the image");
                        session.setAttribute("origin","Menu");
                        response.sendRedirect("error.jsp");
                    }       
                }
                else{
                    session.setAttribute("errorMessage", "No field can be left empty");
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
    }// </editor-fold

}
