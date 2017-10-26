package notebook;
import java.util.Scanner;
import java.io.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
/**
 *
 * @author C.V.Hariharan
 */
public class Notebook {

    static Note note;
    CommandLine cmdline = null;
    static Scanner in = new Scanner(System.in);
    public static void main(String[] args){
        
        Notebook notebook = new Notebook();
        
        System.out.println("Username: ");
        String username = in.next();
        System.out.println("Password: ");
        String password = in.next();
        Users user = new Users(username,password);
        if(user.new_user)
            user = new Users(username,password);
        int input = 0;
        String title;
        String content;
        String hash;
        if(user.logged_in)
        {
            
            try
            {
            notebook.parse(user);
            }
            catch(IOException e)
            {
                System.out.println("There is an IO error while reading. "+e.getMessage());
            }
        }
    }
    
    
    public void parse(Users user) throws IOException
    {
        
        Options option = new Options();
        
        option.addOption("N",false,"Create a new note with the given title.");
        option.addOption("T",false,"Create a new Todo list with the given title.");
        option.addOption("A",false,"Add task to Todo list with given hash.");
        option.addOption("D",false,"Delete note/Todo list with given hash.");
        option.addOption("DT",false,"Delete task from Todo list with given hash.");
        option.addOption("ST",false,"Show all Todo lists.");
        option.addOption("SN",false,"Show all notes.");
        option.addOption("SA",false,"Show all notes and Todo lists.");
        option.addOption("SC",false,"Search through categories");
        option.addOption("S",false,"Show a specific note or Todo list.");
        option.addOption("E",false,"Edit note with given hash.");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "Notebook", option );
        System.out.print("Arguments: ");
        String input = in.next() + in.nextLine();
        String[] arguments = input.split(" ");
        try
        {
            CommandLineParser cmd = new DefaultParser();
            cmdline = cmd.parse(option, arguments);
        }
        catch (ParseException parseException)
        {
            System.out.println("ERROR: Unable to parse command-line arguments "+ parseException);
        }
        if(cmdline.hasOption("N"))
        {
            executeCmd(user,1);
        }
        if(cmdline.hasOption("T"))
        {
            executeCmd(user,2);
        }
        if(cmdline.hasOption("A"))
        {
            executeCmd(user,3);
        }
        if(cmdline.hasOption("D"))
        {
            executeCmd(user,4);
        }
        if(cmdline.hasOption("DT"))
        {
            executeCmd(user,5);
        }
        if(cmdline.hasOption("ST"))
        {
            executeCmd(user,6);
        }
        if(cmdline.hasOption("SN"))
        {
            executeCmd(user,7);
        }
        if(cmdline.hasOption("SA"))
        {
            executeCmd(user,8);
        }
        if(cmdline.hasOption("SC"))
        {
            executeCmd(user,9);
        }
        if(cmdline.hasOption("S"))
        {
            executeCmd(user,10);
        }
        if(cmdline.hasOption("E"))
        {
            executeCmd(user,11);
        }
    }
    
    public void executeCmd(Users user, int input) throws IOException
    {
        String title = "";
        String content = "";
        String hash = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner in = new Scanner(System.in);
        switch(input)
                {
                    case 1:
                        System.out.println("Title: ");
                        title = reader.readLine();
                        System.out.println("Content: ");
                        content = reader.readLine();
                        user.addNote(content,title);
                        break;
                    
                    case 2:
                        System.out.println("Title: ");
                        title = reader.readLine();
                        System.out.println("Category: ");
                        String category = in.next();
                        content = "";
                        hash = "";
                        int i = 0;
                        do
                        {
                        System.out.println("Content (Enter -1 to exit): ");
                        content = reader.readLine();
                        if(!content.equals("-1"))
                        {
                        
                        if(i == 0)
                            hash = user.addTodo(content,title,hash,category,false);
                        else
                            hash = user.addTodo(content,title,hash,category,true); 
                        i++;
                        }
                        }while(!content.equals("-1"));
                        break;
                    
                    case 3: 
                        System.out.println("Hash: ");
                        hash = in.next();
                        System.out.println("Content: ");
                        content = reader.readLine();
                        user.addTodo(content, "", hash, "", true);
                        break;
                        
                    case 4:
                        System.out.println("Hash: ");
                        hash = in.next();
                        user.delete(hash);
                        break;
                    
                    case 5:
                        System.out.println("Hash: ");
                        hash = in.next();
                        user.deleteTask(hash);
                        break;
                        
                    case 6:
                        user.showAll("*", 1);
                        break;
                     
                    case 7:
                        user.showAll("*", 0);
                        break;
                    
                    case 8:
                        user.showAll("*",0);
                        user.showAll("*",1);
                        break;
                        
                    case 9:
                        System.out.println("Category: ");
                        String cate = in.next();
                        user.showAll(cate, 0);
                        user.showAll(cate, 1);
                        break;
                        
                    case -1:
                        break;
                        
                    case 10:
                        System.out.println("1.Hash\n2.Title");
                        int choice = in.nextInt();
                        if(choice == 1)
                        {
                            System.out.println("Hash: ");
                            hash = in.next();
                            user.show(hash);
                        }
                        else if(choice == 2)
                        {
                            System.out.println("Title: ");
                            String search = reader.readLine();
                            System.out.println("1.Notes\n2.Todo lists");
                            int type = in.nextInt()-1;
                            user.getHash(search,type);
                        }
                        break;
                    
                    case 11:
                        System.out.println("Hash: ");
                        hash = in.next();
                        user.editNote(hash);
                        break;
                            
                    default: 
                        System.out.println("Invalid Input!");
                }
    }
}
