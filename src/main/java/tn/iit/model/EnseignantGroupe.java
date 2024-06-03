package tn.iit.model;

public class EnseignantGroupe {
    private int id;
    private int idgroupe;
    private int idenseignant;

    public EnseignantGroupe(int idgroupe, int idenseignant) {
        this.idgroupe = idgroupe;
        this.idenseignant = idenseignant;
    }

    public EnseignantGroupe(int id, int idgroupe, int idenseignant) {
        this.id = id;
        this.idgroupe = idgroupe;
        this.idenseignant = idenseignant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdgroupe() {
        return idgroupe;
    }

    public void setIdgroupe(int idgroupe) {
        this.idgroupe = idgroupe;
    }

    public int getIdenseignant() {
        return idenseignant;
    }

    public void setIdenseignant(int idenseignant) {
        this.idenseignant = idenseignant;
    }



}
