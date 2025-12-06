package com.immobilier.corba;

import Immobilier.Bien;
import Immobilier.BienServicePOA;

import java.util.ArrayList;
import java.util.List;

public class BienServiceImpl extends BienServicePOA {
    private List<Bien> biens = new ArrayList<>();
    private long currentId = 1;

    @Override
    public int addBien(Bien bien) {
        bien.id = (int) currentId++;
        bien.disponible = true;
        biens.add(bien);
        System.out.println("Bien ajouté: " + bien.id + " - " + bien.titre);
        return bien.id;
    }

    @Override
    public Bien[] listBiens() {
        return biens.toArray(new Bien[0]);
    }

    @Override
    public boolean checkDisponibilite(int id) {
        return biens.stream()
                .anyMatch(b -> b.id == id && b.disponible);
    }
}