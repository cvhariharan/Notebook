/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notebook;
import java.util.Scanner;
import java.io.*;
/**
 *
 * @author thero
 */
public class Notebook {

    /**
     * @param args the command line arguments
     */
    static Note note;
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner in = new Scanner(System.in);
        System.out.println("Username: ");
        String username = in.next();
        System.out.println("Password: ");
        String password = in.next();
        Users user = new Users(username,password);
        int input = 0;
        String title;
        String content;
        String hash;
        if(user.logged_in)
        {
            do
            {
                String output = "1.Create Note\n2.Create Todo\n3.Add task to todo list\n4.Delete note/todo\n5.Delete task from todo\n6.Show all todo lists"
                +"\n7.Show all notes\n8.Show all\n9.Search category\n10.Search\n11.Edit Note\n(Enter -1 to exit)";
                System.out.println(output.toUpperCase());
                input = in.nextInt();
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
            }while(input != -1);
        }
    }
    
}
