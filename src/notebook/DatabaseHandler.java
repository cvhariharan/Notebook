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
    public Connection getDatabase(String name, boolean notedb) throws ClassNotFoundException
    {
        String url = "jdbc:sqlite:data/" + name;
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            if(conn!=null)
            {
                DatabaseMetaData meta = conn.getMetaData();
                createTable(notedb); //If userdb is true it creates note related tables
            }
            
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    
    public void createTable(boolean notedb)
    {
        try
        {
            Statement stm = conn.createStatement();
            if(!notedb)
                stm.execute("create table if not exists all_users (username text, passw text)");
            else
            {
                stm.execute("create table if not exists notes (hash_id text, title text, owner text)");
                stm.execute("create table if not exists todo (hash_id text, title text, owner text)");
                stm.execute("create table if not exists categories (hash_id text, category text, owner text,note integer)"); //0-Note, 1-Todo
                
            }
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void executeUpdateDb(String sql, String db_name)
    {
        Connection db;
        DatabaseHandler handler = new DatabaseHandler();
        try
        {
            db = handler.getDatabase(db_name,true);
            Statement stm = db.createStatement();
            stm.executeUpdate(sql);
            stm.close();
            db.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        
    }
    
    /*public static ResultSet executeQueryDb(String sql, String db_name)
    {
        Connection db;
        ResultSet results = null;
        DatabaseHandler handler = new DatabaseHandler();
        try
        {
            db = handler.getDatabase(db_name,true);
            Statement stm = db.createStatement();
            results = stm.executeQuery(sql);
            stm.close();
            db.close();
            
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        return results;
    }*/
}
