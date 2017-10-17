/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notebook;
import java.sql.*;
import java.util.Scanner;
import java.io.*;
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
    private Connection userdb;
    public boolean logged_in = false;
    public int id;
    
    Users(String username, String password)
    {
        this.input_username = username;
        this.input_password = password;
        this.username = username;
        this.password = password;
        authenticate();
    }
    
    public boolean authenticate()
    {
        Scanner sc = new Scanner(System.in);
        try
        {
        conn = getDatabase("users.db",false);
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("There seems to be an error!"+" "+e.getMessage());
        }
        try
        {
            Statement auth = conn.createStatement();
            ResultSet username_exists = auth.executeQuery("SELECT * FROM all_users WHERE username = \""+this.username+ "\"");
            if(username_exists.next())
            {
                ResultSet user_password = auth.executeQuery("SELECT * FROM all_users WHERE username = \""+this.username+ "\"");
                if((this.password).equals(user_password.getString("passw")))
                {
                    System.out.println("Successfully logged in!");
                    userdb = getDatabase(this.username+".db",true);
                    logged_in = true;
                }
                else
                    System.out.println("Username exists but password doesn't match. Create new user with a different username.");
            }
            else
            {
                System.out.println("Add user(y/n):");
                String in = sc.next();
                in = in.toLowerCase();
                if(in.equals("y"))
                {
                    try
                    {
                    Statement add = conn.createStatement();
                    auth.executeUpdate("insert into all_users values('"+this.username+"' ,'"+this.password+"')");
                    System.out.println("Successfully added new user.");
                    }
                    catch(SQLException e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
                else
                    System.out.println("Not quite my input!");
            }
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        catch(ClassNotFoundException c)
        {
            c.printStackTrace();
        }
        return true;
    }
    
    public void addNote(String content)
    {
        Note note = new Note(this);
        note.createNote(content);
        try
        {
            String hash = note.generateHash();
            FileOutputStream obj_file = new FileOutputStream("data/"+hash);
            ObjectOutputStream out = new ObjectOutputStream(obj_file);
            out.writeObject(note);
            out.close();
            System.out.println("New file created.");
            obj_file.close();
        }
        catch(IOException p)
        {
            p.printStackTrace();
        }
    }
}
