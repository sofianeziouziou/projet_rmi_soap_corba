package com.immobilier.corba;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManager;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Properties;

public class CorbaServer {
    public static void main(String[] args) {
        try {
            // Configuration JacORB ultra-stricte pour localhost
            Properties props = new Properties();
            props.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
            props.setProperty("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");

            // FORCER localhost dans TOUS les paramètres possibles
            props.setProperty("OAIAddr", "127.0.0.1");
            props.setProperty("OAPort", "1050");
            props.setProperty("jacorb.ior_proxy_host", "127.0.0.1");
            props.setProperty("jacorb.ior_proxy_port", "1050");
            props.setProperty("jacorb.orb.objectKeyMap.KeyName", "BienService");

            // Désactiver toutes les autres interfaces
            props.setProperty("jacorb.net.server_timeout", "0");
            props.setProperty("jacorb.connection.server.timeout", "0");
            props.setProperty("jacorb.security.support_ssl", "off");

            // IMPORTANT: Forcer vraiment l'utilisation de localhost
            System.setProperty("java.net.preferIPv4Stack", "true");

            System.out.println("🔧 Configuration CORBA:");
            System.out.println("   Adresse forcée: 127.0.0.1");
            System.out.println("   Port: 1050\n");

            // Initialiser ORB avec args pour override
            String[] orbArgs = {
                    "-OAIAddr", "127.0.0.1",
                    "-OAPort", "1050"
            };

            // Merger args avec orbArgs
            String[] allArgs = new String[args.length + orbArgs.length];
            System.arraycopy(orbArgs, 0, allArgs, 0, orbArgs.length);
            System.arraycopy(args, 0, allArgs, orbArgs.length, args.length);

            ORB orb = ORB.init(allArgs, props);
            System.out.println("✅ ORB initialisé");

            // POA
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            System.out.println("✅ RootPOA obtenu");

            POAManager poaManager = rootPOA.the_POAManager();
            poaManager.activate();
            System.out.println("✅ POA Manager activé");

            // Servant
            BienServiceImpl servant = new BienServiceImpl();
            System.out.println("✅ Servant créé");

            // Activer l'objet
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(servant);
            System.out.println("✅ Référence d'objet obtenue");

            // IOR
            String ior = orb.object_to_string(ref);

            // Vérifier l'IOR
            System.out.println("\n🔍 Vérification de l'IOR:");
            if (ior.contains("127.0.0.1") || ior.contains("7F000001")) { // 7F000001 = 127.0.0.1 en hexa
                System.out.println("   ✅ IOR contient localhost");
            } else {
                System.err.println("   ⚠️  IOR ne contient PAS localhost!");
                System.err.println("   IOR début: " + ior.substring(0, Math.min(200, ior.length())));
            }

            // Sauvegarder
            try (PrintWriter out = new PrintWriter(new FileOutputStream("BienService.ior"))) {
                out.println(ior);
            }
            System.out.println("   ✅ IOR sauvegardé");

            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("   🌐 SERVEUR CORBA OPÉRATIONNEL");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("🔗 Écoute sur: 127.0.0.1:1050");
            System.out.println("📡 IOR: BienService.ior");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("⏳ Serveur prêt pour les connexions...\n");

            orb.run();

        } catch (Exception e) {
            System.err.println("❌ Erreur fatale:");
            e.printStackTrace();
        }
    }
}