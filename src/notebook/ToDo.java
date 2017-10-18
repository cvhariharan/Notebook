/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notebook;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author thero
 */
public class ToDo extends Note{
    private ArrayList<Note> note_links = new ArrayList<Note>();
    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private Timestamp timestamp;
    private String content;
    ToDo(String username,String title)
    {
        super(username,title);
        timestamp = new Timestamp(System.currentTimeMillis());
        date_time = date_format.format(timestamp);
        this.owner = username;
        this.title = title;
    }
    @Override
    public void createNote(String content)
    {
        Note note = new Note(this.owner,this.title);
        note.createNote(content);
        note_links.add(note);
    }
    
    public void deleteNote(int index)
    {
        note_links.remove(index);
    }
    
    public void showTasks()
    {
        for(Note note: note_links)
        {
            System.out.println(note.returnContent());
        }
    }
}
