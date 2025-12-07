package com.immobilier.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService
public interface ContractService {
    @WebMethod
    String generateContract(@WebParam(name="bienId") int bienId, @WebParam(name="acheteurId") long acheteurId);
}
