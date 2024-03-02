package gestion_service_apreslocation.entities;

public class ServiceApreslocation {

    private int idservice;
    private Type type;
    private String technicien;
    private String description;
    private String statut;
    private double cout;



    public ServiceApreslocation(Type type, String technicien, String description, String statut, double cout) {
        this.type = type;
        this.technicien = technicien;
        this.description = description;
        this.statut = statut;
        this.cout = cout;

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


    @Override
    public String toString() {
        return "service_apreslocation{" +
                "idservice=" + idservice +
                ", typeservice=" + type +
                ", technicien=" + technicien +
                ", description=" + description +
                ", statut=" + statut +
                ", cout=" + cout +
                '}';
    }


}
