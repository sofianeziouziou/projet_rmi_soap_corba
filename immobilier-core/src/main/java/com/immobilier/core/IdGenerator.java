package com.immobilier.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

public class IdGenerator {

    public static long getNextId(MongoCollection<Document> collection) {
        try {
            Document last = collection.find().sort(Sorts.descending("_id")).first();
            if (last == null) return 1;
            return last.getLong("_id") + 1;
        } catch (Exception e) {
            return 1;
        }
    }
}
