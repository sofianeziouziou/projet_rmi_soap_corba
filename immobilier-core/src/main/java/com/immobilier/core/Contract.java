package com.immobilier.core;

import java.util.Date;

public class Contract {
    private long id;
    private long bienId;
    private long acheteurId;
    private Date dateCreation;
    private String statut;
    private double montant;

    public Contract() {}

    // getters/setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getBienId() { return bienId; }
    public void setBienId(long bienId) { this.bienId = bienId; }
    public long getAcheteurId() { return acheteurId; }
    public void setAcheteurId(long acheteurId) { this.acheteurId = acheteurId; }
    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
}
