package com.immobilier.client;

import org.omg.CORBA.ORB;
import Immobilier.Bien;
import Immobilier.BienService;
import Immobilier.BienServiceHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class ClientConsole {
    public static void main(String[] args) {
        System.out.println("=== Client Console Immobilier ===\n");

        try {
            // Configuration JacORB
            Properties props = new Properties();
            props.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
            props.setProperty("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");

            ORB orb = ORB.init(args, props);

            // Lire l'IOR depuis le fichier créé par le serveur
            System.out.println("📂 Lecture de l'IOR...");

            // Le fichier IOR est dans le working directory du serveur
            // Essayez d'abord le répertoire courant, sinon le répertoire parent
            String ior = null;
            String[] possiblePaths = {
                    "BienService.ior",
                    "../corba-server/BienService.ior",
                    "corba-server/BienService.ior"
            };

            for (String path : possiblePaths) {
                try {
                    ior = readIORFromFile(path);
                    System.out.println("✅ IOR trouvé dans: " + path);
                    break;
                } catch (Exception e) {
                    // Continuer avec le prochain chemin
                }
            }

            if (ior == null) {
                throw new RuntimeException("Fichier BienService.ior introuvable dans les emplacements attendus");
            }

            System.out.println("🔗 Connexion via IOR...");
            org.omg.CORBA.Object objRef = orb.string_to_object(ior);

            System.out.println("🔍 Tentative de narrow...");
            BienService bienService = BienServiceHelper.narrow(objRef);

            if (bienService == null) {
                throw new RuntimeException("Le narrow a retourné null");
            }

            System.out.println("✅ Connecté au service CORBA\n");

            // Tester les opérations
            testOperations(bienService);

        } catch (Exception e) {
            System.err.println("\n❌ Erreur: " + e.getMessage());
            System.err.println("\n⚠️  Vérifications:");
            System.err.println("   1. Le serveur CORBA est-il démarré ?");
            System.err.println("   2. Le fichier BienService.ior existe-t-il ?");
            e.printStackTrace();
        }
    }

    private static String readIORFromFile(String filename) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            return br.readLine();
        }
    }

    private static void testOperations(BienService bienService) {
        try {
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("   TESTS DU SERVICE CORBA - BIENS");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

            // Test 1: Ajouter des biens
            System.out.println("📝 Test 1: Ajout de biens immobiliers\n");

            Bien b1 = new Bien();
            b1.titre = "Appartement Centre-Ville";
            b1.description = "3 chambres, 2 salles de bain, balcon";
            b1.disponible = true;
            int id1 = bienService.addBien(b1);
            System.out.println("  ✅ Bien #" + id1 + " ajouté");

            Bien b2 = new Bien();
            b2.titre = "Maison avec Jardin";
            b2.description = "4 chambres, jardin 500m², garage";
            b2.disponible = true;
            int id2 = bienService.addBien(b2);
            System.out.println("  ✅ Bien #" + id2 + " ajouté");

            Bien b3 = new Bien();
            b3.titre = "Studio Moderne";
            b3.description = "25m², proche métro, meublé";
            b3.disponible = true;
            int id3 = bienService.addBien(b3);
            System.out.println("  ✅ Bien #" + id3 + " ajouté");

            Bien b4 = new Bien();
            b4.titre = "Villa Bord de Mer";
            b4.description = "6 chambres, piscine, vue mer";
            b4.disponible = true;
            int id4 = bienService.addBien(b4);
            System.out.println("  ✅ Bien #" + id4 + " ajouté");

            // Test 2: Lister tous les biens
            System.out.println("\n📋 Test 2: Liste complète des biens\n");
            Bien[] biens = bienService.listBiens();
            System.out.println("  📊 Nombre total de biens: " + biens.length + "\n");

            for (Bien b : biens) {
                System.out.println("  ╔════════════════════════════════════════");
                System.out.println("  ║ 🏠 Bien #" + b.id);
                System.out.println("  ╠════════════════════════════════════════");
                System.out.println("  ║ Titre       : " + b.titre);
                System.out.println("  ║ Description : " + b.description);
                System.out.println("  ║ Disponible  : " + (b.disponible ? "✅ Oui" : "❌ Non"));
                System.out.println("  ╚════════════════════════════════════════\n");
            }

            // Test 3: Vérifier la disponibilité
            System.out.println("🔍 Test 3: Vérification de disponibilité\n");

            boolean dispo1 = bienService.checkDisponibilite(id1);
            System.out.println("  Bien #" + id1 + " : " + (dispo1 ? "✅ Disponible" : "❌ Non disponible"));

            boolean dispo2 = bienService.checkDisponibilite(id2);
            System.out.println("  Bien #" + id2 + " : " + (dispo2 ? "✅ Disponible" : "❌ Non disponible"));

            boolean dispo99 = bienService.checkDisponibilite(99);
            System.out.println("  Bien #99 (inexistant) : " + (dispo99 ? "✅ Disponible" : "❌ Non disponible"));

            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("   ✅ TOUS LES TESTS CORBA RÉUSSIS !");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        } catch (Exception e) {
            System.err.println("\n❌ Erreur lors des tests: " + e.getMessage());
            e.printStackTrace();
        }
    }
}