package com.immobilier.soap;

import jakarta.jws.WebService;
import com.immobilier.core.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

@WebService(endpointInterface = "com.immobilier.soap.ContractService")
public class ContractServiceImpl implements ContractService {
    private final MongoCollection<Document> contrats;
    private final MongoCollection<Document> biens;

    public ContractServiceImpl() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        contrats = db.getCollection("contrats");
        biens = db.getCollection("biens");
        System.out.println("✅ [SOAP] ContractServiceImpl initialisé");
    }

    @Override
    public String generateContract(int bienId, long acheteurId) {
        try {
            Document bien = biens.find(new Document("_id", (long) bienId)).first();
            if (bien == null) return "<error>bien_not_found</error>";

            long newId = contrats.countDocuments() + 1;
            Document c = new Document()
                    .append("_id", newId)
                    .append("bienId", (long) bienId)
                    .append("acheteurId", acheteurId)
                    .append("dateCreation", new Date())
                    .append("statut", "finalisé")
                    .append("montant", bien.getDouble("prix"));
            contrats.insertOne(c);

            // simple XML string contract
            String xml = "<contract><id>" + newId + "</id><bienId>" + bienId + "</bienId><acheteurId>" + acheteurId + "</acheteurId><montant>" +
                    bien.getDouble("prix") + "</montant></contract>";
            return xml;
        } catch (Exception e) {
            e.printStackTrace();
            return "<error>server</error>";
        }
    }
}
