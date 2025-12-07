package com.immobilier.rest;

import com.immobilier.corba.BienServiceImpl;
import Immobilier.Bien;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.google.gson.Gson;
import java.io.IOException;

@WebServlet("/users/*")
public class UtilisateursServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final UserDAO dao = new UserDAO();
    private final BienServiceImpl bienService = new BienServiceImpl(); // instance CORBA directe

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo(); // /login, /register, /addBien
        resp.setContentType("application/json;charset=UTF-8");

        try {
            if("/login".equalsIgnoreCase(path)){
                Utilisateur in = gson.fromJson(req.getReader(), Utilisateur.class);
                Utilisateur found = dao.findByEmailAndPassword(in.getEmail(), in.getPassword());
                if(found!=null) {
                    resp.setStatus(200);
                    resp.getWriter().write(gson.toJson(found));
                } else {
                    resp.setStatus(404);
                    resp.getWriter().write("{\"error\":\"invalid\"}");
                }
                return;
            }

            if("/register".equalsIgnoreCase(path)){
                Utilisateur in = gson.fromJson(req.getReader(), Utilisateur.class);
                if(dao.findByEmail(in.getEmail())!=null) {
                    resp.setStatus(409);
                    resp.getWriter().write("{\"error\":\"exists\"}");
                    return;
                }
                Utilisateur created = dao.create(in);
                if(created!=null) {
                    resp.setStatus(201);
                    resp.getWriter().write(gson.toJson(created));
                } else {
                    resp.setStatus(500);
                    resp.getWriter().write("{\"error\":\"fail\"}");
                }
                return;
            }

            // Nouveau endpoint pour ajouter un bien (pour agent)
            if("/addBien".equalsIgnoreCase(path)){
                Bien b = gson.fromJson(req.getReader(), Bien.class);
                int id = bienService.addBien(b);
                if(id != -1){
                    resp.setStatus(201);
                    resp.getWriter().write("{\"success\":true, \"id\":"+id+"}");
                } else {
                    resp.setStatus(500);
                    resp.getWriter().write("{\"error\":\"fail\"}");
                }
                return;
            }

            resp.setStatus(400);

        } catch(Exception e){
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"server\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo(); // /biens ou /biensAgent?agentId=X
        resp.setContentType("application/json;charset=UTF-8");

        try {
            if("/biens".equalsIgnoreCase(path)){
                Bien[] biens = bienService.listBiens();
                resp.getWriter().write(gson.toJson(biens));
                return;
            }

            if(path != null && path.startsWith("/biensAgent")){
                String query = req.getQueryString(); // agentId=1
                int agentId = Integer.parseInt(query.split("=")[1]);
                Bien[] biens = bienService.listBiensAgent(agentId);
                resp.getWriter().write(gson.toJson(biens));
                return;
            }

            // GET utilisateurs
            resp.getWriter().write(gson.toJson(dao.findAll()));

        } catch(Exception e){
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"server\"}");
        }
    }
}
