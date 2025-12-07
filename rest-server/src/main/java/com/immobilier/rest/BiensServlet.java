package com.immobilier.rest;

import com.immobilier.corba.BienService; // ton interface CORBA
import com.immobilier.corba.BienServiceHelper;
import com.immobilier.corba.Immobilier.Bien;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/biens/*")
public class BiensServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private BienService corbaService;

    @Override
    public void init() {
        try {
            // Récupération de l'IOR CORBA depuis fichier
            String ior = getServletContext().getRealPath("/BienService.ior");
            corbaService = BienServiceHelper.narrow(
                    org.omg.CORBA.ORB.init().string_to_object(new java.io.FileReader(ior).readLine())
            );
        } catch (Exception e) {
            e.printStackTrace();
            corbaService = null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        if(corbaService == null){
            resp.getWriter().write("{\"error\":\"CORBA service not available\"}");
            return;
        }

        try {
            Bien[] biens = corbaService.listBiens();
            List<Object> list = new ArrayList<>();
            for(Bien b : biens){
                list.add(new Object(){
                    public final long id = b.id;
                    public final String titre = b.titre;
                    public final String description = b.description;
                    public final double prix = b.prix;
                    public final boolean disponible = b.disponible;
                });
            }
            resp.getWriter().write(gson.toJson(list));
        } catch(Exception e){
            e.printStackTrace();
            resp.getWriter().write("{\"error\":\"server\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        if(corbaService == null){
            resp.getWriter().write("{\"error\":\"CORBA service not available\"}");
            return;
        }

        try {
            Bien b = gson.fromJson(req.getReader(), Bien.class);
            long id = corbaService.addBien(b);
            resp.getWriter().write("{\"success\":true, \"id\":" + id + "}");
        } catch(Exception e){
            e.printStackTrace();
            resp.getWriter().write("{\"error\":\"server\"}");
        }
    }
}
