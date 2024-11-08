package test.ad.resources;

import com.google.gson.Gson;
import database.ConnectDB;
import database.OperationsDB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author 
 */
@Path("jakartaee9")
public class JakartaEE91Resource {
    
    @GET
    public Response ping(){
        return Response
                .ok("ping Jakarta EE")
                .build();
    }
    
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
            int new_user = OperationsDB.register_user(username, password, connection);
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
        boolean existUser = false;
        try{
            Connection connection = ConnectDB.open_connection();
            existUser = OperationsDB.check_user(username, password, connection);
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
        Boolean updated = false;
        try {
            Connection connection = ConnectDB.open_connection();
            if(OperationsDB.is_user_image(connection, id, creator)) {
                updated = OperationsDB.modify_image(id, title, description, keywords, author, capt_date, connection);
                ConnectDB.close_connection(connection);
                return Response.ok(updated).build();
            }
            else {
                ConnectDB.close_connection(connection);
                return Response.ok(updated).build();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(updated).build();
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
        boolean deleted = false;
        try {
            Connection connection = ConnectDB.open_connection();
            deleted = OperationsDB.is_user_image(connection, id, creator);
            if(deleted) {
                OperationsDB.delete_image(id, connection);
                ConnectDB.close_connection(connection);
                return Response.ok(deleted).build();
            }
            else {
                ConnectDB.close_connection(connection);
                return Response.ok(deleted).build();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(deleted).build();
        } 
    }
        
    //SEARCH BUSQUEDA CONJUNTA PUNTO EXTRA ENUNCIADO
    /**
    * GET method to search images
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
    public Response search (@PathParam("id") int id,
            @PathParam("title") String title,
            @PathParam("date") String date,
            @PathParam("author") String author,
            @PathParam("keywords") String keywords,
            @PathParam("description") String description) {
        try {
                Connection connection = ConnectDB.open_connection();
                List <String[]> images = OperationsDB.get_images(title, description, keywords, author, date, connection);
                Gson json = new Gson();
                json.toJson(images);
                ConnectDB.close_connection(connection);
                return Response.ok(json).build();
            
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
                return Response.ok(null).build();
            }
        }
    }
