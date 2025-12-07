package com.immobilier.rest;

import com.google.gson.Gson;
import com.immobilier.core.MongoDBConnection;
import com.immobilier.producer.Producer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

@WebServlet("/acheterBien")
public class AcheterBienServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final MongoCollection<Document> biens;
    private final MongoCollection<Document> contrats;

    public AcheterBienServlet() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        biens = db.getCollection("biens");
        contrats = db.getCollection("contrats");
    }

    public static class AchatRequest {
        public long bienId;
        public long acheteurId;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        try {
            // Lire JSON de la requête
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            AchatRequest achat = gson.fromJson(sb.toString(), AchatRequest.class);

            // Vérifier si le bien existe
            Document docBien = biens.find(eq("_id", achat.bienId)).first();
            if (docBien == null) {
                // Essayer avec cast double si ID mismatch
                docBien = biens.find(eq("_id", (double) achat.bienId)).first();
                if (docBien == null) {
                    resp.getWriter().write("{\"error\":\"bien_not_found\"}");
                    return;
                }
            }

            // Vérifier disponibilité
            if (!docBien.getBoolean("disponible", false)) {
                resp.getWriter().write("{\"error\":\"bien_not_available\"}");
                return;
            }

            // Créer ID contrat simple
            long newContractId = contrats.countDocuments() + 1;
            double montant = docBien.getDouble("prix");

            // Créer contrat
            Document contrat = new Document("_id", newContractId)
                    .append("bienId", achat.bienId)
                    .append("acheteurId", achat.acheteurId)
                    .append("dateCreation", new Date())
                    .append("montant", montant)
                    .append("statut", "finalisé");
            contrats.insertOne(contrat);

            // Marquer bien comme vendu
            biens.updateOne(eq("_id", achat.bienId), new Document("$set", new Document("disponible", false)));

            // Envoyer notification JMS
            Producer.sendNotification("Bien \"" + docBien.getString("titre") +
                    "\" acheté pour " + montant +
                    " € (contrat " + newContractId + ")");

            // Réponse
            resp.getWriter().write("{\"success\":true,\"contractId\":" + newContractId + ",\"montant\":" + montant + "}");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"server\"}");
        }
    }
}
