package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author alumne
 */
public class ConnectDB {
    public static Connection open_connection() throws ClassNotFoundException{
        Connection connection = null;
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
        }catch(SQLException e){
            System.err.print(e.getMessage());
        }
        return connection;
    }
    
    public static void close_connection(Connection c){
        try{
            if(c!=null) c.close();
        }catch(SQLException e){
            System.err.print(e.getMessage());
        }
    }
}
