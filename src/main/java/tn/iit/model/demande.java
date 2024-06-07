package tn.iit.model;
import java.util.Date;
public class demande {
    private int id;
    private String nomGroupe;
    private String nomMatiere;
    private int nbreEtudiant;
    private String Document;
    private Date date;
    private int etat;
    public demande(int id, String nomGroupe, String nomMatiere, int nbreEtudiant, String document, Date date, int etat) {
        this.id = id;
        this.nomGroupe = nomGroupe;
        this.nomMatiere = nomMatiere;
        this.nbreEtudiant = nbreEtudiant;
        Document = document;
        this.date = date;
        this.etat = etat;
    }

    public demande(String nomGroupe, String nomMatiere, int nbreEtudiant, String document, int etat, Date date) {
        this.nomGroupe = nomGroupe;
        this.nomMatiere = nomMatiere;
        this.nbreEtudiant = nbreEtudiant;
        Document = document;
        this.etat = etat;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public String getNomMatiere() {
        return nomMatiere;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public int getNbreEtudiant() {
        return nbreEtudiant;
    }

    public void setNbreEtudiant(int nbreEtudiant) {
        this.nbreEtudiant = nbreEtudiant;
    }

    public String getDocument() {
        return Document;
    }

    public void setDocument(String document) {
        Document = document;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}