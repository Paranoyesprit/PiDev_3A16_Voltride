package Entities;

public class Voiture {
    private int id_v;
    private String marque;
    private String modele;
    private String etat;
    private float prix_location;
    private float kilometrage;

    public Voiture() {
    }

    public Voiture(int id_v, String marque, String modele, String etat, float prix_location,float kilometrage) {
        this.id_v = id_v;
        this.marque = marque;
        this.modele = modele;
        this.etat = etat;
        this.prix_location = prix_location;
        this.kilometrage=kilometrage;
    }

    public Voiture(String marque, String modele, String etat, float prix_location,float kilometrage) {
        this.marque = marque;
        this.modele = modele;
        this.etat = etat;
        this.prix_location = prix_location;
        this.kilometrage=kilometrage;
    }

    public int getId_v() {
        return id_v;
    }

    public void setId_v(int id_v) {
        this.id_v = id_v;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public float getPrix_location() {
        return prix_location;
    }

    public void setPrix_location(float prix_location) {
        this.prix_location = prix_location;
    }

    public float getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(float kilometrage) {
        this.kilometrage = kilometrage;
    }

    @Override
    public String toString() {
        return "Voiture{" +
                "id_v=" + id_v +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", etat='" + etat + '\'' +
                ", prix_location=" + prix_location +
                ", kilometrage=" + kilometrage +
                '}';
    }
}


