package com.immobilier.core;

public class Utilisateur {
    private long id;
    private String nom;
    private String email;
    private String password;
    private String role; // "agent" or "acheteur"

    public Utilisateur() {}

    public Utilisateur(long id, String nom, String email, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }

    // getters / setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "Utilisateur{" + "id=" + id + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", role='" + role + '\'' + '}';
    }
}
