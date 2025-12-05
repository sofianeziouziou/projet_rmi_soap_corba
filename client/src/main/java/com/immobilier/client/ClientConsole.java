// ClientConsole.java
package com.immobilier.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.omg.CORBA.ORB;

import Immobilier.Bien;
import Immobilier.BienService;
import Immobilier.BienServiceHelper;

public class ClientConsole {
    public static void main(String[] args) {
        System.out.println("Client console CORBA ready!");

        try {
            System.setProperty("org.glassfish.gmbal.no-mbean", "true");

            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object objRef =
                    orb.string_to_object("corbaloc:iiop:localhost:1050/BienService");

            BienService corbaServer = BienServiceHelper.narrow(objRef);

            // Créer un bien via le constructeur IDL
            Bien b1 = new Bien();
            b1.id = 1;
            b1.titre = "Appartement centre-ville";
            b1.description = "3 chambres, 2 salles de bain";
            b1.disponible = true;

            // Ajouter le bien via CORBA
            int id1 = corbaServer.addBien(b1);
            System.out.println("Bien ajouté via CORBA avec id = " + id1);

            // Lister les biens
            Bien[] biens = corbaServer.listBiens();
            System.out.println("Liste des biens CORBA :");
            for (Bien b : biens) {
                System.out.println(b.id + " - " + b.titre + " (" + b.disponible + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
