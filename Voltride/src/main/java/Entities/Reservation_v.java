package Entities;

import java.time.LocalDate;

public class Reservation_v {
    protected int id_r;
    protected LocalDate date_debut;
    protected LocalDate date_fin;
    protected Voiture voiture;

    public Reservation_v() {
    }

    public Reservation_v(LocalDate date_debut, LocalDate date_fin, Voiture voiture) {
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.voiture = voiture;
    }

    public Reservation_v(int id_r, LocalDate date_debut, LocalDate date_fin, Voiture voiture) {
        this.id_r = id_r;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.voiture = voiture;
    }

    public int getId_r() {
        return id_r;
    }

    public void setId_r(int id_r) {
        this.id_r = id_r;
    }

    public LocalDate getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(LocalDate date_debut) {
        this.date_debut = date_debut;
    }

    public LocalDate getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(LocalDate date_fin) {
        this.date_fin = date_fin;
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

    @Override
    public String toString() {
        return "Reservation_v{" +
                "id_r=" + id_r +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", voiture=" + voiture +
                '}';
    }
}
