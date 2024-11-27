package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author alumne
 */
@WebServlet(name = "buscarImagen", urlPatterns = {"/buscarImagen"})
public class buscarImagen extends HttpServlet {
    
    private List<String[]> convertJSONtoList(String json) {
        JSONObject input = new JSONObject(json);
        JSONArray imgs = input.getJSONArray("result");
        List<String[]> images = new ArrayList<>();
        
        for (int i = 0; i < imgs.length(); i++) {
            // Obtenim el JSONObject "i" del JSONArray
            JSONObject img = imgs.getJSONObject(i);
            System.out.println(img);
            Map<String, Object> tempMap = img.toMap();
            String [] info = new String [9];
            // Convertim de Map<String, Object> a Map<String, String>
            int j = 0;
            for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
                info[j] = (String) entry.getValue(); 
                ++j;
            }
            
            // Guardem a la llist el Map que representa una iamtge.
            images.add(info);
        }
        
        return images;
    }
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
                String creationDate = request.getParameter("cdate");
                
                StringBuilder data = new StringBuilder();
                data.append("title=");
                data.append(URLEncoder.encode(title, "UTF-8"));
                data.append("&keywords=");
                data.append(URLEncoder.encode(keywords, "UTF-8"));
                data.append("&author=");
                data.append(URLEncoder.encode(author, "UTF-8"));
                data.append("&date=");
                data.append(URLEncoder.encode(creationDate, "UTF-8"));   
                data.append("&description=");
                data.append(URLEncoder.encode(description, "UTF-8"));
                

                
                URL url = new URL("http://localhost:8080/Practica4-Server/resources/api/search");
                //URI uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), data.toString(), null);
                //url = uri.toURL();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length",Integer.toString(data.toString().getBytes("UTF-8").length));
                connection.setDoOutput(true);
                connection.getOutputStream().write(data.toString().getBytes("UTF-8"));
                
                
                StringBuilder datareturn = new StringBuilder();
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                while((line = rd.readLine()) != null) {
                    datareturn.append(line);
                    datareturn.append('\r');
                }
                rd.close();
                connection.disconnect();
                
                int code = connection.getResponseCode();
                String result = datareturn.toString();
                
                System.out.println(datareturn);
                System.out.println(code);
                
                if (code == 200) {
                    List<String[]> images;
		    String strJsonResponse = datareturn.toString();
                    
                    
                    images = convertJSONtoList(strJsonResponse);
                    System.out.println(images);
                    session.setAttribute("images", images);
                    response.sendRedirect("buscarImagen.jsp");
                }
                else {
                    session.setAttribute("errorMessage", "Error searching an image, try again");
                    session.setAttribute("origin","Menu");
                    response.sendRedirect("error.jsp");
                }     
            }
        }            
    }
    
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
