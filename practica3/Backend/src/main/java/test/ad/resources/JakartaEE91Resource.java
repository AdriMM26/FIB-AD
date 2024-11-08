package test.ad.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import database.ConnectDB;
import database.OperationsDB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;




/**
 *
 * @author 
 */
@Path("jakartaee9")
public class JakartaEE91Resource {
    
    /**
     * OPERACIONES EXTRAS
     */
    /**
    * POST method to register new user in the application
    * @param username
    * @param password
    * @return
    */
    @Path("register_user")
    @POST
    public Response register_user(@FormParam("username") String username,
            @FormParam("password") String password){
        try{
            Connection connection = ConnectDB.open_connection();
            Integer new_user = OperationsDB.register_user(username, password, connection);
            ConnectDB.close_connection(connection);
            switch (new_user) {
                case 1:
                    return Response.status(201).build();
                case -1:
                    return Response.status(409).build();
                default:
                    return Response.serverError().build();
            }
        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    /**
    * GET method to search images with zero or more parameters
    * @param id
    * @param title
    * @param date
    * @param author
    * @param keywords
    * @param description
    * @return
    */
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByParameters (@QueryParam("id") String id,
            @QueryParam("title") String title,
            @QueryParam("date") String date,
            @QueryParam("author") String author,
            @QueryParam("keywords") String keywords,
            @QueryParam("description") String description) {
        try {
            Connection connection = ConnectDB.open_connection();
            List <String[]> images = OperationsDB.get_images(id,title, description, keywords, author, date, connection);
            String json = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                json = objectMapper.writeValueAsString(images);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            }
            ConnectDB.close_connection(connection);
            return Response.ok(json).build();
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    /**
    * OPERACIONES DEL SERVICIO REST
    */
    /**
    * POST method to login in the application
    * @param username
    * @param password
    * @return
    */
    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("username") String username,
            @FormParam("password") String password) {
        try{
            Connection connection = ConnectDB.open_connection();
            Boolean existUser = OperationsDB.check_user(username, password, connection);
            ConnectDB.close_connection(connection);
            if(existUser){ 
                return Response.ok().build();
            }
            else return Response.status(404).build();
        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    /**
    * POST method to register a new image – File is not uploaded
    * @param title
    * @param description
    * @param keywords
    * @param author
    * @param creator
    * @param capt_date
    * @return
    */
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerImage (@FormParam("title") String title,
            @FormParam("description") String description,
            @FormParam("keywords") String keywords,
            @FormParam("author") String author,
            @FormParam("creator") String creator,
            @FormParam("capture") String capt_date){
        try{
            Connection connection = ConnectDB.open_connection();
            String uploadDate = LocalDate.now().toString();
            
            Integer insertID = OperationsDB.upload_image(title, description, keywords, author, creator, capt_date, uploadDate, connection);
            ConnectDB.close_connection(connection);
            if(insertID > 0){
                return Response.status(201).build();
            }
            else return Response.serverError().build();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }  
    }
    
    /**
    * POST method to modify an existing image
    * @param id
    * @param title
    * @param description
    * @param keywords
    * @param author
    * @param creator, used for checking image ownership
    * @param capt_date
    * @return
    */
    @Path("modify")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyImage (@FormParam("id") String id,
            @FormParam("title") String title,
            @FormParam("description") String description,
            @FormParam("keywords") String keywords,
            @FormParam("author") String author,
            @FormParam("creator") String creator,
            @FormParam("capture") String capt_date){
        try {
            Connection connection = ConnectDB.open_connection();
            if(OperationsDB.is_user_image(connection, id, creator)) {
                boolean updated = OperationsDB.modify_image(id, title, description, keywords, author, capt_date, connection);
                ConnectDB.close_connection(connection);
                if(updated) {
                    return Response.ok().build();
                }
                else return Response.serverError().build();   
            }
            else {
                ConnectDB.close_connection(connection);
                return Response.status(401).build();
            }
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }      
    }
    
    /**
    * POST method to delete an existing image
    * @param id
    * @param creator
    * @return
    */
    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteImage (@FormParam("id") String id,
            @FormParam("creator") String creator){
        try {
            Connection connection = ConnectDB.open_connection();
            if(OperationsDB.is_user_image(connection, id, creator)) {
                OperationsDB.delete_image(id, connection);
                ConnectDB.close_connection(connection);
                return Response.ok().build();
            }
            else {
                ConnectDB.close_connection(connection);
                return Response.status(401).build();
            }
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        } 
    }
    
    /**
    * GET method to search images by id
    * @param id
    * @return
    */
    @Path("searchID/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByID (@PathParam("id") int id) {
        try {
            Connection connection = ConnectDB.open_connection();
            String insertID = Integer.toString(id);
            List <String[]> images = OperationsDB.get_images(insertID,null,null,null,null,null,connection);
            String json = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                json = objectMapper.writeValueAsString(images);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            }
            ConnectDB.close_connection(connection);
            return Response.ok(json).build();
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    /**
    * GET method to search images by title
    * @param title
    * @return
    */
    @Path("searchTitle/{title}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByTitle (@PathParam("title") String title) {
        try {
            Connection connection = ConnectDB.open_connection();
            List <String[]> images = OperationsDB.get_images(null,title,null,null,null,null,connection);
            String json = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                json = objectMapper.writeValueAsString(images);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            }
            ConnectDB.close_connection(connection);
            return Response.ok(json).build();
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    /**
    * GET method to search images by creation date. Date format should be
    * yyyy-mm-dd
    * @param date
    * @return
    */
    @Path("searchCreationDate/{date}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByCreationDate (@PathParam("date") String date) {
        try {
            Connection connection = ConnectDB.open_connection();
            List <String[]> images = OperationsDB.get_images(null,null,null,null,null,date,connection);
            String json = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                json = objectMapper.writeValueAsString(images);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            }
            ConnectDB.close_connection(connection);
            return Response.ok(json).build();
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    /**
    * GET method to search images by author
    * @param author
    * @return
    */
    @Path("searchAuthor/{author}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByAuthor (@PathParam("author") String author) {
        try {
            Connection connection = ConnectDB.open_connection();
            List <String[]> images = OperationsDB.get_images(null,null,null,null,author,null,connection);
            String json = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                json = objectMapper.writeValueAsString(images);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            }
            ConnectDB.close_connection(connection);
            return Response.ok(json).build();
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }    
    }
    
    /**
    * GET method to search images by keyword
    * @param keywords
    * @return
    */
    @Path("searchKeywords/{keywords}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByKeywords (@PathParam("keywords") String keywords) {
        try {
            Connection connection = ConnectDB.open_connection();
            List <String[]> images = OperationsDB.get_images(null,null,null,keywords,null,null,connection);
            String json = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                json = objectMapper.writeValueAsString(images);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            }
            ConnectDB.close_connection(connection);
            return Response.ok(json).build();
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }    
    }
}
