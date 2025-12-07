package com.immobilier.core;

import java.util.Date;

public class Contract {
    private long id;
    private long bienId;
    private long acheteurId;
    private long agentId;
    private double montant;
    private String statut; // en_cours, signe, annule, finalise
    private Date dateCreation;
    private Date dateSignature;
    private Date dateFinalisatio;
    private String typeContrat; // vente, location, promesse
    private String conditionsParticulieres;
    private double commission; // Commission de l'agent
    private String modesPaiement; // comptant, credit, mixte
    private String remarques;

    // Constructeur vide - OBLIGATOIRE
    public Contract() {
        this.dateCreation = new Date();
        this.statut = "en_cours";
    }

    // Constructeur avec paramètres essentiels
    public Contract(long bienId, long acheteurId, double montant) {
        this();
        this.bienId = bienId;
        this.acheteurId = acheteurId;
        this.montant = montant;
    }

    // Constructeur complet
    public Contract(long id, long bienId, long acheteurId, long agentId,
                    double montant, String statut, String typeContrat) {
        this();
        this.id = id;
        this.bienId = bienId;
        this.acheteurId = acheteurId;
        this.agentId = agentId;
        this.montant = montant;
        this.statut = statut;
        this.typeContrat = typeContrat;
    }

    // Getters
    public long getId() { return id; }
    public long getBienId() { return bienId; }
    public long getAcheteurId() { return acheteurId; }
    public long getAgentId() { return agentId; }
    public double getMontant() { return montant; }
    public String getStatut() { return statut; }
    public Date getDateCreation() { return dateCreation; }
    public Date getDateSignature() { return dateSignature; }
    public Date getDateFinalisatio() { return dateFinalisatio; }
    public String getTypeContrat() { return typeContrat; }
    public String getConditionsParticulieres() { return conditionsParticulieres; }
    public double getCommission() { return commission; }
    public String getModesPaiement() { return modesPaiement; }
    public String getRemarques() { return remarques; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setBienId(long bienId) { this.bienId = bienId; }
    public void setAcheteurId(long acheteurId) { this.acheteurId = acheteurId; }
    public void setAgentId(long agentId) { this.agentId = agentId; }
    public void setMontant(double montant) { this.montant = montant; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    public void setDateSignature(Date dateSignature) { this.dateSignature = dateSignature; }
    public void setDateFinalisatio(Date dateFinalisatio) { this.dateFinalisatio = dateFinalisatio; }
    public void setTypeContrat(String typeContrat) { this.typeContrat = typeContrat; }
    public void setConditionsParticulieres(String conditionsParticulieres) {
        this.conditionsParticulieres = conditionsParticulieres;
    }
    public void setCommission(double commission) { this.commission = commission; }
    public void setModesPaiement(String modesPaiement) { this.modesPaiement = modesPaiement; }
    public void setRemarques(String remarques) { this.remarques = remarques; }

    // Méthodes métier

    /**
     * Calcule la commission de l'agent (par défaut 5% du montant)
     */
    public double calculerCommission() {
        if (commission > 0) {
            return commission;
        }
        return montant * 0.05; // 5% par défaut
    }

    /**
     * Signe le contrat
     */
    public void signer() {
        this.statut = "signe";
        this.dateSignature = new Date();
    }

    /**
     * Finalise le contrat (transaction terminée)
     */
    public void finaliser() {
        this.statut = "finalise";
        this.dateFinalisatio = new Date();
    }

    /**
     * Annule le contrat
     */
    public void annuler(String motif) {
        this.statut = "annule";
        this.remarques = (remarques != null ? remarques + "\n" : "") +
                "Annulé: " + motif + " (" + new Date() + ")";
    }

    /**
     * Vérifie si le contrat est actif
     */
    public boolean isActif() {
        return "en_cours".equals(statut) || "signe".equals(statut);
    }

    /**
     * Vérifie si le contrat est finalisé
     */
    public boolean isFinalise() {
        return "finalise".equals(statut);
    }

    /**
     * Vérifie si le contrat est annulé
     */
    public boolean isAnnule() {
        return "annule".equals(statut);
    }

    /**
     * Obtient le nombre de jours depuis la création
     */
    public long getJoursDepuisCreation() {
        long diff = new Date().getTime() - dateCreation.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    /**
     * Vérifie si le contrat est complet (toutes les infos nécessaires)
     */
    public boolean isComplet() {
        return bienId > 0 &&
                acheteurId > 0 &&
                montant > 0 &&
                statut != null && !statut.isEmpty();
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", bienId=" + bienId +
                ", acheteurId=" + acheteurId +
                ", agentId=" + agentId +
                ", montant=" + montant +
                ", statut='" + statut + '\'' +
                ", typeContrat='" + typeContrat + '\'' +
                ", dateCreation=" + dateCreation +
                ", dateSignature=" + dateSignature +
                '}';
    }

    /**
     * Obtient un résumé du contrat
     */
    public String getResume() {
        return String.format("Contrat #%d - Bien #%d - %.0f € - %s",
                id, bienId, montant, statut);
    }

    /**
     * Constants pour les statuts
     */
    public static class Statut {
        public static final String EN_COURS = "en_cours";
        public static final String SIGNE = "signe";
        public static final String ANNULE = "annule";
        public static final String FINALISE = "finalise";
    }

    /**
     * Constants pour les types de contrat
     */
    public static class Type {
        public static final String VENTE = "vente";
        public static final String LOCATION = "location";
        public static final String PROMESSE = "promesse";
    }
}