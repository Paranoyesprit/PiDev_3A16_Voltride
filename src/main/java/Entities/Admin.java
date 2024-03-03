package Entities;

import javafx.scene.control.SingleSelectionModel;

import java.util.Date;

public class Admin extends User {
    private String departement;

    public Admin(int i, String text, String prenomAText, String emailAText, String mpAText, String imageAText, SingleSelectionModel<String> selectionModel) {
        super(); // Appel du constructeur par défaut de la classe parente
    }

    public Admin(int id_u, int cin, String nom, String prenom, String email, String motDePasse, Date dateInscription, String image, String departement) {
        super(id_u, cin, nom, prenom, email, motDePasse, dateInscription, image); // Appel du constructeur de la classe parente avec les paramètres correspondants
        this.departement = departement;
    }

    public Admin(int cin, String nom, String prenom, String email, String motDePasse, Date dateInscription, String image, String departement) {
        super(cin, nom, prenom, email, motDePasse, dateInscription, image); // Appel du constructeur de la classe parente avec les paramètres correspondants
        this.departement = departement;
    }

    // Getters and setters
    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }
}
