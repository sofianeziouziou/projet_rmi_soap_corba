package com.immobilier.rest;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import java.io.*;
import java.util.*;
import com.google.gson.Gson;
@WebServlet("/utilisateurs")
public class UtilisateursServlet extends HttpServlet {
    private List<Utilisateur> utilisateurs = new ArrayList<>(
        Arrays.asList(new Utilisateur(1,"Alice","agent"), new Utilisateur(2,"Bob","acheteur"))
    );
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(utilisateurs));
    }
}
