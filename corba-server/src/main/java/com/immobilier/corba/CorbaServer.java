package com.immobilier.corba;

import Immobilier.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;

public class CorbaServer {

    public static void main(String[] args) {
        try {
            System.out.println("🚀 Démarrage du serveur CORBA...\n");

            // Configuration JacORB
            Properties props = new Properties();
            props.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
            props.setProperty("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
            props.setProperty("jacorb.implname", "BienServiceImpl");

            // Initialiser l'ORB
            ORB orb = ORB.init(args, props);

            // Obtenir le POA root et activer le POA Manager
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            // Créer le servant
            BienServiceImpl bienServiceImpl = new BienServiceImpl();

            // Enregistrer le servant avec le POA
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(bienServiceImpl);
            BienService bienService = BienServiceHelper.narrow(ref);

            // Obtenir la référence IOR
            String ior = orb.object_to_string(bienService);

            // Afficher l'IOR
            System.out.println("✅ Serveur CORBA démarré avec succès !");
            System.out.println("\n📋 IOR du service BienService :");
            System.out.println(ior);

            // Écrire l'IOR dans un fichier
            String iorFile = "BienService.ior";
            try (PrintWriter out = new PrintWriter(new FileWriter(iorFile))) {
                out.println(ior);
                System.out.println("\n💾 IOR écrit dans le fichier : " + iorFile);
            }

            System.out.println("\n🔄 Serveur en attente de requêtes...\n");
            System.out.println("Pour arrêter le serveur, appuyez sur Ctrl+C\n");

            // Lancer l'ORB (bloquant)
            orb.run();

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du démarrage du serveur CORBA :");
            e.printStackTrace();
            System.exit(1);
        }
    }
}