package gestion_service_apreslocation.entities;

public class Participation {

    private int idp;
    private ServiceApreslocation ids;
    private int nbr_de_participant;

    public Participation(ServiceApreslocation ids, int nbr_de_participant) {
        this.ids= ids;
        this.nbr_de_participant = nbr_de_participant;
    }

    public int getIdp() {
        return idp;
    }

    public void setIdp(int idp) {
        this.idp = idp;
    }

    public ServiceApreslocation getIds() {
        return ids;
    }

    public void setIds(ServiceApreslocation ids) {
        this.ids = ids;
    }

    public int getNbr_de_participant() {
        return nbr_de_participant;
    }

    public void setNbr_de_participant(int nbr_de_participant) {
        this.nbr_de_participant = nbr_de_participant;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "idp=" + idp +
                ", ids=" + ids +
                ", nbr_de_participant=" + nbr_de_participant +
                '}';
    }
}
