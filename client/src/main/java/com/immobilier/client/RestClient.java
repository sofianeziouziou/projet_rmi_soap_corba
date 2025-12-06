package com.immobilier.client;

import com.google.gson.Gson;
import com.immobilier.rest.Utilisateur;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.core5.http.ContentType;

public class RestClient {
    private final String base;
    private final Gson gson = new Gson();

    public RestClient(String base) {
        this.base = base;
    }

    public Utilisateur login(String email, String password) throws Exception {
        // Création de l'utilisateur pour login
        Utilisateur in = new Utilisateur();
        in.setEmail(email);
        in.setPassword(password);

        String json = gson.toJson(in);

        Content c = Request.post(base + "/users/login")
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute().returnContent();

        return gson.fromJson(c.asString(), Utilisateur.class);
    }

    public Utilisateur register(Utilisateur u) throws Exception {
        String json = gson.toJson(u);
        Content c = Request.post(base + "/users/register")
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute().returnContent();

        return gson.fromJson(c.asString(), Utilisateur.class);
    }
}
