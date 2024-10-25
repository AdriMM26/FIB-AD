package test.ad.resources;

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
        int new_user = -1;
        try{
            Connection connection = ConnectDB.open_connection();
            new_user = OperationsDB.register_user(username, password, connection);
            ConnectDB.close_connection(connection);
            return Response.ok(new_user).build();
        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(new_user).build();
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
            return Response.ok(existUser).build();
        }
        catch (ClassNotFoundException ex){
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(existUser).build();
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
            /* Com aconseguim el filePart? */
            
            Integer insertID = OperationsDB.upload_image(title, description, keywords, author, creator, capt_date, uploadDate, filePart, connection);
            ConnectDB.close_connection(connection);
            return Response.ok(insertID).build();                 
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(JakartaEE91Resource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.ok(0).build();
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
        
    }
    /**
    * POST method to delete an existing image
    * @param id
    * @return
    */
    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteImage (@FormParam("id") String id){
        
    }
    /**
    * GET method to search images by id
    * @param id
    * @return
    */
    @Path("searchID/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByID (@PathParam("id") int id){
        
    }
    /**
    * GET method to search images by title
    * @param title
    * @return
    */
    @Path("searchTitle/{title}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByTitle (@PathParam("title") String title){
        
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
    public Response searchByCreationDate (@PathParam("date") String date){
        
    }
    /**
    * GET method to search images by author
    * @param author
    * @return
    */
    @Path("searchAuthor/{author}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByAuthor (@PathParam("author") String author){
        
    }
    /**
    * GET method to search images by keyword
    * @param keywords
    * @return
    */
    @Path("searchKeywords/{keywords}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByKeywords (@PathParam("keywords") String keywords){
        
    }
}
