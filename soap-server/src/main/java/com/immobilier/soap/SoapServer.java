package com.immobilier.soap;

import jakarta.xml.ws.Endpoint;

public class SoapServer {
    public static void main(String[] args) {
        String url = "http://localhost:8081/contracts";
        Endpoint.publish(url, new ContractServiceImpl());
        System.out.println("SOAP Server started at: " + url);
        System.out.println("WSDL available at: " + url + "?wsdl");
    }
}
