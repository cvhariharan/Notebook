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
public class Users extends DatabaseHandler implements Serializable{
    private String input_username;
    private String input_password;
    private String username;
    private String password;
    private Connection conn;
    private Connection notesdb;
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
                    notesdb = getDatabase("notes.db",true);
                    logged_in = true;
                }
                else
                    System.out.println("Username exists but password doesn't match. Create new user with a different username.");
                username_exists.close();
                
            }
            else
            {
                System.out.println("Add user(y/n):");
                String in = sc.next();
                in = in.toLowerCase();
                if(in.equals("y"))
                {
                    insertInto("users.db","all_users",this.input_username,this.input_password,"",0);
                    System.out.println("Successfully added new user.");
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
    
    public void addNote(String content, String title)
    {
        Note note = new Note(this.username,title);
        note.createNote(content);
        note.title = title;
        try
        {
            String hash = note.hash;
            FileOutputStream obj_file = new FileOutputStream("data/"+hash);
            ObjectOutputStream out = new ObjectOutputStream(obj_file);
            out.writeObject(note);
            out.close();
            System.out.println("Note created.");
            obj_file.close();
            Statement add_note = notesdb.createStatement();
            add_note.executeUpdate("insert into notes values ('"+hash+"', '"+title+"','"+this.username+"')");
           
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch(IOException p)
        {
            p.printStackTrace();
        }
    }
    
    public void addTodo(String content,String title,String file_hash,String category,boolean existing)
    {
        if(!existing)
        {
            try
            {
                Connection notesdb = getDatabase("notes.db",true);
                ToDo todo = new ToDo(this.username,title);
                todo.createNote(content);
                todo.addCategory(category);
                String hash = todo.hash;
                System.out.println("Created a Todo with hash: "+hash);
                FileOutputStream obj_file = new FileOutputStream("data/"+hash);
                ObjectOutputStream out = new ObjectOutputStream(obj_file);
                Statement stm = notesdb.createStatement();
                stm.executeUpdate("insert into todo values ('"+hash+"', '"+title+"','"+this.username+"')");
                //todo.showTasks();
                stm.close();
                notesdb.close();
                out.writeObject(todo);
                out.close();
                obj_file.close();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
            }
            catch(ClassNotFoundException e)
            {
                System.out.println(e.getMessage());
            }
            
        }
        else
        {
            try
            {
                FileInputStream obj_file = new FileInputStream("data/"+file_hash);
                ObjectInputStream in = new ObjectInputStream(obj_file);
                ToDo todo = (ToDo) in.readObject();
                todo.createNote(content);
                todo.showTasks();
            }
            catch(FileNotFoundException e)
            {
                System.out.println("You sure the ToDo list exists?");
                System.out.println(e.getMessage());
            }
            catch(ClassNotFoundException e)
            {
                System.out.println(e.getMessage());
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
            }
            
        }
    }
    public void showNotes(String category)
    {
        String sql = "select * from notes where owner = '"+this.username+"'";
        if(!category.equals("*"))
        {
            sql = "select * from categories where category = '"+category+"'"+" and owner = '"+this.username+"' and note = '"+0+"'";
        }
        
        int i = 1;
        try
        {
            Connection notesdb = getDatabase("notes.db",true);
            Statement stm = notesdb.createStatement();
            ResultSet results = stm.executeQuery(sql);
            while(results.next())
            {
                FileInputStream obj_file = new FileInputStream("data/"+results.getString("hash_id"));
                ObjectInputStream in = new ObjectInputStream(obj_file);
                Note note = (Note) in.readObject();
                System.out.println(i+"."+note.title.toUpperCase());
                System.out.println(note.returnContent());
                System.out.println("Hash: "+note.hash);
                i++;
            }
            notesdb.close();
            stm.close();
            results.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void showTodos(String category)
    {
        String sql = "select * from todo where owner = '"+this.username+"'";
        if(!category.equals("*"))
        {
            sql = "select * from categories where category = '"+category+"'"+" and owner = '"+this.username+"' and note = '"+1+"'";
        }
        
        int i = 1;
        try
        {
            Connection notesdb = getDatabase("notes.db",true);
            Statement stm = notesdb.createStatement();
            ResultSet results = stm.executeQuery(sql);
            while(results.next())
            {
                FileInputStream obj_file = new FileInputStream("data/"+results.getString("hash_id"));
                ObjectInputStream in = new ObjectInputStream(obj_file);
                ToDo todo = (ToDo) in.readObject();
                System.out.println(i+"."+todo.title.toUpperCase());
                todo.showTasks();
                System.out.println("Hash: "+todo.hash);
                i++;
            }
            notesdb.close();
            stm.close();
            results.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
