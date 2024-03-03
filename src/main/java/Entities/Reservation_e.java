package Entities;

import java.time.LocalDate;

public class Reservation_e {

    protected int id_e;
    protected int nbrPersonne;
    protected String commentaire;
    protected Evenement evenement;
    public String getType(){
        return evenement.getType();
    }
    public String getAdresseEvenement(){
        return evenement.getAdresseEvenement();
    }
    public LocalDate getDateEvenement(){
        return evenement.getDateEvenement();
    }

    public Reservation_e() {
    }

    public Reservation_e(int nbrPersonne, String commentaire, Evenement evenement) {
        this.nbrPersonne = nbrPersonne;
        this.commentaire = commentaire;
        this.evenement = evenement;
    }

    public Reservation_e(int id_e, int nbrPersonne, String commentaire, Evenement evenement) {
        this.id_e = id_e;
        this.nbrPersonne = nbrPersonne;
        this.commentaire = commentaire;
        this.evenement = evenement;
    }

    public int getId_e() {
        return id_e;
    }

    public void setId_e(int id_e) {
        this.id_e = id_e;
    }

    public int getNbrPersonne() {
        return nbrPersonne;
    }

    public void setNbrPersonne(int nbrPersonne) {
        this.nbrPersonne = nbrPersonne;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    @Override
    public String toString() {
        return "Reservation_e{" +
                "id_e=" + id_e +
                ", nbrPersonne=" + nbrPersonne +
                ", commentaire='" + commentaire + '\'' +
                ", evenement=" + evenement +
                '}';
    }
}
