package dev.peterhaviland.site.beans;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "users", noClassnameStored = true)
public class User {

    @Id
    private ObjectId id;
    private String username;
    private String hash;
    
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String id) {
        this.username = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    
}
