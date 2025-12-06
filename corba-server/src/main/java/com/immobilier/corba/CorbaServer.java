package com.immobilier.corba;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class CorbaServer {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            BienServiceImpl servant = new BienServiceImpl();
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(servant);

            String ior = orb.object_to_string(ref);
            String iorFilePath = System.getProperty("user.home") + File.separator + "BienService.ior";

            File iorFile = new File(iorFilePath);
            try (PrintWriter out = new PrintWriter(new FileOutputStream(iorFile))) {
                out.println(ior);
            }

            System.out.println("CORBA Server ready, IOR saved in: " + iorFile.getAbsolutePath());
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
