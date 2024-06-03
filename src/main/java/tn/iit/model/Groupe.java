package tn.iit.model;

public class Groupe {
    private int id;
    private String nom;
    private int nbre;

    public Groupe(int id, String nom, int nbre) {
        this.id = id;
        this.nom = nom;
        this.nbre = nbre;
    }

    public Groupe(String nom, int nbre) {
        this.nom = nom;
        this.nbre = nbre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbre() {
        return nbre;
    }

    public void setNbre(int nbre) {
        this.nbre = nbre;
    }



}
