package com.peter_haviland.site.dao;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class UsersDAO {

    @Autowired
    private MongoCollection<Document> usersCollection;
    
    public Document findByUser(String user) {
        Document post = usersCollection.find(Filters.eq("user", user)).first();
        return post;
    }

}