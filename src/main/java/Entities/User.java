package Entities;
import java.util.Date;


public class User {
    private int id_u;
    private int cin;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private Date dateInscription;
    private String image;

    public User() {
    }

    public User(int id_u, int cin, String nom, String prenom, String email, String motDePasse, Date dateInscription, String image) {
        this.id_u = id_u;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateInscription = dateInscription;
        this.image = image;
    }

    public User(int cin, String nom, String prenom, String email, String motDePasse, Date dateInscription, String image) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateInscription = dateInscription;
        this.image = image;
    }

    public User(int cin, String nom, String prenom, String email, String motDePasse, String image) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateInscription = new Date();
        this.image = image;
    }

    public int getId_u() {
        return id_u;
    }

    public void setId_u(int id_u) {
        this.id_u = id_u;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }




    @Override
    public String toString() {
        return "User{" +
                "id_u=" + id_u +
                ", cin=" + cin +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", dateInscription=" + dateInscription +
                ", image='" + image + '\'' +
                '}';
    }
}


