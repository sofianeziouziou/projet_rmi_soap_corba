package com.immobilier.core;

import java.util.Date;

public class Bien {
    private long id;
    private String titre;
    private String description;
    private double prix;
    private String type; // appartement, maison, terrain, etc.
    private double surface; // en m²
    private int nbPieces;
    private int nbChambres;
    private int nbSallesBain;
    private String adresse;
    private String ville;
    private String codePostal;
    private boolean disponible;
    private long agentId;
    private Date dateCreation;
    private Date dateModification;
    private String[] images; // URLs des images
    private String caracteristiques; // parking, jardin, balcon, etc.

    // Constructeur vide - OBLIGATOIRE pour la désérialisation
    public Bien() {
        this.dateCreation = new Date();
        this.disponible = true;
    }

    // Constructeur avec paramètres essentiels
    public Bien(String titre, String description, double prix, long agentId) {
        this();
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.agentId = agentId;
    }

    // Constructeur complet
    public Bien(long id, String titre, String description, double prix,
                String type, double surface, String adresse, String ville,
                long agentId, boolean disponible) {
        this();
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.type = type;
        this.surface = surface;
        this.adresse = adresse;
        this.ville = ville;
        this.agentId = agentId;
        this.disponible = disponible;
    }

    // Getters
    public long getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public double getPrix() { return prix; }
    public String getType() { return type; }
    public double getSurface() { return surface; }
    public int getNbPieces() { return nbPieces; }
    public int getNbChambres() { return nbChambres; }
    public int getNbSallesBain() { return nbSallesBain; }
    public String getAdresse() { return adresse; }
    public String getVille() { return ville; }
    public String getCodePostal() { return codePostal; }
    public boolean isDisponible() { return disponible; }
    public long getAgentId() { return agentId; }
    public Date getDateCreation() { return dateCreation; }
    public Date getDateModification() { return dateModification; }
    public String[] getImages() { return images; }
    public String getCaracteristiques() { return caracteristiques; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String description) { this.description = description; }
    public void setPrix(double prix) { this.prix = prix; }
    public void setType(String type) { this.type = type; }
    public void setSurface(double surface) { this.surface = surface; }
    public void setNbPieces(int nbPieces) { this.nbPieces = nbPieces; }
    public void setNbChambres(int nbChambres) { this.nbChambres = nbChambres; }
    public void setNbSallesBain(int nbSallesBain) { this.nbSallesBain = nbSallesBain; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setVille(String ville) { this.ville = ville; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public void setAgentId(long agentId) { this.agentId = agentId; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    public void setDateModification(Date dateModification) { this.dateModification = dateModification; }
    public void setImages(String[] images) { this.images = images; }
    public void setCaracteristiques(String caracteristiques) { this.caracteristiques = caracteristiques; }

    // Méthode utilitaire pour mettre à jour la date de modification
    public void updateModificationDate() {
        this.dateModification = new Date();
    }

    // Méthode pour marquer comme vendu
    public void marquerCommeVendu() {
        this.disponible = false;
        this.updateModificationDate();
    }

    // Méthode pour rendre disponible
    public void rendreDisponible() {
        this.disponible = true;
        this.updateModificationDate();
    }

    @Override
    public String toString() {
        return "Bien{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", type='" + type + '\'' +
                ", surface=" + surface +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", disponible=" + disponible +
                ", agentId=" + agentId +
                '}';
    }

    // Méthode pour vérifier si le bien est complet
    public boolean isComplet() {
        return titre != null && !titre.isEmpty() &&
                description != null && !description.isEmpty() &&
                prix > 0 &&
                agentId > 0;
    }

    // Méthode pour obtenir un résumé court
    public String getResume() {
        return String.format("%s - %.0f m² - %.0f €",
                titre, surface, prix);
    }
}