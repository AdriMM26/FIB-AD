/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restad.restoperations;

import dbConnector.dbConnector;

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
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

// Nous imports pel lab 4
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 *
 * @author alumne
 */
@Path("api")
public class RestOperations {
	dbConnector dbc;
        final String imgsPath = "/var/webapp/ad-lab";
	
	/**
	 * Auxiliar function for image List to JSONObject transformation.
	 */
        private JSONObject convertListToJSON(List<Map<String, String>> images) {
            JSONObject result = new JSONObject();
            JSONArray imgs = new JSONArray();
            for (Map<String, String> image : images) {
                JSONObject img = new JSONObject(image);
                imgs.put(img);
            }
            result.put("result", imgs);
            return result;
        }

	/**
	 * OPERACIONES DEL SERVICIO REST
	 */
	/**
	 * POST method to login in the application
	 *
	 * @param username
	 * @param password
	 * @return
	 * @throws java.sql.SQLException
	 */
	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("username") String username,
			      @FormParam("password") String password) throws SQLException {

		if (dbc == null) dbc = new dbConnector();
		String db_response;	
		Response.Status ret_status;
		if (dbc.checkUserPass(username, password)) {
			ret_status = Response.Status.OK;
			db_response = "{\"message\": \"authentication success\"}";
		}
		else {
			ret_status = Response.Status.UNAUTHORIZED;
			db_response = "{\"message\": \"authentication failed\"}";
		}
                		
		// https://www.baeldung.com/jax-rs-response
		return Response
			.status(ret_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
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
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	// Changes in request mediatype and params for image upload implementation
	// https://stackoverflow.com/questions/56954122/multiple-files-upload-in-a-rest-service-using-jersey
	public Response registerImage (@FormDataParam("title") String title,
				       @FormDataParam("description") String description,
				       @FormDataParam("keywords") String keywords,
				       @FormDataParam("author") String author,
				       @FormDataParam("creator") String creator,
				       @FormDataParam("capture") String capt_date,
				       @FormDataParam("filename") String filename,
				       @FormDataParam("file") InputStream file_input) { 
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String db_response;
		try {
                        if (!dbc.userExists(creator)) throw new Exception("User "+creator+" doesn't exists!");
                        
			String fileExtension = "";
			Integer extensionStart = filename.lastIndexOf('.');
			if (extensionStart != -1) {
				fileExtension = filename.substring(extensionStart);
			}

			// Read & store file content
			UUID uuid = UUID.randomUUID();
			String newFilename = uuid.toString() + fileExtension;
			File dest = new File(imgsPath, newFilename);
			OutputStream img_output = new FileOutputStream(dest);

			int read = 0;
			final byte[] bytes = new byte[1024];
			while ((read = file_input.read(bytes)) != -1) {
				img_output.write(bytes, 0, read);
			}

			System.out.println("Succesfully saved image: " + newFilename + " to disk!");

			dbc.uploadImage(title, description, keywords, author, creator, capt_date, newFilename);

			System.out.println("Succesfully uploaded image to database!");

			return_status = Response.Status.OK;
			db_response = "{\"message\": \"image uploaded successfully\"}";
		
		} catch (FileNotFoundException fne) {
			System.err.println("[FileNotFoundException] File not found: " + fne.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to upload image\"}";
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to upload image\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to upload image\"}";
		}
		return Response
			.status(return_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
	}

	/**
	* GET method to download an image by ID
	* @param id
	* @return
	*/
	@Path("imgContent/{id}")
	@GET
	@Produces({"image/png", "image/jpeg"})
	public Response imgContentByID(@PathParam("id") int id) {
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String content_type;
		String content_response;
                System.out.println("Petition to server image with ID: "+id);
		try {
			String filename = dbc.getFilename(id);
			File imgFile = new File(imgsPath, ("/" + filename));
			if (!filename.equals("") && imgFile.exists()) {
				FileInputStream inputStream = new FileInputStream(imgFile); 
				ByteArrayOutputStream imgStream = new ByteArrayOutputStream();

				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					imgStream.write(buffer, 0, bytesRead);
				}
				
				String fileExtension = "";
				Integer extensionStart = filename.lastIndexOf('.');
				if (extensionStart != -1) {
					fileExtension = filename.substring(extensionStart);
				} else {
					fileExtension = "*";
				}
				
				return Response
					.status(Response.Status.OK)
					.entity(imgStream.toByteArray())
					.type("image/" + fileExtension)
					.build();
			} else {
                            System.err.println("[FileNotFound]: Error on finding the file");
                            return_status = Response.Status.INTERNAL_SERVER_ERROR;
                            content_type = MediaType.APPLICATION_JSON;
                            content_response = "{\"message\": \"failed to deliver image\"}";
			}
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			content_type = MediaType.APPLICATION_JSON;
			content_response = "{\"message\": \"failed to deliver image\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			content_type = MediaType.APPLICATION_JSON;
			content_response = "{\"message\": \"failed to deliver image\"}";
		}
		return Response
			.status(return_status)
			.entity(content_response)
			.type(content_type)
			.build();
	}

	/**
	* POST method to modify an existing image
	* @param id
	* @param title
	* @param description
	* @param keywords
	* @param author
	* @param username, used for checking image ownership
	* @param capt_date
	* @return
	*/
	@Path("modify")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)

	public Response modifyImage(@FormParam("id") String id,
				    @FormParam("title") String title,
				    @FormParam("description") String description,
				    @FormParam("keywords") String keywords,
				    @FormParam("author") String author,
				    @FormParam("creator") String username,
				    @FormParam("capture") String capt_date) {
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String db_response;
		try {
			boolean result = dbc.modifyImage(Integer.valueOf(id), title, description, keywords, author, capt_date, username);
			if (result) {
				return_status = Response.Status.OK;
				db_response = "{\"message\": \"successfully modified image\"}";
			} else {
				return_status = Response.Status.FORBIDDEN;
				db_response = "{\"message\": \"you cannot modify another user images!\"}";
			}
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to modify image\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to modify image\"}";
		}
		return Response
			.status(return_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
	}

	/**
	* POST method to delete an existing image
	* @param id
	* @param username
	* @return
	*/
	@Path("delete")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)

	public Response deleteImage (@FormParam("id") String id,
				     @FormParam("creator") String username) {
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String db_response;
		try {
			boolean result = dbc.deleteImage(Integer.valueOf(id), username);
			if (result) {
				return_status = Response.Status.OK;
				db_response = "{\"message\": \"successfully deleted image\"}";
			} else {
				return_status = Response.Status.FORBIDDEN;
				db_response = "{\"message\": \"you cannot delete another user images!\"}";
			}
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to delete image\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to delete image\"}";
		}
		return Response
			.status(return_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
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
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String db_response;
		try {
			List<Map<String,String>> results = dbc.searchImage(null, null, null, null, null, id);
			return_status = Response.Status.OK;
			db_response = convertListToJSON(results).toString(); // TODO: Change format to json (DONE)
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by id\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by id\"}";
		}
		return Response
			.status(return_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
	}
	
	/**
	* GET method to search images by title
	* @param title
	* @return
	*/
	@Path("searchTitle/{title}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)

	public Response searchByTitle(@PathParam("title") String title) {
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String db_response;
		try {
			List<Map<String,String>> results = dbc.searchImage(title, null, null, null, null, null);
			return_status = Response.Status.OK;
			db_response = convertListToJSON(results).toString(); // TODO: Change format to json (DONE)
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by title\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by title\"}";
		}
		return Response
			.status(return_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
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
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String db_response;
		try {
			List<Map<String,String>> results = dbc.searchImage(null, null, null, null, date, null);
			return_status = Response.Status.OK;
			db_response = convertListToJSON(results).toString(); // TODO: Change format to json (DONE)
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by creation date\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by creation date\"}";
		}
		return Response
			.status(return_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
	}

	/**
	* GET method to search images by author
	* @param author
	* @return
	*/
	@Path("searchAuthor/{author}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)

	public Response searchByAuthor(@PathParam("author") String author) {
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String db_response;
		try {
			List<Map<String,String>> results = dbc.searchImage(null, null, author, null, null, null);
			return_status = Response.Status.OK;
			db_response = convertListToJSON(results).toString(); // TODO: Change format to json (DONE)
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by author\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by author\"}";
		}
		return Response
			.status(return_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
	}	

	/**
	* GET method to search images by keyword
	* @param keywords
	* @return
	*/
	@Path("searchKeywords/{keywords}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)

	public Response searchByKeywords(@PathParam("keywords") String keywords) {	
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String db_response;
		try {
			List<Map<String,String>> results = dbc.searchImage(null, keywords, null, null, null, null);
			return_status = Response.Status.OK;
			db_response = convertListToJSON(results).toString(); // TODO: Change format to json (DONE)
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by keywords\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image by keywords\"}";
		}
		return Response
			.status(return_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
	}
        
        
        ////////////////////////////////////////////////////////////////
        //              OPTIONAL ENDPOIN GENERAL SEARCH               //
        ////////////////////////////////////////////////////////////////
        
        /**
	* POST method to search images by multiple parameters
	* @param id
        * @param title
	* @param keywords
	* @param author
        * @param creator
	* @param date
	* @return
	*/
	@Path("search")
	@POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)

	public Response search(@FormParam("id") String id,
                               @FormParam("title") String title,
                               @FormParam("keywords") String keywords,
                               @FormParam("author") String author,
                               @FormParam("creator") String creator,
                               @FormParam("date") String date) {	
		if (dbc == null) dbc = new dbConnector();
		Response.Status return_status;
		String db_response;
		try {
                        Integer idTemp = null;
                        if (id != null && !id.isEmpty()) idTemp = Integer.valueOf(id);
			List<Map<String,String>> results = dbc.searchImage(title, keywords, author, creator, date, idTemp);
			return_status = Response.Status.OK;
			db_response = convertListToJSON(results).toString(); // TODO: Change format to json (DONE)
		} catch (SQLException e) {
			System.err.println("[SQLException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image\"}";
		} catch (Exception e) {
			System.err.println("[GenericException]: " + e.getMessage());
			return_status = Response.Status.INTERNAL_SERVER_ERROR;
			db_response = "{\"message\": \"failed to search image\"}";
		}
		return Response
			.status(return_status)
			.entity(db_response)
			.type(MediaType.APPLICATION_JSON)
			.build();
	}
}
