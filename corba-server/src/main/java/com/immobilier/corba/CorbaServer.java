package com.immobilier.corba;

import java.util.ArrayList;
import java.util.List;

public class CorbaServer {
    private List<Bien> biens = new ArrayList<>();

    public long addBien(Bien b) {
        biens.add(b);
        return b.getId();
    }

    public Bien[] listBiens() {
        return biens.toArray(new Bien[0]);
    }

    public boolean checkDisponibilite(long id) {
        return biens.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .map(Bien::isDisponible)
                .orElse(false);
    }

    public static void main(String[] args) {
        CorbaServer server = new CorbaServer();
        System.out.println("CORBA Server ready!");

        // Exemples de test
        server.addBien(new Bien(1, "Appartement centre-ville", "3 chambres, 2 salles de bain"));
        server.addBien(new Bien(2, "Maison banlieue", "4 chambres, jardin"));

        System.out.println("Liste des biens :");
        for (Bien b : server.listBiens()) {
            System.out.println(b.getId() + " - " + b.getTitre() + " (" + b.isDisponible() + ")");
        }

        System.out.println("Disponibilité du bien 1 : " + server.checkDisponibilite(1));
    }
}
