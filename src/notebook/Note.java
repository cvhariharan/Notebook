/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notebook;
import java.util.*;
import java.text.*;
import java.sql.*;
import java.io.*;
/**
 *
 * @author thero
 */
public class Note implements Serializable{
    private LinkedList<String> categories = new LinkedList<String>();
    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private Timestamp timestamp;
    public String date_time;
    private String content;
    public String title;
    public String owner;
    public String hash;
    Note(String username,String title)
    {
        timestamp = new Timestamp(System.currentTimeMillis());
        date_time = date_format.format(timestamp);
        this.owner = username;
        this.title = title;
        generateHash();
    }
    
    public void createNote(String content)
    {
        this.content = content;
        
        findCategory();
    }
    
    public void appendNote(String add)
    {
        this.content += add;
    }
    public String returnContent()
    {
        return this.content;
    }
    
    private String generateHash()
    {
        String date_and_title = this.date_time + this.owner;
        String hash = BCrypt.hashpw(date_and_title,BCrypt.gensalt());
        hash = hash.replace("\\", "s");
        hash = hash.replace("/", "s");
        hash = hash.replace(".", "s");
        this.hash = hash.substring(10,25);
        return hash;
    }
    
    public final void findCategory()
    {
        DatabaseHandler handler = new DatabaseHandler();
        String temp = this.content;
        int cat_index = 0;
        while(temp.indexOf("#",cat_index)>=0)
        {
            int cat_start = temp.indexOf("#",cat_index);
            int cat_end = 0;
            if(temp.indexOf(" ",cat_start)<temp.length() && temp.indexOf(" ",cat_start) != -1)
            {
                cat_end = temp.indexOf(" ",cat_start);
            }
            else
                cat_end = temp.length();
            cat_index = cat_end+1;
            String hashtag = temp.substring(cat_start, cat_end);
            if(!categories.contains(hashtag))
            {
                categories.add(hashtag);
                /*String sql = "insert into categories values ('"+this.hash+"','"+hashtag+"', '"+this.owner+"', '"+0+"')";
                DatabaseHandler.executeUpdateDb(sql,"notes.db");*/
                handler.insertInto("notes.db", "categories", this.hash,hashtag,this.owner,0);
            }
        }
    }

}
