package dev.peterhaviland.site.dao;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Updates;

public class SequenceDAO {

    @Autowired
    private MongoCollection<Document> sequencesCollection;
    
    public int findAndUpdateSequence() {
        Document sequence = sequencesCollection.findOneAndUpdate(Filters.eq("_id", "posts"), Updates.inc("value", 1), new FindOneAndUpdateOptions().upsert(true));
        return sequence.getInteger("value");
    }
    
}
