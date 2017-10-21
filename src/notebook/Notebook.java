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
        /*Options option = new Options();
        option.addOption("N",true,"Create a new note with the given title.");
        option.addOption("T",true,"Create a new Todo list with the given title.");
        option.addOption("A",true,"Append a new task to the todo list with given hash.");
        option.addOption("D",true,"Delete note/todo with given hash.");//Not implemented
        option.addOption("Dt",true,"Delete a task from a todo list.");
        option.addOption("ST",true,"Show todo list with given hash");
        option.addOption("SN",true,"Show note with given hash");
        option.addOption("SA",false,"Show all notes and todo lists.");
        option.addOption("S",true,"Show all notes and todo lists in the given category.");
        BasicParser parser = new BasicParser();*/
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
            //user.addTodo("Test todo","Test","","Test",false);
            //user.addTodo("Second test","","$2a$10$OPMBp7VnkkqDU9RshqXHXuvA7JLsHBXY1EVgvf92mvBHDhhyo7v9C","",true);
            /*user.addNote("Test #note", "test2");
            user.addNote("Testas asfasf #note", "test3");
            user.addNote("Test sfasfasf #note", "test4");
            user.addNote("Test2 #as", "lol1");
            user.addNote("Test3 #as", "lol2");*/
            //user.delete("$2a$10$ox8VRsYww6S5h7t2nCQK9O2ZbVJ4rca4jnF6HpPof6eZjMV0uh0xy");
            //user.showAll("#note", 0);
            //user.delete("$2a$10$OPMBp7VnkkqDU9RshqXHXuvA7JLsHBXY1EVgvf92mvBHDhhyo7v9C");
            //user.showAll("*",1); //0-Note,1-Todo
            //user.addTodo("Just checking","Test2","$2a$10$XOxJalyO577lA7cfI18j7u25vgJAsSrWsthZvKgOF9Wv7nm41Bs1m",true);
            do
            {
                System.out.println("1.Create Note\n2.Create Todo\n3.Add task to todo list\n4.Delete note/todo\n5.Delete task from todo\n6.Show todo"
                +"\n7.Show note\n8.Show all\n9.Search category\n10.Show\n(Enter -1 to exit)");
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
                        System.out.println("Content: ");
                        content = reader.readLine();
                        System.out.println("Category: ");
                        String category = in.next();
                        user.addTodo(content,title,category,"",false);
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
                        user.showAll("#note.", 0);
                        user.showAll(cate, 1);
                        break;
                        
                    case -1:
                        break;
                        
                    case 10:
                        System.out.println("Hash: ");
                        hash = in.next();
                        user.show(hash);
                        break;
                        
                    default: 
                        System.out.println("Invalid Input!");
                }
            }while(input != -1);
        }
    }
    
}
