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
    public Users owner;
    Note(Users user)
    {
        timestamp = new Timestamp(System.currentTimeMillis());
        date_time = date_format.format(timestamp);
        this.owner = user;
    }
    
    public void createNote(String content)
    {
        this.content = content;
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
        String date_and_content = this.date_time + this.content;
        String hash = BCrypt.hashpw(date_and_content,BCrypt.gensalt());
        return hash;
    }
}
