package entities;

import java.time.LocalDate;

public class borne {
    private int id;
    private String emplacement;
    private int capacite;
    private String etat;
    private LocalDate date_inst;

    public borne() {
    }

    // Constructeur
    public borne(int id, String emplacement, int capacite, String etat, LocalDate date_inst) {
        this.id = id;
        this.emplacement = emplacement;
        this.capacite = capacite;
        this.etat = etat;
        this.date_inst = date_inst;
    }

    public borne(String emplacement, int capacite, String etat, LocalDate date_inst){
            this.emplacement = emplacement;
            this.capacite = capacite;
            this.etat = etat;
            this.date_inst = date_inst;
        }

        public int getId () {
            return id;
        }

        public void setId ( int id){
            this.id = id;
        }

        public String getEmplacement () {
            return emplacement;
        }

        public void setEmplacement (String emplacement){
            this.emplacement = emplacement;
        }

        public int getCapacite () {
            return capacite;
        }

        public void setCapacite ( int capacite){
            this.capacite = capacite;
        }

        public String getEtat () {
            return etat;
        }

        public void setEtat (String etat){
            this.etat = etat;
        }


        public LocalDate getDate_inst () {
            return date_inst;
        }

        public void setDate_inst (LocalDate date_inst){
            this.date_inst = date_inst;
        }


        @Override
        public String toString () {
            return "borne{" +
                    "id=" + id +
                    ", emplacement='" + emplacement + '\'' +
                    ", capacite=" + capacite +
                    ", etat='" + etat + '\'' +
                    ", date_inst=" + date_inst +
                    '}';
        }
    }