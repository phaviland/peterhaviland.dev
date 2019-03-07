package dev.peterhaviland.site.dao;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class UsersDAO {

    @Autowired
    private MongoCollection<Document> usersCollection;
    
    public Document loginAttempt(String username) {
        return usersCollection.find(Filters.eq("_id", username)).first();
    }
    
    public void registerAttempt(String username, String hash) {
        Document account = new Document("_id", username).append("hash", hash);
        usersCollection.insertOne(account);
    }

}