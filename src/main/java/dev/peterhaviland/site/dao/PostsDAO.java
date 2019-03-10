package dev.peterhaviland.site.dao;

import java.util.Iterator;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.WriteResult;

import dev.peterhaviland.site.beans.Post;

public class PostsDAO {

    @Autowired
    private Datastore datastore;
    
    public void composeAttempt(Post post) {
        datastore.save(post);
    }
    
    public Post getPost(int id) {
        Query<Post> query = datastore.createQuery(Post.class);
        Iterator<Post> iterator = query.filter("_id", id).fetch();
        if (iterator.hasNext())
            return iterator.next();
        else
            return null;
    }
    
    public List<Post> getMostRecentPosts(int numberOfPosts) {
        Query<Post> query = datastore.createQuery(Post.class);
        return query.order("-id").asList(new FindOptions().limit(numberOfPosts));
    }
    
    public List<Post> getPosts(int offset, int numberOfPosts) {
        Query<Post> query = datastore.createQuery(Post.class);
        return query.order("-id").asList(new FindOptions().limit(numberOfPosts).skip(offset));
    }
    
    public int updatePost(Post post) {
        Query<Post> query = datastore.createQuery(Post.class).field("id").equal(post.getId());
        UpdateOperations<Post> updateOperations = datastore.createUpdateOperations(Post.class).set("subject", post.getSubject()).set("body", post.getBody());
        UpdateResults updateResults = datastore.update(query, updateOperations);
        return updateResults.getUpdatedCount();
    }
    
    public void deletePost(int id) {
        Query<Post> query = datastore.createQuery(Post.class).field("id").equal(id);
        datastore.delete(query);
    }
    
}
