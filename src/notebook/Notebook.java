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
           /* user.addNote("Test #note", "test2");
            user.addNote("Testas asfasf #note", "test3");
            user.addNote("Test sfasfasf #note", "test4");
            user.addNote("Test2 #as", "lol1");
            user.addNote("Test3 #as", "lol2");
            user.showAll("#note", 0);*/
            user.showAll("*",1);
            //user.addTodo("Just checking","Test2","$2a$10$XOxJalyO577lA7cfI18j7u25vgJAsSrWsthZvKgOF9Wv7nm41Bs1m",true);
        }
    }
    
}
