package com.peter_haviland.site.dao;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.peter_haviland.site.beans.Session;

public class UsersDAO {

    @Autowired
    private MongoCollection<Document> usersCollection;
    @Autowired
    private Session session;
    
    public boolean login(String username, String password) {
        Document account = usersCollection.find(Filters.and(Filters.eq("_id", username), Filters.eq("password", password))).first();
        
        if (account == null)
            return false;
        else
            session.setId(account.getString("_id"));
        
        return true;
    }

}