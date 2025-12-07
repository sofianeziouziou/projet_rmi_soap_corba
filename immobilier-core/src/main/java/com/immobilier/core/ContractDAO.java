package com.immobilier.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.*;

/**
 * DAO pour la gestion des contrats
 */
public class ContractDAO {
    private final MongoCollection<Document> collection;

    public ContractDAO(MongoDatabase database) {
        this.collection = database.getCollection("contrats");
    }

    /**
     * Crée un nouveau contrat
     */
    public Contract create(Contract contract) {
        try {
            long newId = collection.countDocuments() + 1;
            contract.setId(newId);

            Document doc = contractToDocument(contract);
            collection.insertOne(doc);

            System.out.println("✅ Contrat créé avec ID: " + newId);
            return contract;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création du contrat: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Trouve un contrat par son ID
     */
    public Contract findById(long id) {
        try {
            Document doc = collection.find(eq("_id", id)).first();
            return doc != null ? documentToContract(doc) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Liste tous les contrats
     */
    public List<Contract> findAll() {
        List<Contract> contrats = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                contrats.add(documentToContract(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contrats;
    }

    /**
     * Liste les contrats par acheteur
     */
    public List<Contract> findByAcheteur(long acheteurId) {
        List<Contract> contrats = new ArrayList<>();
        try {
            for (Document doc : collection.find(eq("acheteurId", acheteurId))) {
                contrats.add(documentToContract(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contrats;
    }

    /**
     * Liste les contrats par agent
     */
    public List<Contract> findByAgent(long agentId) {
        List<Contract> contrats = new ArrayList<>();
        try {
            for (Document doc : collection.find(eq("agentId", agentId))) {
                contrats.add(documentToContract(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contrats;
    }

    /**
     * Liste les contrats par bien
     */
    public List<Contract> findByBien(long bienId) {
        List<Contract> contrats = new ArrayList<>();
        try {
            for (Document doc : collection.find(eq("bienId", bienId))) {
                contrats.add(documentToContract(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contrats;
    }

    /**
     * Liste les contrats par statut
     */
    public List<Contract> findByStatut(String statut) {
        List<Contract> contrats = new ArrayList<>();
        try {
            for (Document doc : collection.find(eq("statut", statut))) {
                contrats.add(documentToContract(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contrats;
    }

    /**
     * Liste les contrats actifs
     */
    public List<Contract> findActifs() {
        List<Contract> contrats = new ArrayList<>();
        try {
            for (Document doc : collection.find(
                    or(eq("statut", "en_cours"), eq("statut", "signe")))) {
                contrats.add(documentToContract(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contrats;
    }

    /**
     * Met à jour un contrat
     */
    public boolean update(Contract contract) {
        try {
            Document doc = contractToDocument(contract);
            collection.replaceOne(eq("_id", contract.getId()), doc);
            System.out.println("✅ Contrat mis à jour: " + contract.getId());
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Signe un contrat
     */
    public boolean signer(long id) {
        try {
            Document update = new Document("$set",
                    new Document("statut", "signe")
                            .append("dateSignature", new java.util.Date()));

            collection.updateOne(eq("_id", id), update);
            System.out.println("✅ Contrat " + id + " signé");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finalise un contrat
     */
    public boolean finaliser(long id) {
        try {
            Document update = new Document("$set",
                    new Document("statut", "finalise")
                            .append("dateFinalisatio", new java.util.Date()));

            collection.updateOne(eq("_id", id), update);
            System.out.println("✅ Contrat " + id + " finalisé");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Annule un contrat
     */
    public boolean annuler(long id, String motif) {
        try {
            Document update = new Document("$set",
                    new Document("statut", "annule")
                            .append("remarques", "Annulé: " + motif));

            collection.updateOne(eq("_id", id), update);
            System.out.println("✅ Contrat " + id + " annulé");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un contrat
     */
    public boolean delete(long id) {
        try {
            collection.deleteOne(eq("_id", id));
            System.out.println("✅ Contrat supprimé: " + id);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la suppression: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calcule le total des ventes d'un agent
     */
    public double getTotalVentesAgent(long agentId) {
        double total = 0.0;
        try {
            for (Document doc : collection.find(
                    and(eq("agentId", agentId), eq("statut", "finalise")))) {
                total += doc.getDouble("montant");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * Convertit un Document MongoDB en objet Contract
     */
    private Contract documentToContract(Document doc) {
        Contract contract = new Contract();
        contract.setId(doc.getLong("_id"));
        contract.setBienId(doc.getLong("bienId"));
        contract.setAcheteurId(doc.getLong("acheteurId"));
        contract.setAgentId(doc.getLong("agentId") != null ? doc.getLong("agentId") : 0);
        contract.setMontant(doc.getDouble("montant"));
        contract.setStatut(doc.getString("statut"));
        contract.setDateCreation(doc.getDate("dateCreation"));
        contract.setDateSignature(doc.getDate("dateSignature"));
        contract.setDateFinalisatio(doc.getDate("dateFinalisatio"));
        contract.setTypeContrat(doc.getString("typeContrat"));
        contract.setConditionsParticulieres(doc.getString("conditionsParticulieres"));
        contract.setCommission(doc.getDouble("commission") != null ? doc.getDouble("commission") : 0.0);
        contract.setModesPaiement(doc.getString("modesPaiement"));
        contract.setRemarques(doc.getString("remarques"));

        return contract;
    }

    /**
     * Convertit un objet Contract en Document MongoDB
     */
    private Document contractToDocument(Contract contract) {
        Document doc = new Document()
                .append("_id", contract.getId())
                .append("bienId", contract.getBienId())
                .append("acheteurId", contract.getAcheteurId())
                .append("agentId", contract.getAgentId())
                .append("montant", contract.getMontant())
                .append("statut", contract.getStatut())
                .append("dateCreation", contract.getDateCreation())
                .append("typeContrat", contract.getTypeContrat())
                .append("conditionsParticulieres", contract.getConditionsParticulieres())
                .append("commission", contract.getCommission())
                .append("modesPaiement", contract.getModesPaiement())
                .append("remarques", contract.getRemarques());

        if (contract.getDateSignature() != null) {
            doc.append("dateSignature", contract.getDateSignature());
        }
        if (contract.getDateFinalisatio() != null) {
            doc.append("dateFinalisatio", contract.getDateFinalisatio());
        }

        return doc;
    }
}