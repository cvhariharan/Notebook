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
            user.addNote("#This is a #hash #tag", "Hashtag test");
            user.addNote("#This is another test #file","hashtag test2");
            //user.addTodo("Just checking","Test2","$2a$10$XOxJalyO577lA7cfI18j7u25vgJAsSrWsthZvKgOF9Wv7nm41Bs1m",true);
        }
    }
    
}
