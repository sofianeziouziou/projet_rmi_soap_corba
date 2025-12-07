package com.immobilier.rest;

import com.google.gson.Gson;
import com.immobilier.core.Bien;
import com.immobilier.core.MongoDBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/biens/*")
public class BiensServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final MongoCollection<Document> coll;

    public BiensServlet() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        coll = db.getCollection("biens");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // GET /biens -> list biens
        resp.setContentType("application/json;charset=UTF-8");
        try {
            List<Bien> list = new ArrayList<>();
            for (Document d : coll.find()) {
                Bien b = new Bien();
                b.setId(d.getLong("_id"));
                b.setTitre(d.getString("titre"));
                b.setDescription(d.getString("description"));
                b.setPrix(d.getDouble("prix"));
                b.setDisponible(d.getBoolean("disponible", false));
                b.setAgentId(d.getLong("agentId"));
                list.add(b);
            }
            resp.getWriter().write(gson.toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"server\"}");
        }
    }
}
