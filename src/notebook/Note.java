package notebook;
import java.util.*;
import java.text.*;
import java.sql.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
/**
 *
 * @author C.V.Hariharan
 */
public class Note implements Serializable{
    private LinkedList<String> categories = new LinkedList<String>();
    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private Timestamp timestamp;
    public String date_time;
    private String email;
    private String content;
    public String title;
    public String owner;
    public String hash;
    private java.util.Date scheduledFor;
    public boolean reminder = false;
    public long remind_unix;
    Note(String username,String title, String email)
    {
        timestamp = new Timestamp(System.currentTimeMillis());
        date_time = date_format.format(timestamp);
        this.owner = username;
        this.title = title;
        this.email = email;
        generateHash();
    }
    
    public void createNote(String content)
    {
        this.content = content;
        findCategory();
        parseDate();
        remind();
    }
    
    public void appendNote(String add)
    {
        this.content += add;
        findCategory();
    }
    public String returnContent()
    {
        return this.content+"\n";
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
                handler.insertInto("notes.db", "categories", this.hash,hashtag,this.owner,0);
            }
        }
    }

    private void parseDate()
    {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String content = this.content;
        String s_date;
        int start_index = content.indexOf("%");
        int end_index = content.indexOf("%",start_index+1);
        if(start_index < end_index && end_index > 0)
        {
            s_date = content.substring(start_index+1, end_index); //To skip % sign 
            this.reminder = true;
        try
        {
            this.scheduledFor = sf.parse(s_date.trim());
            this.remind_unix = this.scheduledFor.getTime()/1000; //Returns milli seconds 
            System.out.println("Reminder scheduled for: "+sf.parse(s_date.trim()));
        } 
        catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
        }
        
    }
    public void remind()
    {
        if(this.reminder)
        {
        String api_key = "";
        String message = "";
        int start_index = this.content.indexOf("%");
        int end_index = this.content.indexOf("%",start_index+1);
        if(start_index < end_index && end_index > 0)
        {
            message = this.content.replace(this.content.substring(start_index, end_index+1),"");
        }
        String jsonBody = "{\"personalizations\": [{\"to\": [{\"email\": \""+this.email+"\"}]}],\"from\": {\"email\": \"hv314@snu.edu.in\"},\"subject\": \""+"Reminder: "+this.title+"\",\"send_at\":"+this.remind_unix+",\"content\": [{\"type\": \"text/plain\", \"value\": \""+message+"\"}]}";
        
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post("https://api.sendgrid.com/v3/mail/send")
                    .header("Authorization", "Bearer "+api_key)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asJson();
        } catch (UnirestException ex) {
           System.out.println(ex.getMessage());
        }
        }
    }
}
