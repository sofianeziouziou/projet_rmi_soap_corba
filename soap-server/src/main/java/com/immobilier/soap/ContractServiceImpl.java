package com.immobilier.soap;

// Après (jakarta)
import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
@WebService(endpointInterface = "com.immobilier.soap.ContractService")
public class ContractServiceImpl implements ContractService {

    @Override
    public String createContract(int bienId, int acheteurId) {
        // ton code pour créer le contrat
        return "Contrat créé pour bien " + bienId + " et acheteur " + acheteurId;
    }
}
