package notebook;
import java.sql.*;
import java.util.Scanner;
import java.io.*;
/**
 *
 * @author C.V.Hariharan
 */
public class Users extends DatabaseHandler implements Serializable{
    private String input_username;
    private String input_password;
    private String username;
    private String password;
    private String plain_password;
    private Connection conn;
    private Connection notesdb;
    public boolean logged_in = false;
    public int id;
    public boolean new_user = false;
    Users(String username, String password)
    {
        this.input_username = username;
        this.input_password = password;
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
            ResultSet username_exists = selectFrom("users.db","all_users","username = '"+this.input_username+"'","","");//auth.executeQuery("SELECT * FROM all_users WHERE username = \""+this.username+ "\"");
            if(username_exists.next())
            {
                if(BCrypt.checkpw(this.input_password,username_exists.getString("passw")))
                {
                    System.out.println("Successfully logged in!");
                    notesdb = getDatabase("notes.db",true);
                    logged_in = true;
                    this.username = this.input_username;
                    this.password = this.input_password;
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
                    insertInto("users.db","all_users",this.input_username,BCrypt.hashpw(this.input_password, BCrypt.gensalt()),"",0);
                    System.out.println("Successfully added new user.");
                    this.new_user = true;
                }
                else
                    System.out.println("Not quite my input!");
            }
        }
        catch(SQLException s)
        {
            System.out.println(s.getMessage());
        }
        catch(ClassNotFoundException c)
        {
            System.out.println(c.getMessage());
        }
        return true;
    }
    
    public void addNote(String content, String title)
    {
        Note note = new Note(this.username,title);
        note.createNote(content);
        note.title = title;
        String hash = note.hash;
        writeToFile(note);
        System.out.println("Note created.");
        insertInto("notes.db","notes",hash,title,this.username,0);
           
        
    }
    
    public String addTodo(String content,String title,String file_hash,String category,boolean existing)
    {
        String hash = null;
        if(!existing)
        {
            try
            {
                Connection notesdb = getDatabase("notes.db",true);
                ToDo todo = new ToDo(this.username,title);
                todo.createNote(content);
                todo.addCategory(category);
                hash = todo.hash;
                System.out.println("Created a Todo with hash: "+hash);
                insertInto("notes.db","todo",hash,title,this.username,0);
                writeToFile(todo);
            }
            catch(ClassNotFoundException e)
            {
                System.out.println(e.getMessage());
            }
            
        }
        else
        {
                ToDo todo = (ToDo) readFromFile(file_hash,1);
                todo.createNote(content);
                todo.returnContent();
                hash = todo.hash;
                writeToFile(todo);
            
        }
        return hash;
    }
    
    public void delete(String hash)
    {
        String table_name = noteOrTodo(hash);
        deleteFrom("notes.db",table_name,hash);
        File file = new File("data/"+hash);
        file.delete();
        deleteFrom("notes.db","categories",hash);
    }
    
    public String noteOrTodo(String hash)
    {
        //Returns the table name based on the hash type ie: note or todo
        String table_name = null;
        ResultSet result = selectFrom("notes.db","categories","hash_id = '"+hash+"'"," and owner = '"+this.username+"'","");
        try
        {
            String temp = result.getString("note");
            int note = Integer.parseInt(temp);
            table_name = (note==0)?"notes":"todo";
            result.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        return table_name;
    }
    
    public void show(String hash)
    {
        String type = noteOrTodo(hash);
        int type_no = type.equals("todo")?1:0;
        Note note = null;
        note = readFromFile(hash,type_no);
        System.out.println("-------------------------------------------------");
        System.out.println(note.title.toUpperCase());
        System.out.println("-------------------------------------------------");
        System.out.print(note.returnContent());
        System.out.println("Hash: "+note.hash);
        System.out.println("-------------------------------------------------");
    }
    public void deleteTask(String hash)
    {
        String type = noteOrTodo(hash);
        FileInputStream obj_file;
        ObjectInputStream in;
        if(type.equals("todo"))
        {
                ToDo todo = (ToDo) readFromFile(hash,1);
                todo.returnContent();
                System.out.println("Index: ");
                Scanner sc = new Scanner(System.in);
                int index = sc.nextInt();
                todo.deleteNote(index-1);
                writeToFile(todo);
            
        }
    }
   
    public void showAll(String category,int type) //0-Note,1-Todo
    {
        String table_name = (type==1)?"todo":"notes"; 
        String sql = "select * from "+table_name+" where owner = '"+this.username+"'";
        if(!category.equals("*"))
        {
            sql = "select * from categories where category = '"+category+"'"+" and owner = '"+this.username+"' and note = '"+type+"'";
        }
        
        int i = 1;
        try
        {
            Connection notesdb = getDatabase("notes.db",true);
            Statement stm = notesdb.createStatement();
            ResultSet results = stm.executeQuery(sql);
            
            Note note = null;
            while(results.next())
            {
                note = readFromFile(results.getString("hash_id"),type);
                System.out.println("-------------------------------------------------");
                System.out.println(i+"."+note.title.toUpperCase());
                System.out.println("-------------------------------------------------");
                System.out.print(note.returnContent());
                System.out.println("Hash: "+note.hash);
                System.out.println("-------------------------------------------------");
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
    }
    
    public Note readFromFile(String hash,int type)
    {
        Note note = null;
        try
        {
            FileInputStream obj_file = new FileInputStream("data/"+hash);
            ObjectInputStream in = new ObjectInputStream(obj_file);
            if(type == 1)
                note = (ToDo) in.readObject();
            else if(type == 0)
                note = (Note) in.readObject();
            in.close();
            obj_file.close();
        }
        catch(ClassNotFoundException e)
        {
                System.out.println(e.getMessage());
        }
        catch(FileNotFoundException e)
        {
                System.out.println(e.getMessage());
        }
        catch(IOException e)
        {
                System.out.println(e.getMessage());
        }
        return note;
    }
    public void writeToFile(Note note)
    {
        String hash = note.hash;
        try
        {
        FileOutputStream obj_file = new FileOutputStream("data/"+hash);
        ObjectOutputStream out = new ObjectOutputStream(obj_file);
        out.writeObject(note);
        out.close();
        obj_file.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void editNote(String hash)
    {
        String type = noteOrTodo(hash);
        Scanner sc = new Scanner(System.in);
        String content = "";
        if(type.equals("notes"))
        {
            Note note = readFromFile(hash,0);
            System.out.println("Content: ");
            content = sc.next()+sc.nextLine();
            System.out.println("1.APPEND\n2.OVERWRITE");
            int choice = sc.nextInt();
            if(choice == 1)
                note.appendNote(content);
            else if(choice == 2)
                note.createNote(content);
            else
                System.out.println("Invalid Input!");
            writeToFile(note);
        }
    }
    
    public void getHash(String title,int type)//0-Note 1-Todo
    {
        String table_name = (type==0)?"notes":"todo";
        String hash = "";
        ResultSet r = selectFrom("notes.db",table_name," title like '%"+title+"%'"," and owner = '"+this.username+"'","");
        try
        {
           while(r.next())
           {
               hash = r.getString("hash_id");
               show(hash);
           }
        }
        catch(SQLException e)
        {
            System.out.println("No results...");
            System.out.println(e.getMessage());
        }
    }
}
