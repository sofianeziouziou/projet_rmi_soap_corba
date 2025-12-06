package com.immobilier.client;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.omg.CORBA.ORB;

import Immobilier.BienService;
import Immobilier.BienServiceHelper;
import Immobilier.Bien;

public class CorbaClient {

    private BienService service;

    /**
     * Initialise le client CORBA avec l'IOR fourni dans le fichier.
     */
    public CorbaClient(String iorPath) throws Exception {
        // Lire l'IOR depuis le fichier
        String ior = new String(Files.readAllBytes(Paths.get(iorPath))).trim();

        // Initialiser l'ORB
        ORB orb = ORB.init(new String[]{}, null);

        // Convertir la chaîne IOR en objet CORBA
        org.omg.CORBA.Object obj = orb.string_to_object(ior);

        // Récupérer l'interface BienService
        service = BienServiceHelper.narrow(obj);
    }

    /**
     * Liste tous les biens.
     */
    public Bien[] listBiens() {
        return service.listBiens();
    }

    /**
     * Ajoute un bien et retourne son ID.
     */
    public int addBien(Bien b) {
        return service.addBien(b);
    }

    /**
     * Vérifie la disponibilité d'un bien par son ID.
     */
    public boolean checkDisponibilite(int id) {
        return service.checkDisponibilite(id);
    }

    /**
     * Exemple d'utilisation du client.
     */
    public static void main(String[] args) {
        try {
            // Chemin vers le fichier contenant l'IOR
            CorbaClient client = new CorbaClient("ior.txt");

            // Lister les biens existants
            System.out.println("Liste des biens :");
            for (Bien b : client.listBiens()) {
                System.out.println("ID: " + b.id + ", Titre: " + b.titre + ", Disponible: " + b.disponible);
            }

            // Ajouter un nouveau bien
            Bien newBien = new Bien();
            newBien.titre = "Appartement Test";
            newBien.description = "Un appartement à louer";
            newBien.disponible = true;

            int newId = client.addBien(newBien);
            System.out.println("Bien ajouté avec ID: " + newId);

            // Vérifier la disponibilité
            boolean dispo = client.checkDisponibilite(newId);
            System.out.println("Disponibilité du bien ajouté: " + dispo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
