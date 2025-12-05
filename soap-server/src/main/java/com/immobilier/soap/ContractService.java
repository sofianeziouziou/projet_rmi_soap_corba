package com.immobilier.soap;

import jakarta.jws.WebService;
import jakarta.xml.ws.Endpoint;

@WebService
public class ContractService {
    public String generateContract(long bienId, long acheteurId) {
        return "<contract><bienId>" + bienId + "</bienId><acheteurId>" + acheteurId + "</acheteurId></contract>";
    }

    public String signContract(long contractId) {
        return "<signedContract id='" + contractId + "'/>";
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8081/contracts", new ContractService());
        System.out.println("SOAP Service started at http://localhost:8081/contracts");
    }
}
