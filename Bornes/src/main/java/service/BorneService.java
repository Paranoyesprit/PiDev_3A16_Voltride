package service;

import entities.borne;
import utils.Datasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

    public class BorneService implements IService<borne> {
    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;

    public BorneService() {
        conn= Datasource.getInstance().getCnx();
    }


    public void addpst(borne b){
        String requete="insert into borne (emplacement,capacite,etat,date_inst) values (?,?,?,?)";
        try {
            pst=conn.prepareStatement(requete);
            pst.setString(1,b.getEmplacement());
            pst.setInt(2,b.getCapacite());
            pst.setString(3,b.getEtat());
            // Convertir LocalDate en Date (java.sql.Date)
            Date dateInstallation = Date.valueOf(b.getDate_inst());
            pst.setDate(4, dateInstallation);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int id) {
        String requete = "delete from borne where id = ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
        public void update(int id, borne updatedBorne) {
            String updateQuery = "update borne set emplacement=?, capacite=?, etat=?, date_inst=? where id=?";
            try (PreparedStatement pst = conn.prepareStatement(updateQuery)) {
                pst.setString(1, updatedBorne.getEmplacement());
                pst.setInt(2, updatedBorne.getCapacite());
                pst.setString(3, updatedBorne.getEtat());

                // Convertir LocalDate en Date (java.sql.Date)
                Date dateInstallation = Date.valueOf(updatedBorne.getDate_inst());
                pst.setDate(4, dateInstallation);

                pst.setInt(5, id);
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    @Override
    public List<borne> readAll() {
        String requete = "select * from borne";
        List<borne> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(requete);
            while (rs.next()) {
                int id = rs.getInt(1);
                String emplacement = rs.getString("emplacement");
                int capacite = rs.getInt("capacite");
                String etat = rs.getString("etat");

                // Convertir Date en LocalDate
                Date dateInstallation = rs.getDate("date_inst");
                LocalDate date_inst= dateInstallation.toLocalDate();

                list.add(new borne(id, emplacement, capacite, etat, date_inst));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

        @Override
        public List<borne> readAvailableBornes() {
            List<borne> availableBornes = new ArrayList<>();

            String query = "SELECT * FROM borne WHERE etat = 'disponible'";

            try (PreparedStatement pst = conn.prepareStatement(query);
                 ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    int id= rs.getInt("id");
                    String emplacement = rs.getString("emplacement");
                    int capacite = rs.getInt("capacite");
                    String etat = rs.getString("etat");
                    // Convertir Date en LocalDate
                    Date dateInstallation = rs.getDate("date_inst");
                    LocalDate date_inst= dateInstallation.toLocalDate();
                    // Création de l'objet Voiture
                    borne borne = new borne(id, emplacement, capacite, etat, date_inst);
                    availableBornes.add(borne);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return availableBornes;
        }

    @Override
    public borne readById(int id) {
        return null;
    }
}