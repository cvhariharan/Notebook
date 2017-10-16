/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notebook;
import java.sql.*;
/**
 *
 * @author thero
 */
public class DatabaseHandler {
    
    private Connection conn;
    public Connection getDatabase(String name) throws ClassNotFoundException
    {
        String url = "jdbc:sqlite:D:/sqlite/" + name;
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            if(conn!=null)
            {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database: "+meta.getDriverName());
                System.out.println("Database successfully created!");
                createTable();
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public void createTable()
    {
        try
        {
            Statement stm = conn.createStatement();
            stm.execute("create table if not exists all_users (username text, passw text)");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
}
