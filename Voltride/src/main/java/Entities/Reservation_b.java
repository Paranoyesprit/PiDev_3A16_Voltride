package Entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation_b {
    protected int id_r;
    protected LocalDateTime date_d;
    protected LocalDateTime date_f;
    protected borne borne;

    public Reservation_b() {
    }

    public Reservation_b(int id_r, LocalDateTime date_d, LocalDateTime date_f, Entities.borne borne) {
        this.id_r = id_r;
        this.date_d = date_d;
        this.date_f = date_f;
        this.borne = borne;
    }

    public Reservation_b(LocalDateTime date_d, LocalDateTime date_f, Entities.borne borne) {
        this.date_d = date_d;
        this.date_f = date_f;
        this.borne = borne;
    }

    public int getId_r() {
        return id_r;
    }

    public void setId_r(int id_r) {
        this.id_r = id_r;
    }

    public LocalDateTime getDate_d() {
        return date_d;
    }

    public void setDate_d(LocalDateTime date_d) {
        this.date_d = date_d;
    }

    public LocalDateTime getDate_f() {
        return date_f;
    }

    public void setDate_f(LocalDateTime date_f) {
        this.date_f = date_f;
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
                ", date_f=" + date_f +
                ", borne=" + borne +
                '}';
    }
}
