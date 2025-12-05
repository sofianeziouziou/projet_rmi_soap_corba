package com.immobilier.rest;
public class Annonce {
    private long id;
    private long bienId;
    private String description;
    public Annonce(long id,long bienId,String description){this.id=id;this.bienId=bienId;this.description=description;}
    public long getId(){return id;} public long getBienId(){return bienId;} public String getDescription(){return description;}
}
