package dev.peterhaviland.dao;

import java.util.Iterator;
import java.util.regex.Pattern;

import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.client.MongoCollection;

import dev.peterhaviland.beans.User;

public class UsersDAO {

    @Autowired
    private MongoCollection<Document> usersCollection;
    @Autowired
    private Datastore datastore;
    
    public User loginAttempt(String username) {
        Query<User> query = datastore.createQuery(User.class);
        Pattern regex = Pattern.compile(username, Pattern.CASE_INSENSITIVE);
        Iterator<User> iterator = query.filter("username", regex).fetch();
        if (iterator.hasNext())
            return iterator.next();
        else
            return null;
    }
    
    public void registerAttempt(String username, String hash) {
        Document account = new Document("username", username).append("hash", hash);
        usersCollection.insertOne(account);
    }

}