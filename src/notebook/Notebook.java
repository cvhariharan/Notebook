package notebook;
import java.util.Scanner;
import java.io.*;
import org.apache.commons.cli.*;
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
        String title = "";
        String content = "";
        String hash = "";
        Options option = new Options();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        option.addOption("n",true,"Create a new note with the given title.");
        option.addOption("t",true,"Create a new Todo list with the given title.");
        option.addOption("a",true,"Add task to Todo list with given hash.");
        option.addOption("d",true,"Delete note/Todo list with given hash.");
        option.addOption("dt",false,"Delete task from Todo list with given hash.");
        option.addOption("st",false,"Show all Todo lists.");
        option.addOption("sn",false,"Show all notes.");
        option.addOption("sa",false,"Show all notes and Todo lists.");
        option.addOption("sc",true,"Search through categories");
        option.addOption("s",false,"Show a specific note or Todo list.");
        option.addOption("e",false,"Edit note with given hash.");
        option.addOption("exit",false,"Exit");
        option.addOption("h",false,"Help");
        System.out.print("Arguments: ");
        String input = in.next() + in.nextLine();
        String[] arguments = input.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); //Regex to select all whitespaces except the ones in between double quotes
        try
        {
            CommandLineParser cmd = new DefaultParser();
            cmdline = cmd.parse(option, arguments);
        }
        catch (ParseException parseException)
        {
            System.out.println("ERROR: Unable to parse command-line arguments "+ parseException);
        }
        if(cmdline.hasOption("n"))
        {
            title = cmdline.getOptionValue("n");
            System.out.println("Content: ");
            content = reader.readLine();
            user.addNote(content,title);
        }
        if(cmdline.hasOption("t"))
        {
            title = cmdline.getOptionValue("t");
            System.out.println("Category: ");
            String category = in.next();
            content = "";
            hash = "";
            int i = 0;
            do {
                System.out.println("Content (Enter -1 to exit): ");
                content = reader.readLine();
                if (!content.equals("-1")) {

                    if (i == 0) {
                        hash = user.addTodo(content, title, hash, category, false);
                    } else {
                        hash = user.addTodo(content, title, hash, category, true);
                    }
                    i++;
                }
            } while (!content.equals("-1"));
        }
        if(cmdline.hasOption("h"))
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Notebook", option);
        }
        if(cmdline.hasOption("a"))
        {
            hash = cmdline.getOptionValue("a");
            System.out.println("Content: ");
            content = reader.readLine();
            user.addTodo(content, "", hash, "", true);
        }
        if(cmdline.hasOption("d"))
        {
            hash = cmdline.getOptionValue("d");
            user.delete(hash);
        }
        if(cmdline.hasOption("dt"))
        {
            hash = cmdline.getOptionValue("dt");
            user.deleteTask(hash);
        }
        if(cmdline.hasOption("st"))
        {
            user.showAll("*", 1);
        }
        if(cmdline.hasOption("sn"))
        {
            user.showAll("*", 0);
        }
        if(cmdline.hasOption("sa"))
        {
            user.showAll("*",0);
            user.showAll("*",1);
        }
        if(cmdline.hasOption("sc"))
        {
            String cate = cmdline.getOptionValue("sc");
            user.showAll(cate, 0);
            user.showAll(cate, 1);
        }
        if(cmdline.hasOption("s"))
        {
            System.out.println("1.Hash\n2.Title");
            int choice = in.nextInt();
            if (choice == 1) {
                System.out.println("Hash: ");
                hash = in.next();
                user.show(hash);
            } else if (choice == 2) {
                System.out.println("Title: ");
                String search = reader.readLine();
                System.out.println("1.Notes\n2.Todo lists");
                int type = in.nextInt() - 1;
                user.getHash(search, type);
            }
        }
        if(cmdline.hasOption("e"))
        {
            hash = cmdline.getOptionValue("e");
            user.editNote(hash);
        }
        if(cmdline.hasOption("exit"))
        {
            System.exit(0);
        }
        parse(user);
    }
}
