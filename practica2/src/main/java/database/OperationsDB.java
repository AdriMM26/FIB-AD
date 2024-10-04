/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.http.Part;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alumne
 */
public class OperationsDB {
     /* Return true if user {username, password} exists in DB, false otherwise */
    public static boolean check_user (String username, String password, Connection connection){
        try{
            String query = "select * from usuarios where id_usuario=? and password=?";
            /* Prepare: An SQL statement template is created and sent to the database. Certain values are left unspecified, called parameters (labeled "?") */
            PreparedStatement statement = connection.prepareStatement(query);
            
            /* set the correct values */
            statement.setString(1, username);
            statement.setString(2, password);
            
            /* Execute the query */
            ResultSet rs = statement.executeQuery();
            
            return rs.next();
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }
    
    public static int register_user (String username, String password, Connection connection) {
        try{
            String query = "insert into usuarios(id_usuario, password) values (?,?)";
            /* Prepare: An SQL statement template is created and sent to the database. Certain values are left unspecified, called parameters (labeled "?") */
            PreparedStatement statement = connection.prepareStatement(query);
        
            /* set the correct values */
            statement.setString(1, username);
            statement.setString(2, password);
            
            statement.executeUpdate();
            return 1;
        } catch(SQLException e){
            System.err.println(e.getMessage());
            if(e.getErrorCode() == 3000) return 0;
            else return -1;
        }
    }
    
    public static int upload_image (String title, String description, String keywords, String author, String creator, String creationDate, String uploadDate, Part filepart, Connection connection){
        try{
            String filename = Paths.get(filepart.getSubmittedFileName()).getFileName().toString();

            String query = "insert into image(title, description, keywords, author, creator, capture_date, storage_date, filename) values (?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, keywords);
            statement.setString(4, author);
            statement.setString(5, creator);
            statement.setString(6, creationDate);
            statement.setString(7, uploadDate);
            statement.setString(8, filename);
            
            statement.executeUpdate();
            
            return get_max_id(connection);
            
        } catch (SQLException ex) {
            return -1;
        }
    }
    
    public static void delete_image(Integer insertID, Connection connection){
        try{
            String query = "DELETE From IMAGE where ID=?";
            PreparedStatement statement = connection.prepareStatement(query);
        
            /* set the correct values */
            statement.setString(1, insertID.toString());
            statement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(OperationsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static int get_max_id(Connection connection) {
        try{
            String query = "SELECT MAX(ID) From IMAGE";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            int id;
            if(rs.next()) {
                id = rs.getInt(1);
            }
            else id = 1;
 
            return id;
            
        } catch (SQLException ex) {
            Logger.getLogger(OperationsDB.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    } 
    
    public static List<String[]> getAllImages() {
        try {
         /* Open connection */
         Connection connection = ConnectDB.open_connection();
         
         List<String[]> imageList = new ArrayList<>();
           
         /* SQL query */
           String query;
           PreparedStatement statement;
           
           query = "SELECT * FROM IMAGE";
           statement = connection.prepareStatement(query);
           ResultSet rs = statement.executeQuery();
           
           
           while (rs.next()) {
             String[] imageInfo = new String[9];
             for (int i = 0; i < 9; ++i) { imageInfo[i] = rs.getString(i+1); }
             imageList.add(imageInfo);
           }
           
           /* Close connection */
           ConnectDB.close_connection(connection);
           
           return imageList;
           
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
