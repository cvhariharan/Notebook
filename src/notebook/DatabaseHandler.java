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
            Connection conn = DriverManager.getConnection(url);
            if(conn!= null)
            {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database: "+meta.getDriverName());
                System.out.println("Database successfully created!");
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        createTable();
        return conn;
    }
    
    public void createTable()
    {
        String table_create = "CREATE TABLE IF NOT EXISTS users (note text, created_at datetime)";
        
        try
        {
        Statement create_table = conn.createStatement();
        if(create_table.execute(table_create))
            System.err.println("Successfully created users table.");
        else
            System.out.println("Table found");
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
}
