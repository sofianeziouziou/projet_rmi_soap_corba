package com.immobilier.core;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "ImmobilierDB";

    static {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            System.out.println("✅ Connexion MongoDB (core) établie");
        } catch (Exception e) {
            System.err.println("❌ Erreur connexion MongoDB (core): " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() {
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    public static MongoClient getClient() {
        return mongoClient;
    }

    public static void close() {
        if (mongoClient != null) mongoClient.close();
    }
}
