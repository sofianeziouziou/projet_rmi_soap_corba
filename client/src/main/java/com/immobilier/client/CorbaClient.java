package com.immobilier.client;

import Immobilier.*;
import org.omg.CORBA.*;

public class CorbaClient {
    public static void main(String[] args) throws Exception {

        ORB orb = ORB.init(args, null);

        String ior = java.nio.file.Files.readString(java.nio.file.Path.of("ior.txt"));
        org.omg.CORBA.Object obj = orb.string_to_object(ior);

        BienService service = BienServiceHelper.narrow(obj);

        System.out.println("== Test listBiens() ==");
        for (Bien b : service.listBiens()) {
            System.out.println(b.titre);
        }

        System.out.println("Disponibilité : " + service.checkDisponibilite(1));
    }
}
