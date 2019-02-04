package com.peter_haviland.site;

import org.bson.Document;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.peter_haviland.site.beans.Session;
import com.peter_haviland.site.dao.UsersDAO;

@Configuration
@ConfigurationProperties(prefix = "db")
public class Config {

    private String connectionString;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
    
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(getConnectionString()));
    }
    
    @Bean
    public MongoCollection<Document> usersCollection(MongoClient mongoClient) {
        return mongoClient.getDatabase("blog").getCollection("users");
    }
    
    @Bean
    public UsersDAO usersDAO() {
        return new UsersDAO();
    }
    
    @Bean
    @Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
    public Session session() {
        return new Session();
    }

}
