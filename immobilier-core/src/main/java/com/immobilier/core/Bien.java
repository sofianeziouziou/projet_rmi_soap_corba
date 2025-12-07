package com.immobilier.core;

public class Bien {
    private long id;
    private String titre;
    private String description;
    private double prix;
    private boolean disponible;
    private long agentId;

    public Bien() {}

    // getters / setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public long getAgentId() { return agentId; }
    public void setAgentId(long agentId) { this.agentId = agentId; }
}
