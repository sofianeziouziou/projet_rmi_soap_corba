package com.immobilier.rest;
public class Utilisateur {
    private long id;
    private String nom;
    private String role;
    public Utilisateur(long id, String nom, String role) { this.id=id; this.nom=nom; this.role=role; }
    public long getId(){return id;} public String getNom(){return nom;} public String getRole(){return role;}
}
