package dev.peterhaviland.site.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;

import dev.peterhaviland.site.beans.Comment;
import dev.peterhaviland.site.beans.Post;

public class PostsDAO {

    @Autowired
    private Datastore datastore;
    
    public void composePost(Post post) {
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
        return query.order("-_id").asList(new FindOptions().limit(numberOfPosts));
    }
    
    public List<Post> getPosts(int minArticleId, int numberOfPosts) {
        Query<Post> query = datastore.createQuery(Post.class).field("_id").lessThan(minArticleId);
        return query.order("-_id").asList(new FindOptions().limit(numberOfPosts));
    }
    
    public int updatePost(int id, String subject, String body) {
        Query<Post> query = datastore.createQuery(Post.class).field("_id").equal(id);
        UpdateOperations<Post> updateOperations = datastore.createUpdateOperations(Post.class).set("subject", subject).set("body", body);
        UpdateResults updateResults = datastore.update(query, updateOperations);
        return updateResults.getUpdatedCount();
    }
    
    public void deletePost(int id) {
        Query<Post> query = datastore.createQuery(Post.class).field("_id").equal(id);
        datastore.delete(query);
    }
    
    public int composeComment(int id, Comment comment) {
        Query<Post> query = datastore.createQuery(Post.class).field("_id").equal(id);
        UpdateOperations<Post> updateOperations = datastore.createUpdateOperations(Post.class).push("comments", comment);
        UpdateResults updateResults = datastore.update(query, updateOperations);
        return updateResults.getUpdatedCount();
    }
    
}
