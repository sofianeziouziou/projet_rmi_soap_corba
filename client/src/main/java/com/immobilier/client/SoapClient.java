package com.immobilier.client;

import org.apache.hc.client5.http.fluent.Request;

public class SoapClient {
    private final String endpoint;

    public SoapClient(String endpoint) { this.endpoint = endpoint; }

    public String createContract(int bienId, int acheteurId) throws Exception {
        // ContractServiceImpl published at http://localhost:8081/contracts
        String url = endpoint + "?bienId=" + bienId + "&acheteurId=" + acheteurId;
        return Request.get(url).execute().returnContent().asString();
    }
}
