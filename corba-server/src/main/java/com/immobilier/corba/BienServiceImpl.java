package com.immobilier.corba;

import Immobilier.*;
import com.immobilier.core.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class BienServiceImpl extends BienServicePOA {

    private final MongoCollection<Document> coll;

    public BienServiceImpl() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.coll = db.getCollection("biens");
        System.out.println("✅ BienServiceImpl (CORBA) initialisé");
    }

    @Override
    public long addBien(Bien b) {
        try {
            long newId = coll.countDocuments() + 1;

            Document d = new Document()
                    .append("_id", newId)
                    .append("titre", b.titre)
                    .append("description", b.description)
                    .append("prix", b.prix)
                    .append("disponible", b.disponible)
                    .append("agentId", b.agentId);

            coll.insertOne(d);

            System.out.println("✅ CORBA addBien id=" + newId);
            return (int) newId;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Bien[] listBiens() {
        List<Bien> list = new ArrayList<>();

        try {
            for (Document d : coll.find()) {
                list.add(documentToBien(d));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new Bien[0]);
    }

    @Override
    public Bien[] listBiensAgent(int agentId) {
        List<Bien> list = new ArrayList<>();

        try {
            for (Document d : coll.find(eq("agentId", agentId))) {
                list.add(documentToBien(d));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.toArray(new Bien[0]);
    }

    @Override
    public boolean checkDisponibilite(int bienId) {
        try {
            Document d = coll.find(eq("_id", bienId)).first();
            return d != null && d.getBoolean("disponible", false);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Bien documentToBien(Document d) {
        Bien b = new Bien();
        b.id = d.getLong("_id").intValue();
        b.titre = d.getString("titre");
        b.description = d.getString("description");
        b.prix = d.getDouble("prix");
        b.disponible = d.getBoolean("disponible");
        b.agentId = d.getLong("agentId").intValue();
        return b;
    }
}
