package com.immobilier.corba;

import Immobilier.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

public class CorbaServer {

    public static void main(String[] args) throws Exception {

        ORB orb = ORB.init(args, null);

        POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        rootPOA.the_POAManager().activate();

        BienServiceImpl bienService = new BienServiceImpl();
        org.omg.CORBA.Object ref = rootPOA.servant_to_reference(bienService);
        BienService href = BienServiceHelper.narrow(ref);

        String ior = orb.object_to_string(href);

        System.out.println("🚀 JacORB CORBA Server démarré !");
        System.out.println("IOR = " + ior);

        // écrit dans un fichier pour le client
        java.nio.file.Files.writeString(java.nio.file.Path.of("ior.txt"), ior);

        orb.run();
    }
}
