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
            System.out.println(e.getMessage());
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
    
    public boolean insertInto(String db_name,String table_name,String param1, String param2, String param3,int param4)
    {
        String sql;
        boolean notes = db_name.equals("notes.db");
        try
        {
            Connection conn = getDatabase(db_name,notes);
            Statement stm = conn.createStatement();
            if(notes)
            {
            if(table_name.equals("categories"))
                sql = "insert into "+table_name+" values ('"+param1+"', '"+param2+"', '"+param3+"', '"+param4+"')";
            else
                sql = "insert into "+table_name+" values ('"+param1+"', '"+param2+"', '"+param3+"')";
            }
            else
                sql = "insert into "+table_name+" values ('"+param1+"', '"+param2+"')";
            stm.executeUpdate(sql);
            stm.close();
            conn.close();
        }  
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        return true;
    }
    
    public ResultSet selectFrom(String db_name, String table_name, String param1, String param2, String param3)
    {
        String sql;
        ResultSet results = null;
        boolean notes = db_name.equals("notes.db");
        try
        {
            Connection conn = getDatabase(db_name,notes);
            if(notes)
            {
                sql = "select * from "+table_name+" where "+param1+" "+param2+" "+param3;
            }
            else
                sql = "select * from "+table_name+" where "+param1+" "+param2;
            Statement stm = conn.createStatement();
            results = stm.executeQuery(sql);
            
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return results;
    }
    
    public void deleteFrom(String db_name, String table_name, String hash)
    {
        String sql = "delete from "+table_name+" where hash_id = '"+hash+"'";
        boolean notes = db_name.equals("notes.db");
        try
        {
            Connection conn = getDatabase(db_name,notes);
            Statement stm = conn.createStatement();
            stm.executeUpdate(sql);
            stm.close();
            conn.close();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
