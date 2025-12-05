package com.immobilier.rest;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import java.io.*;
import java.util.*;
import com.google.gson.Gson;
@WebServlet("/annonces")
public class AnnoncesServlet extends HttpServlet {
    private List<Annonce> annonces = new ArrayList<>();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(annonces));
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        BufferedReader reader=req.getReader();
        Annonce a=new Gson().fromJson(reader,Annonce.class);
        annonces.add(a);
        resp.setStatus(201);
    }
}
