/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
