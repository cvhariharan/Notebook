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
        
    }
    
    public void createNote(String content)
    {
        this.content = content;
        generateHash();
    }
    
    public void appendNote(String add)
    {
        this.content += add;
    }
    public String returnContent()
    {
        return this.content;
    }
    
    public String generateHash()
    {
        String date_and_title = this.date_time + this.title;
        String hash = BCrypt.hashpw(date_and_title,BCrypt.gensalt());
        hash = hash.replace("\\", "s");
        hash = hash.replace("/", "s");
        hash = hash.replace(".", "s");
        this.hash = hash;
        return hash;
    }
}
