package com.immobilier.rest;

import com.google.gson.Gson;
import com.immobilier.core.MongoDBConnection;
import com.immobilier.producer.Producer;
import com.immobilier.soap.ContractService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.xml.ws.Service;
import org.bson.Document;

import javax.xml.namespace.QName;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URL;
import java.security.Provider;

@WebServlet("/acheterBien")
public class AcheterBienServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final MongoCollection<Document> biens;

    public AcheterBienServlet() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        biens = db.getCollection("biens");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            class Req { public int bienId; public long acheteurId; }
            Req body = gson.fromJson(req.getReader(), Req.class);

            Document bien = biens.find(new Document("_id", (long) body.bienId)).first();
            if(bien == null){
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"bien_not_found\"}");
                return;
            }

            // Appel SOAP ContractService
            URL wsdlURL = new URL("http://localhost:8081/contracts?wsdl");
            QName SERVICE_NAME = new QName("http://soap.immobilier.com/", "ContractServiceImplService");
            Service service = Service.create(wsdlURL, SERVICE_NAME);
            ContractService contractPort = service.getPort(ContractService.class);

            String contractXML = contractPort.generateContract(body.bienId, body.acheteurId);

            // Marquer le bien comme non disponible
            biens.updateOne(new Document("_id", (long) body.bienId),
                    new Document("$set", new Document("disponible", false)));

            // Notification
            Producer.sendNotification("Bien acheté: " + bien.getString("titre"));

            resp.getWriter().write("{\"contract\":" + gson.toJson(contractXML) + "}");
        } catch(Exception e){
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Erreur lors de la création du contrat\"}");
        }
    }
}
