package notebook;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author C.V.Hariharan
 */
public class ToDo extends Note{
    private LinkedList<String> categories = new LinkedList<String>();
    private ArrayList<Note> note_links = new ArrayList<Note>();
    private SimpleDateFormat date_format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private Timestamp timestamp;
    private String content;
    private String email;
    ToDo(String username,String title,String email)
    {
        super(username,title,email);
        timestamp = new Timestamp(System.currentTimeMillis());
        date_time = date_format.format(timestamp);
        this.owner = username;
        this.title = title;
        generateHash();
    }
    @Override
    public void createNote(String content)
    {
        Note note = new Note(this.owner,this.title,this.email);
        note.createNote(content);
        note_links.add(note);
    }
    
    public void deleteNote(int index)
    {
        note_links.remove(index);
    }
    @Override 
    public String returnContent()
    {
        int i = 1;
        for(Note note: note_links)
        {
            System.out.print(i+"."+note.returnContent());
            i++;
        }
        return "";
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
    
    public void addCategory(String cate)
    {
        DatabaseHandler handler = new DatabaseHandler();
        if(!categories.contains(cate))
        {
            categories.add(cate);
            handler.insertInto("notes.db","categories",this.hash,cate,this.owner,1);
        }
    }
}
