package Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reservation_b {
    protected int id_r;
    protected LocalDate date_d;
    protected LocalTime heure_d;
    protected LocalDate date_f;
    protected LocalTime heure_f;
    protected borne borne;

    public String getEmplacement() {
        return borne.getEmplacement();
    }

    public int getCapacite() {
        return borne.getCapacite();
    }

    public Reservation_b(int id_r, LocalDateTime date_d, LocalDateTime date_f, borne borne) {
        this.id_r = id_r;
        this.date_d = date_d.toLocalDate();
        this.heure_d = date_d.toLocalTime();
        this.date_f = date_f.toLocalDate();
        this.heure_f = date_f.toLocalTime();
        this.borne = borne;
    }
    public Reservation_b() {
    }

    public Reservation_b(int id_r, LocalDate date_d, LocalTime heure_d, LocalDate date_f, LocalTime heure_f, Entities.borne borne) {
        this.id_r = id_r;
        this.date_d = date_d;
        this.heure_d = heure_d;
        this.date_f = date_f;
        this.heure_f = heure_f;
        this.borne = borne;
    }

    public Reservation_b(LocalDate date_d, LocalTime heure_d, LocalDate date_f, LocalTime heure_f, Entities.borne borne) {
        this.date_d = date_d;
        this.heure_d = heure_d;
        this.date_f = date_f;
        this.heure_f = heure_f;
        this.borne = borne;
    }

    public int getId_r() {
        return id_r;
    }

    public void setId_r(int id_r) {
        this.id_r = id_r;
    }

    public LocalDate getDate_d() {
        return date_d;
    }

    public void setDate_d(LocalDate date_d) {
        this.date_d = date_d;
    }

    public LocalTime getHeure_d() {
        return heure_d;
    }

    public void setHeure_d(LocalTime heure_d) {
        this.heure_d = heure_d;
    }

    public LocalDate getDate_f() {
        return date_f;
    }

    public void setDate_f(LocalDate date_f) {
        this.date_f = date_f;
    }

    public LocalTime getHeure_f() {
        return heure_f;
    }

    public void setHeure_f(LocalTime heure_f) {
        this.heure_f = heure_f;
    }

    public Entities.borne getBorne() {
        return borne;
    }

    public void setBorne(Entities.borne borne) {
        this.borne = borne;
    }

    @Override
    public String toString() {
        return "Reservation_b{" +
                "id_r=" + id_r +
                ", date_d=" + date_d +
                ", heure_d=" + heure_d +
                ", date_f=" + date_f +
                ", heure_f=" + heure_f +
                ", borne=" + borne +
                '}';
    }
}
