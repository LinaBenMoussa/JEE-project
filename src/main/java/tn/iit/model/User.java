package tn.iit.model;

import java.util.Date;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private int role;
    private String email;

    public User(int id, String nom, String prenom, Date dateNaissance, int role, String email, String pw, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.role = role;
        this.email = email;
        this.pw = pw;
        this.telephone = telephone;
    }

    public User(String nom, String prenom, Date dateNaissance, int role, String email, String pw, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.role = role;
        this.email = email;
        this.pw = pw;
        this.telephone = telephone;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    private String pw;
    private String telephone;
}
