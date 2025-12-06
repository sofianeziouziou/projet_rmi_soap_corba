package com.immobilier.soap;

// Après (jakarta)
import jakarta.jws.WebService;
import jakarta.jws.WebMethod;

@WebService
public interface ContractService {
    @WebMethod
    String createContract(int bienId, int acheteurId);
}
