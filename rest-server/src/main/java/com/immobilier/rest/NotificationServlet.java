package com.immobilier.rest;

import com.google.gson.Gson;
import com.immobilier.producer.Producer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet("/notifications/*")
public class NotificationServlet extends HttpServlet {
    private static final List<String> notifications = new CopyOnWriteArrayList<>();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        List<Object> list = new ArrayList<>();
        for(String n : notifications) list.add(new Object(){public final String message = n;});
        resp.getWriter().write(gson.toJson(list));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Recevoir notification de front et stocker
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        String json = sb.toString();
        var map = gson.fromJson(json, java.util.Map.class);
        String msg = (String) map.get("message");
        if(msg != null){
            notifications.add(msg);
            // Envoyer via JMS
            Producer.sendNotification(msg);
            resp.getWriter().write("{\"success\":true}");
        } else {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"message_required\"}");
        }
    }
}
