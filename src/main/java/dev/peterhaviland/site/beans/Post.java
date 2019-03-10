package dev.peterhaviland.site.beans;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "posts", noClassnameStored = true)
public class Post {

    @Id
    private Integer id;
    private Date date;
    private ObjectId userId;
    private String username;
    private String subject;
    private String body;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public ObjectId getUserId() {
        return userId;
    }
    
    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
}
