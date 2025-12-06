package com.immobilier.soap;

// Changez javax.xml.ws en jakarta.xml.ws
import jakarta.xml.ws.Endpoint;

public class SoapServer {
    public static void main(String[] args) {
        try {
            String address = "http://localhost:8081/contracts";
            Endpoint.publish(address, new ContractServiceImpl());

            System.out.println("SOAP Server started at: " + address);
            System.out.println("WSDL available at: " + address + "?wsdl");

            Thread.currentThread().join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}