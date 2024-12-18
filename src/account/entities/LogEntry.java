package account.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "Log_Entry")
@Entity
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @ManyToOne
    @JsonIgnore
    private int id;

    @NotEmpty
    @JsonIgnore
    private String date;

    @NotEmpty
    private String action;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String object;

    @NotEmpty
    private String path;

//    @ElementCollection
//    private List<LogEntry> logEntryList = new ArrayList<>();

    public LogEntry() {
    }

    public LogEntry(String subject, String action, String object, String path) {
        this.date = String.valueOf(LocalDateTime.now());
        this.subject = subject;
        this.action = action;
        this.object = object;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", subject='" + subject + '\'' +
                ", object='" + object + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
