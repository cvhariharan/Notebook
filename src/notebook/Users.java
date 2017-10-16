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
public class Users extends DatabaseHandler{
    private String input_username;
    private String input_password;
    private String username;
    private String password;
    private Connection conn;
    public int id;
    Users(String username, String password)
    {
        this.input_username = username;
        this.input_password = password;
        authenticate();
    }
    
    public boolean authenticate()
    {
        try
        {
        conn = getDatabase("users.db");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("There seems to be an error!"+" "+e.getMessage());
        }
        try
        {
            Statement auth = conn.createStatement();
            ResultSet results = auth.executeQuery("SELECT * FROM user WHERE username = "+this.username+ " password = "+this.password);
            if(results.next())
            {
                System.out.println("User exists");
            }
        }
        catch(SQLException s)
        {
            
        }
        return true;
    }
}
