package com.immobilier.rest;

import com.google.gson.Gson;
import com.immobilier.core.Bien;
import com.immobilier.core.MongoDBConnection;
import com.immobilier.producer.Producer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/biens/*")
public class BiensServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final MongoCollection<Document> coll;

    public BiensServlet() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        coll = db.getCollection("biens");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            List<Bien> list = new ArrayList<>();
            for (Document d : coll.find()) {
                Bien b = new Bien();
                b.setId(d.getLong("_id"));
                b.setTitre(d.getString("titre"));
                b.setDescription(d.getString("description"));
                b.setPrix(d.getDouble("prix"));
                b.setDisponible(d.getBoolean("disponible", false));
                b.setAgentId(d.getLong("agentId"));
                list.add(b);
            }
            resp.getWriter().write(gson.toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"server\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // POST /biens -> ajouter un bien
        resp.setContentType("application/json;charset=UTF-8");
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            Bien b = gson.fromJson(sb.toString(), Bien.class);

            if (b.getTitre() == null ) {
                resp.setStatus(400);
                resp.getWriter().write("{\"error\":\"titre_et_prix_requis\"}");
                return;
            }

            long newId = coll.countDocuments() + 1;
            Document doc = new Document("_id", newId)
                    .append("titre", b.getTitre())
                    .append("description", b.getDescription())
                    .append("prix", b.getPrix())
                    .append("disponible", true)
                    .append("agentId", b.getAgentId());
            coll.insertOne(doc);

            // Notification JMS
            Producer.sendNotification("Nouveau bien ajouté : " + b.getTitre());

            resp.getWriter().write("{\"success\":true,\"id\":" + newId + "}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"server\"}");
        }
    }
}
