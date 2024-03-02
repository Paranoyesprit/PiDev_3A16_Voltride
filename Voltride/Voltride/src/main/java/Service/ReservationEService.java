package Service;

import Entities.Reservation_e;
import util.DataSource;
import Entities.Evenement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public  class ReservationEService implements IServicee<Reservation_e> {
    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;
    public ReservationEService() { conn= DataSource.getInstance().getCnx();}
    @Override
    public void add(Reservation_e rv) {
        try {
            String requete = "INSERT INTO reservation_e (id_event, nbrPersonne, commentaire) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, rv.getEvenement().getId_event());
            pst.setInt(2, rv.getNbrPersonne());
            pst.setString(3, rv.getCommentaire());

            pst.executeUpdate();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id_e = generatedKeys.getInt(1);
                rv.setId_e(id_e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEvenementType(int id_e, String newType) {
        String query = "UPDATE evenement SET type = ? WHERE id_event = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, newType);
            pst.setInt(2, id_e);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getEvenementIdByType(String type) {
        int id_event = -1;
        String query = "SELECT id_event FROM evenement WHERE type = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, type);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id_event = rs.getInt("id_event");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id_event;
    }

    @Override

    public void delete(int id_e) {
        String requete = "delete from reservation_e where id_e = ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, id_e);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int id_e, Reservation_e upReservationE) {
        String updateQuery = "UPDATE reservation_e SET nbrPersonne = ?, commentaire = ? WHERE id_e = ?";

        try (PreparedStatement pst = conn.prepareStatement(updateQuery)) {
            pst.setInt(1, upReservationE.getNbrPersonne());
            pst.setString(2, upReservationE.getCommentaire());
            pst.setInt(3, id_e);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reservation_e> readAll() {
        List<Reservation_e> reservationList = new ArrayList<>();

        String query = "SELECT rv.*, v.* FROM reservation_e rv " +
                "JOIN evenement v ON rv.id_event = v.id_event";

        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id_e = rs.getInt("id_e");
                int nbrPersonne = rs.getInt("nbrPersonne");
                String commentaire = rs.getString("commentaire");

                int id_event = rs.getInt("id_event");
                String type = rs.getString("type");
                String adresseEvenement = rs.getString("adresseEvenement");
                LocalDate dateEvenement = rs.getDate("dateEvenement").toLocalDate(); // Retrieve dateEvenement as LocalDate

                Evenement correspondingEvenement = new Evenement(id_event, type, adresseEvenement, dateEvenement);


                Reservation_e reservation = new Reservation_e(id_e, nbrPersonne, commentaire, correspondingEvenement);
                reservationList.add(reservation);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservationList;
    }
    @Override
    public List<Evenement> readAvailableEvenement() {
        return null;
    }



    @Override
    public Reservation_e readbyId(int id_e) {
        String query = "SELECT * FROM reservation_e WHERE id_e = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id_e);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int nbrPersonne = rs.getInt("nbrPersonne");
                String commentaire = rs.getString("commentaire");

                int id_event = rs.getInt("id_event");
                String type = rs.getString("type");
                String adresseEvenement = rs.getString("adresseEvenement");
                LocalDate dateEvenement = rs.getDate("dateEvenement").toLocalDate();

                Evenement evenement = new Evenement(id_event, type, adresseEvenement, dateEvenement);

                return new Reservation_e(id_e, nbrPersonne, commentaire, evenement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    @Override
    public List<Reservation_e> readAvailableReservations() {
        return null;
    }

}
