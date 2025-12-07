package com.immobilier.core;

public class Utilisateur {
    private long id;
    private String nom;
    private String email;
    private String password;
    private String role;

    // Constructeur vide - OBLIGATOIRE
    public Utilisateur() {
    }

    // Constructeur simple
    public Utilisateur(long id, String nom, String role) {
        this.id = id;
        this.nom = nom;
        this.role = role;
    }

    // Constructeur complet
    public Utilisateur(long id, String nom, String email, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }

    // Getters
    public long getId() { return id; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "Utilisateur{id=" + id + ", nom='" + nom + "', email='" + email + "', role='" + role + "'}";
    }
}