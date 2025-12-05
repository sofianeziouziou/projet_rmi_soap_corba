package com.immobilier.corba;

public class Bien {
    public long id;
    public String titre;
    public String description;
    public boolean disponible = true;

    // Constructeur vide nécessaire pour CORBA
    public Bien(int i, String s, String string, boolean b) {}

    public Bien(long id, String titre, String description) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.disponible = true;
    }

    public long getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
