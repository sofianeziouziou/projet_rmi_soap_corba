package com.immobilier.rest;

import jakarta.servlet.http.*;
import java.io.*;
import com.google.gson.Gson;

public class UtilisateursServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo(); // /login or /register
        resp.setContentType("application/json;charset=UTF-8");

        try {
            if("/login".equalsIgnoreCase(path)) {
                Utilisateur in = gson.fromJson(req.getReader(), Utilisateur.class);
                Utilisateur found = dao.findByEmailAndPassword(in.getEmail(), in.getPassword());
                if(found != null) {
                    resp.setStatus(200);
                    resp.getWriter().write(gson.toJson(found));
                } else {
                    resp.setStatus(404);
                    resp.getWriter().write("{\"error\":\"invalid\"}");
                }
                return;
            }

            if("/register".equalsIgnoreCase(path)) {
                Utilisateur in = gson.fromJson(req.getReader(), Utilisateur.class);
                if(dao.findByEmail(in.getEmail()) != null) {
                    resp.setStatus(409);
                    resp.getWriter().write("{\"error\":\"exists\"}");
                    return;
                }
                Utilisateur created = dao.create(in);
                if(created != null) {
                    resp.setStatus(201);
                    resp.getWriter().write(gson.toJson(created));
                } else {
                    resp.setStatus(500);
                    resp.getWriter().write("{\"error\":\"fail\"}");
                }
                return;
            }

            resp.setStatus(400);
        } catch(Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"server\"}");
        }
    }
}
