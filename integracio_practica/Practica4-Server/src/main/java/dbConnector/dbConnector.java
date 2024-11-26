/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbConnector;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alumne
 */
@WebServlet(name = "dbConnector", urlPatterns = {"/dbConnector"})
public class dbConnector extends HttpServlet {
    Connection connection;
    
    protected void openConnection() throws SQLException {
        if (connection == null) {
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
            } catch (ClassNotFoundException e) {
                System.err.println("ERROR ON OPENING CONNECTION TO DB: "+e.getMessage());
            } finally {
                connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            }           
        }
    }
    
    protected void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
    
    public String getFilename(Integer id) throws SQLException {
    	// Returns filename corresponding to given id
	openConnection();	

	String query = "SELECT * FROM image WHERE image.id = ?";
	PreparedStatement statement = connection.prepareStatement(query);
	statement.setInt(1, id);
	ResultSet rs = statement.executeQuery();

	String filename = ""; 
	if (rs.next()) {
		filename = rs.getString("filename");
	}
	
	closeConnection();
	return filename;
    }
    
    public boolean checkUserPass(String username, String password) throws SQLException {
        openConnection();
        //createDBTables(); // Canviar despres a veure on ho posem xd
        // Check that username and password are present in the DB

        // TODO: Check user input to avoid sql injection
        String query = "SELECT * FROM usuarios WHERE id_usuario = ? AND password = ?";  
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) return true;

        closeConnection();
            
        return false;
    }
    
    public boolean userExists(String username) throws SQLException {
        openConnection();
        
        String query = "SELECT * FROM usuarios WHERE id_usuario = ?";  
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();

        boolean returnValue = rs.next();
        
        closeConnection();
        
        System.out.println(returnValue);
        return returnValue;
    }
    
    public void uploadImage(String title, String desc, String keywords, String author, String creator, String creation_date, String filename) throws SQLException {
        openConnection();
        String query;
        PreparedStatement statement;

        query = "INSERT INTO IMAGE (TITLE, DESCRIPTION, KEYWORDS, AUTHOR, CREATOR, CAPTURE_DATE, STORAGE_DATE, FILENAME) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        statement = connection.prepareStatement(query);            
        statement.setString(1, title);
        statement.setString(2, desc);
        statement.setString(3, keywords);
        statement.setString(4, author);
        statement.setString(5, creator);
        statement.setString(6, creation_date);            
        statement.setString(7, LocalDate.now().toString());
        statement.setString(8, filename);            
        statement.executeUpdate();

        closeConnection();
    }
    
    public List<Map<String, String>> searchImage(String title, String keywords, String author, String creator, String date, Integer id) throws SQLException {
	// TODO: Need to implement search by ID and search by DATE!!!
        List<Map<String, String>> results = new ArrayList<>();
        
        openConnection();
        String query;
        PreparedStatement statement;
        
	query = "SELECT * FROM image WHERE "
                + "image.title LIKE ? AND "
                + "image.keywords LIKE ? AND "
                + "image.author LIKE ? AND "
                + "image.creator LIKE ? AND "
                + "image.capture_date LIKE ? AND "
                + "(? IS NULL OR image.id = ?)";

        statement = connection.prepareStatement(query);

        if (title == null || title.isEmpty()) statement.setString(1, "%");
        else statement.setString(1, title);
	
	if (keywords == null || keywords.isEmpty()) statement.setString(2, "%");
        else statement.setString(2, "%" + keywords + "%"); // This way a single keyword match can get to the desired image

        if (author == null || author.isEmpty()) statement.setString(3, "%");
        else statement.setString(3, author);

        if (creator == null || creator.isEmpty()) statement.setString(4, "%");
        else statement.setString(4, creator);

        if (date == null || date.isEmpty()) statement.setString(5, "%");
        else statement.setString(5, date);

	if (id == null) {
		statement.setNull(6, java.sql.Types.VARCHAR); // Maybe change VARCHAR for INTEGER???
		statement.setNull(7, java.sql.Types.VARCHAR);
	} else {
		statement.setInt(6, id);
		statement.setInt(7, id);
	}

	ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Map<String, String> row = new HashMap<>();
            Integer aux_id = rs.getInt("id");

            row.put("id", aux_id.toString());
            row.put("title", rs.getString("TITLE"));
            row.put("description", rs.getString("DESCRIPTION"));
            row.put("keywords", rs.getString("KEYWORDS"));
            row.put("author", rs.getString("AUTHOR"));
            row.put("creator", rs.getString("CREATOR"));
            row.put("capture_date", rs.getString("CAPTURE_DATE"));
            row.put("storage_date", rs.getString("STORAGE_DATE"));
            row.put("filename", rs.getString("FILENAME"));

            results.add(row);
        }
        System.out.println("Resultats de la cerca collected!"); 

        rs.close();

        closeConnection();
        
        return results;
    }
    
    // Tots els paràmetres que es poden modificar d'una imatge
    public boolean modifyImage(Integer id, String title, String desc, String keywords, String author, String creation_date, String username) throws SQLException {
        openConnection();
        String query;
        PreparedStatement statement;

        query = "SELECT * FROM image WHERE image.id = ?";
	statement = connection.prepareStatement(query);
	statement.setInt(1, id);
	ResultSet rs = statement.executeQuery();
        Boolean notEmpty = rs.next();

	if (notEmpty && rs.getString("CREATOR").equals(username)) {
		// First step: Get original values
		query = "SELECT * FROM image WHERE image.id = ?";
		statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		rs = statement.executeQuery();
		rs.next();
		System.out.println("Information of image recollected!");

		// Second step: Update columns with new value or original value, depending on user input	    
		query = "UPDATE image SET image.title = ?, image.description = ?, image.keywords = ?, image.author = ?, image.capture_date = ? WHERE image.id = ?";
		statement = connection.prepareStatement(query);

		if (title == null || title.isEmpty()) statement.setString(1, rs.getString("title"));
		else statement.setString(1, title);
		if (desc == null || desc.isEmpty()) statement.setString(2, rs.getString("description"));
		else statement.setString(2, desc);
		if (keywords == null || keywords.isEmpty()) statement.setString(3, rs.getString("keywords"));
		else statement.setString(3, keywords);
		if (author == null || author.isEmpty()) statement.setString(4, rs.getString("author"));
		else statement.setString(4, author);
		if (creation_date == null || creation_date.isEmpty()) statement.setString(5, rs.getString("capture_date"));
		else statement.setString(5, creation_date);
		statement.setInt(6, id);
		statement.executeUpdate();
		System.out.println("Information of image updated!");
		closeConnection();
		return true;
	}
	closeConnection();
	return false;
    }
    
    public boolean deleteImage(Integer id, String username) throws SQLException {
        openConnection();
        String query;
        PreparedStatement statement;

        query = "SELECT * FROM image WHERE image.id = ?";
	statement = connection.prepareStatement(query);
	statement.setInt(1, id);
	ResultSet rs = statement.executeQuery();
        Boolean notEmpty = rs.next();

	if (notEmpty && rs.getString("CREATOR").equals(username)) {
		query = "DELETE FROM image WHERE image.id = ?";
		statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.executeUpdate();
        	closeConnection();
		return true;
	}
        closeConnection();
	return false;
    }
    
    /*
    
    //////////// NOT USED ANYMORE -- ONLY AS EXAMPLES ////////////
    
    public void createDBTables() throws java.sql.SQLException {
        String query;
        PreparedStatement statement;
        // Delete existing table. Comment this line if you have already created and filled it

        query = "drop table image";
        statement = connection.prepareStatement(query);
        statement.setQueryTimeout(30);  // set timeout to 30 sec.                      
        statement.executeUpdate();

        // Delete existing table. Comment this line if you have already created and filled it          
        query = "drop table usuarios"; 
        statement = connection.prepareStatement(query);
        statement.executeUpdate();
        
        // fill parameters for prepared statement            
        // create and fill table usuarios            
        query = "create table usuarios (id_usuario varchar (256) primary key, password varchar (256))";
        statement = connection.prepareStatement(query);                        
        statement.executeUpdate();

        query = "insert into usuarios values(?,?)";
        statement = connection.prepareStatement(query);    
        statement.setString(1, "test");
        statement.setString(2, "test");
        statement.executeUpdate();
        
        query = "insert into usuarios values(?,?)";
        statement = connection.prepareStatement(query);    
        statement.setString(1, "Silvia");
        statement.setString(2, "12345");
        statement.executeUpdate();

        query = "insert into usuarios values(?,?)";
        statement = connection.prepareStatement(query);
        statement.setString(1, "Pepito");
        statement.setString(2, "23456");                                    
        statement.executeUpdate();

        query = "create table image (id int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                + "title varchar (256) NOT NULL, description varchar (1024) NOT NULL, keywords "
                + "varchar (256) NOT NULL, author varchar (256) NOT NULL, creator varchar (256) NOT NULL, "
                + "capture_date varchar (10) NOT NULL, storage_date varchar (10) NOT NULL, filename varchar (512) NOT NULL, "
                + "primary key (id), foreign key (creator) references usuarios(id_usuario))";

        statement = connection.prepareStatement(query);
        statement.executeUpdate();

        // With preparedStatement, SQL Injection and other problems when inserting values in the database can be avoided
    
        query = "INSERT INTO IMAGE (TITLE, DESCRIPTION, KEYWORDS, AUTHOR, CREATOR, CAPTURE_DATE, STORAGE_DATE, FILENAME) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        statement = connection.prepareStatement(query);           
        statement.setString(1, "test1");
        statement.setString(2, "This is image 1");
        statement.setString(3, "Keyword11, Keyword12");
        statement.setString(4, "Silvia");
        statement.setString(5, "Silvia");
        statement.setString(6, "2020/03/02");
        statement.setString(7, "2020/09/17");
        statement.setString(8, "file1.jpg");            
        statement.executeUpdate();      

        query = "INSERT INTO IMAGE (TITLE, DESCRIPTION, KEYWORDS, AUTHOR, CREATOR, CAPTURE_DATE, STORAGE_DATE, FILENAME) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        statement = connection.prepareStatement(query);            
        statement.setString(1, "test2");
        statement.setString(2, "This is image 2");
        statement.setString(3, "Keyword21, Keyword22");
        statement.setString(4, "Silvia");
        statement.setString(5, "Silvia");
        statement.setString(6, "2019/03/02");
        statement.setString(7, "2020/09/17");
        statement.setString(8, "file2.jpg");            
        statement.executeUpdate();
    }*/
}
