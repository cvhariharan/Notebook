/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notebook;
import java.util.Scanner;

/**
 *
 * @author thero
 */
public class Notebook {

    /**
     * @param args the command line arguments
     */
    static Note note;
    public static void main(String[] args) {
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
        
        Scanner in = new Scanner(System.in);
        System.out.println("Username: ");
        String username = in.next();
        System.out.println("Password: ");
        String password = in.next();
        Users user = new Users(username,password);
        if(user.logged_in)
        {
            //user.addTodo("Test todo","Test","","Test",false);
            //user.addTodo("Second test","","$2a$10$OPMBp7VnkkqDU9RshqXHXuvA7JLsHBXY1EVgvf92mvBHDhhyo7v9C","",true);
            /*user.addNote("Test #note", "test2");
            user.addNote("Testas asfasf #note", "test3");
            user.addNote("Test sfasfasf #note", "test4");
            user.addNote("Test2 #as", "lol1");
            user.addNote("Test3 #as", "lol2");*/
            user.delete("$2a$10$ox8VRsYww6S5h7t2nCQK9O2ZbVJ4rca4jnF6HpPof6eZjMV0uh0xy");
            user.showAll("#note", 0);
            //user.delete("$2a$10$OPMBp7VnkkqDU9RshqXHXuvA7JLsHBXY1EVgvf92mvBHDhhyo7v9C");
            //user.showAll("*",1); //0-Note,1-Todo
            //user.addTodo("Just checking","Test2","$2a$10$XOxJalyO577lA7cfI18j7u25vgJAsSrWsthZvKgOF9Wv7nm41Bs1m",true);
            
        }
    }
    
}
