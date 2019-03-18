package dev.peterhaviland.beans;

import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Comment {

    private String username;
    private Date date;
    private String body;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
}
