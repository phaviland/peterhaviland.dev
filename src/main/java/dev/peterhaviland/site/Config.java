package dev.peterhaviland.site;

import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import dev.peterhaviland.site.beans.User;
import dev.peterhaviland.site.dao.PostsDAO;
import dev.peterhaviland.site.dao.SequenceDAO;
import dev.peterhaviland.site.dao.UsersDAO;

@Configuration
public class Config implements WebMvcConfigurer  {
    
    @Value("${CONNECTION_STRING}")
    private String connectionString;
    
    @Bean
    public Properties properties() {
        return new Properties();
    }
    
    @Bean
    public MongoClient mongoClient(Properties properties) {
        return new MongoClient(new MongoClientURI(connectionString));
    }
    
    @Bean
    public MongoCollection<Document> usersCollection(MongoClient mongoClient) {
        return mongoClient.getDatabase("blog").getCollection("users");
    }
    
    @Bean
    public MongoCollection<Document> postsCollection(MongoClient mongoClient) {
        return mongoClient.getDatabase("blog").getCollection("posts");
    }
    
    @Bean
    public MongoCollection<Document> sequencesCollection(MongoClient mongoClient) {
        return mongoClient.getDatabase("blog").getCollection("sequences");
    }
    
    @Bean
    public UsersDAO usersDAO() {
        return new UsersDAO();
    }
    
    @Bean
    public PostsDAO postsDAO() {
        return new PostsDAO();
    }
    
    @Bean
    public SequenceDAO sequenceDAO() {
        return new SequenceDAO();
    }
    
    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }
    
    @Bean
    public SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }
    
    @Bean
    @Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
    public User user() {
        return new User();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor());
        registry.addInterceptor(securityInterceptor()).addPathPatterns("/compose","/blog/posts/*/edit","/blog/posts/*/delete");
    }
    
    @Bean
    public Morphia morphia() {
        return new Morphia();
    }
    
    @Bean
    public Datastore datastore(MongoClient mongoClient, Morphia morphia) {
        return morphia.createDatastore(mongoClient, "blog");
    }

}
