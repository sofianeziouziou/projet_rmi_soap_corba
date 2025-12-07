package com.immobilier.soap;

import jakarta.jws.WebService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Date;
import static com.mongodb.client.model.Filters.*;

@WebService(endpointInterface = "com.immobilier.soap.ContractService")
public class ContractServiceImpl implements ContractService {
    private final MongoCollection<Document> contratsCollection;
    private final MongoCollection<Document> biensCollection;

    public ContractServiceImpl() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.contratsCollection = db.getCollection("contrats");
        this.biensCollection = db.getCollection("biens");
        System.out.println("✅ ContractServiceImpl initialisé avec MongoDB");
    }

    @Override
    public String createContract(int bienId, int acheteurId) {
        try {
            // Vérifier si le bien existe et est disponible
            Document bien = biensCollection.find(eq("_id", (long) bienId)).first();

            if (bien == null) {
                return "❌ Erreur: Bien " + bienId + " introuvable";
            }

            if (!bien.getBoolean("disponible", false)) {
                return "❌ Erreur: Bien " + bienId + " n'est plus disponible";
            }

            // Créer le contrat
            long newId = contratsCollection.countDocuments() + 1;

            Document contrat = new Document()
                    .append("_id", newId)
                    .append("bienId", (long) bienId)
                    .append("acheteurId", (long) acheteurId)
                    .append("dateCreation", new Date())
                    .append("statut", "en_cours")
                    .append("montant", bien.getDouble("prix"));

            contratsCollection.insertOne(contrat);

            // Marquer le bien comme non disponible
            biensCollection.updateOne(
                    eq("_id", (long) bienId),
                    new Document("$set", new Document("disponible", false))
            );

            String message = "✅ Contrat créé avec succès!\n" +
                    "ID Contrat: " + newId + "\n" +
                    "Bien: " + bien.getString("titre") + "\n" +
                    "Prix: " + bien.getDouble("prix") + " €\n" +
                    "Acheteur ID: " + acheteurId;

            System.out.println(message);
            return message;

        } catch (Exception e) {
            String error = "❌ Erreur lors de la création du contrat: " + e.getMessage();
            System.err.println(error);
            e.printStackTrace();
            return error;
        }
    }
}