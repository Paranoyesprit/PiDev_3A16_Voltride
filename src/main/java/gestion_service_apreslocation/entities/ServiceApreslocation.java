package gestion_service_apreslocation.entities;

public class ServiceApreslocation {

    private int idservice;
    private Type type;
    private String technicien;
    private String description;
    private String statut;
    private double cout;
    private  int id_client;
    private int id_voiture;

    public ServiceApreslocation(Type type, String technicien, String description, String statut, double cout, int id_client, int id_voiture) {
        this.type = type;
        this.technicien = technicien;
        this.description = description;
        this.statut = statut;
        this.cout = cout;
        this.id_client = id_client;
        this.id_voiture = id_voiture;
    }
    public int getIdservice() {
        return idservice;
    }

    public void setIdservice(int idservice) {
        this.idservice = idservice;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTechnicien() {
        return technicien;
    }

    public void setTechnicien(String technicien) {
        this.technicien = technicien;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_voiture() {
        return id_voiture;
    }

    public void setId_voiture(int id_voiture) {
        this.id_voiture = id_voiture;
    }

    @Override
    public String toString() {
        return "service_apreslocation{" +
                "idservice=" + idservice +
                ", typeservice=" + type +
                ", technicien=" + technicien +
                ", description=" + description +
                ", statut=" + statut +
                ", cout=" + cout +
                ", id_client=" + id_client +
                ", id_voiture=" + id_voiture +
                '}';
    }


}
