package Entities;

import java.time.LocalDate;

public class Evenement {
    private int id_event;
    private String type;
    private String adresseEvenement;
    private LocalDate dateEvenement;
    private int placesDispo;

    public Evenement(int id_event, String type, String adresseEvenement, LocalDate dateEvenement, int placesDispo) {
        this.id_event = id_event;
        this.type = type;
        this.adresseEvenement = adresseEvenement;
        this.dateEvenement = dateEvenement;
        this.placesDispo = placesDispo;
    }

    public Evenement() {

    }

    public String getAdresseEvenement() {
        return adresseEvenement;
    }

    public void setAdresseEvenement(String adresseEvenement) {
        this.adresseEvenement = adresseEvenement;
    }

    public LocalDate getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(LocalDate dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public int getPlacesDispo() {
        return placesDispo;
    }

    public void setPlacesDispo(int placesDispo) {
        this.placesDispo = placesDispo;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id_event=" + id_event +
                ", type='" + type + '\'' +
                ", adresseEvenement='" + adresseEvenement + '\'' +
                ", dateEvenement=" + dateEvenement +
                ", placesDispo=" + placesDispo +
                '}';
    }

    public Evenement(String type, String adresseEvenement, LocalDate dateEvenement, int placesDispo) {
        this.type = type;
        this.adresseEvenement = adresseEvenement;
        this.dateEvenement = dateEvenement;
        this.placesDispo = placesDispo;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
