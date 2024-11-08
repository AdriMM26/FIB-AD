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
    
    public static boolean check_user (String username, String password, Connection connection){
        try{
            String query = "select * from usuarios where id_usuario=? and password=?";
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, username);
            statement.setString(2, password);
            
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
            PreparedStatement statement = connection.prepareStatement(query);
        
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
    
    public static int upload_image (String title, String description, String keywords, String author, String creator, String creationDate, String uploadDate, Connection connection){
        try{
            String filename = "No file upload";

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
    
    public static void delete_image(String insertID, Connection connection){
        try{
            String query = "delete from image where id=?";
            PreparedStatement statement = connection.prepareStatement(query);
        
            statement.setString(1, insertID);
            statement.executeUpdate();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(OperationsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean modify_image(String insertID, String title, String description, String keywords, String author, String creationDate, Connection connection) {
        try {
            
            String query = "update image set title =?, description =?, keywords =?, author =?, capture_date =? where id =?";
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, title);
            statement.setString(2,description);
            statement.setString(3, keywords);
            statement.setString(4, author);
            statement.setString(5, creationDate);
            statement.setString(6, insertID);
            
            statement.executeUpdate();
            
            return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(OperationsDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static List<String[]> get_images(String insertID, String title,String description,String keywords,String author,String creationDate,Connection connection) {
        try{
            
            List<String[]> images = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            
            String query = "select * from image where 1=1";
            PreparedStatement statement;
            
            if(!insertID.isBlank()) {
                query += " and id like ?";
                values.add("%" + insertID + "%");
            }
            if(!title.isBlank()) {
                query += " and title like ?";
                values.add("%" + title + "%");
            }
            if(!description.isBlank()) {
                query += " and description like ?";
                values.add( "%" + description + "%");
            }
            if(!keywords.isBlank()) {
                query += " and keywords like ?";
                values.add("%" + keywords + "%");
            }
            if(!author.isBlank()) {
                query += " and author like ?";
                values.add("%" + author + "%");
            }
            if(!creationDate.isBlank()) {
                query += " and capture_date like ?";
                values.add("%" + creationDate + "%");
            }
            
            statement = connection.prepareStatement(query);
            for(int i = 0; i < values.size(); ++i) {
                statement.setObject(i+1, values.get(i));
            }
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                String[] imageInfo = new String[9];
                for (int i = 0; i < 9; ++i) { imageInfo[i] = rs.getString(i+1); }
                images.add(imageInfo);
            }

            return images;
            
        } catch (SQLException ex) {
            Logger.getLogger(OperationsDB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static boolean is_user_image (Connection connection, String insertId, String creator) {
        try{
            String query = "select*from image where id = ? and creator = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, insertId);
            statement.setString(2, creator);
            
            ResultSet rs = statement.executeQuery();
            
            return rs.next();
            
        } catch (SQLException ex) {
            Logger.getLogger(OperationsDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private static int get_max_id(Connection connection) {
        try{
            String query = "select max(id) from image";
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
