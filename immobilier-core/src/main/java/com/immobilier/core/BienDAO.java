package com.immobilier.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.*;

/**
 * DAO pour la gestion des biens immobiliers
 */
public class BienDAO {
    private final MongoCollection<Document> collection;

    public BienDAO(MongoDatabase database) {
        this.collection = database.getCollection("biens");
    }

    /**
     * Crée un nouveau bien
     */
    public Bien create(Bien bien) {
        try {
            long newId = collection.countDocuments() + 1;
            bien.setId(newId);

            Document doc = bienToDocument(bien);
            collection.insertOne(doc);

            System.out.println("✅ Bien créé avec ID: " + newId);
            return bien;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création du bien: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Trouve un bien par son ID
     */
    public Bien findById(long id) {
        try {
            Document doc = collection.find(eq("_id", id)).first();
            return doc != null ? documentToBien(doc) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Liste tous les biens
     */
    public List<Bien> findAll() {
        List<Bien> biens = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                biens.add(documentToBien(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return biens;
    }

    /**
     * Liste les biens disponibles
     */
    public List<Bien> findDisponibles() {
        List<Bien> biens = new ArrayList<>();
        try {
            for (Document doc : collection.find(eq("disponible", true))) {
                biens.add(documentToBien(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return biens;
    }

    /**
     * Liste les biens d'un agent
     */
    public List<Bien> findByAgent(long agentId) {
        List<Bien> biens = new ArrayList<>();
        try {
            for (Document doc : collection.find(eq("agentId", agentId))) {
                biens.add(documentToBien(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return biens;
    }

    /**
     * Liste les biens par ville
     */
    public List<Bien> findByVille(String ville) {
        List<Bien> biens = new ArrayList<>();
        try {
            for (Document doc : collection.find(eq("ville", ville))) {
                biens.add(documentToBien(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return biens;
    }

    /**
     * Recherche de biens par critères
     */
    public List<Bien> search(Double prixMin, Double prixMax, String ville, String type) {
        List<Bien> biens = new ArrayList<>();
        try {
            List<org.bson.conversions.Bson> filters = new ArrayList<>();

            if (prixMin != null) {
                filters.add(gte("prix", prixMin));
            }
            if (prixMax != null) {
                filters.add(lte("prix", prixMax));
            }
            if (ville != null && !ville.isEmpty()) {
                filters.add(eq("ville", ville));
            }
            if (type != null && !type.isEmpty()) {
                filters.add(eq("type", type));
            }
            filters.add(eq("disponible", true));

            org.bson.conversions.Bson filter = filters.size() > 1 ?
                    and(filters) : filters.get(0);

            for (Document doc : collection.find(filter)) {
                biens.add(documentToBien(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return biens;
    }

    /**
     * Met à jour un bien
     */
    public boolean update(Bien bien) {
        try {
            bien.updateModificationDate();
            Document doc = bienToDocument(bien);
            collection.replaceOne(eq("_id", bien.getId()), doc);
            System.out.println("✅ Bien mis à jour: " + bien.getId());
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Marque un bien comme vendu
     */
    public boolean marquerCommeVendu(long id) {
        try {
            Document update = new Document("$set",
                    new Document("disponible", false)
                            .append("dateModification", new java.util.Date()));

            collection.updateOne(eq("_id", id), update);
            System.out.println("✅ Bien " + id + " marqué comme vendu");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un bien
     */
    public boolean delete(long id) {
        try {
            collection.deleteOne(eq("_id", id));
            System.out.println("✅ Bien supprimé: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la suppression: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Convertit un Document MongoDB en objet Bien
     */
    private Bien documentToBien(Document doc) {
        Bien bien = new Bien();
        bien.setId(doc.getLong("_id"));
        bien.setTitre(doc.getString("titre"));
        bien.setDescription(doc.getString("description"));
        bien.setPrix(doc.getDouble("prix"));
        bien.setType(doc.getString("type"));
        bien.setSurface(doc.getDouble("surface") != null ? doc.getDouble("surface") : 0.0);
        bien.setNbPieces(doc.getInteger("nbPieces") != null ? doc.getInteger("nbPieces") : 0);
        bien.setNbChambres(doc.getInteger("nbChambres") != null ? doc.getInteger("nbChambres") : 0);
        bien.setNbSallesBain(doc.getInteger("nbSallesBain") != null ? doc.getInteger("nbSallesBain") : 0);
        bien.setAdresse(doc.getString("adresse"));
        bien.setVille(doc.getString("ville"));
        bien.setCodePostal(doc.getString("codePostal"));
        bien.setDisponible(doc.getBoolean("disponible", true));
        bien.setAgentId(doc.getLong("agentId"));
        bien.setDateCreation(doc.getDate("dateCreation"));
        bien.setDateModification(doc.getDate("dateModification"));
        bien.setCaracteristiques(doc.getString("caracteristiques"));

        return bien;
    }

    /**
     * Convertit un objet Bien en Document MongoDB
     */
    private Document bienToDocument(Bien bien) {
        Document doc = new Document()
                .append("_id", bien.getId())
                .append("titre", bien.getTitre())
                .append("description", bien.getDescription())
                .append("prix", bien.getPrix())
                .append("type", bien.getType())
                .append("surface", bien.getSurface())
                .append("nbPieces", bien.getNbPieces())
                .append("nbChambres", bien.getNbChambres())
                .append("nbSallesBain", bien.getNbSallesBain())
                .append("adresse", bien.getAdresse())
                .append("ville", bien.getVille())
                .append("codePostal", bien.getCodePostal())
                .append("disponible", bien.isDisponible())
                .append("agentId", bien.getAgentId())
                .append("dateCreation", bien.getDateCreation())
                .append("caracteristiques", bien.getCaracteristiques());

        if (bien.getDateModification() != null) {
            doc.append("dateModification", bien.getDateModification());
        }

        return doc;
    }
}