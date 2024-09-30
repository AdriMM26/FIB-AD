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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public static boolean upload_image (String title, String description, String keywords, String author, String creator, String creationDate, String uploadDate, Part filepart, Connection connection){
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
            
            return true;
            
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /*retorna la ultima id de la tabla image (ns si funciona be pero bueno)*/
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
}
