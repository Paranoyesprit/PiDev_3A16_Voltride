package Service;

import Entities.Reservation_v;
import util.DataSource;
import Entities.Voiture;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationVService implements IService<Reservation_v> {
    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;
    public ReservationVService() { conn= DataSource.getInstance().getCnx();}
    @Override
    public void addV(Reservation_v rv) {
        // Fetching the id of the corresponding borne
        int id_voiture = getVoitureIdByMarque(rv.getVoiture().getMarque()); // You need to implement this method

        if (id_voiture == -1) {
            throw new RuntimeException("Voiture not found!"); // Handle the case when the corresponding borne is not found
        }

        try {
            // Updating the etat of the corresponding borne to "indisponible"
            updateVoitureEtat(id_voiture, "indisponible"); // You need to implement this method

            // Inserting a new row into the reservation_b table
            String requete = "INSERT INTO reservation_voiture (id_v, date_debut,date_fin) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, id_voiture);

            // Convert LocalDate to SQL Date
            Date date_debut = Date.valueOf(rv.getDate_debut());
            pst.setDate(2, date_debut);
            Date date_fin = Date.valueOf(rv.getDate_fin());
            pst.setDate(3, date_fin);

            pst.executeUpdate();

            // Retrieve the auto-generated key (id_r)
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id_r = generatedKeys.getInt(1);
                rv.setId_r(id_r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateVoitureEtat(int id_v, String newEtat) {
        String query = "UPDATE voiture SET etat = ? WHERE id_v = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, newEtat);
            pst.setInt(2, id_v);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getVoitureIdByMarque(String marque) {
        int id_v = -1;
        String query = "SELECT id_v FROM voiture WHERE marque = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, marque);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id_v = rs.getInt("id_v");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id_v;
    }

    @Override

        public void delete(int id_r) {
            String requete = "delete from reservation_voiture where id_r = ?";
            try {
                pst = conn.prepareStatement(requete);
                pst.setInt(1, id_r);
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    @Override
    public void update(int id_r, Reservation_v upReservationV) {
        String updateQuery = "UPDATE reservation_voiture SET date_debut = ?, date_fin = ? WHERE id_r = ?";

        try (PreparedStatement pst = conn.prepareStatement(updateQuery)) {
            // Set the new date_debut and date_fin values
            pst.setDate(1, Date.valueOf(upReservationV.getDate_debut()));
            pst.setDate(2, Date.valueOf(upReservationV.getDate_fin()));
            // Specify the id_r to identify the reservation to update
            pst.setInt(3, id_r);

            // Execute the update query
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reservation_v> readAll() {
        List<Reservation_v> reservationList = new ArrayList<>();

        String query = "SELECT rv.*, v.* FROM reservation_voiture rv " +
                "JOIN voiture v ON rv.id_v = v.id_v";

        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id_r = rs.getInt("id_r");
                LocalDate date_debut = rs.getDate("date_debut").toLocalDate();
                LocalDate date_fin = rs.getDate("date_fin").toLocalDate();

                // Read voiture data excluding id_v
                int id_v = rs.getInt("id_v");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                String etat = rs.getString("etat");
                float prix_location = rs.getFloat("prix_location");
                float kilometrage = rs.getFloat("kilometrage");

                // Create the corresponding voiture object
                Voiture correspondingVoiture = new Voiture(id_v, marque, modele, etat, prix_location, kilometrage);

                // Create the Reservation_v object
                Reservation_v reservation = new Reservation_v(id_r, date_debut, date_fin, correspondingVoiture);
                reservationList.add(reservation);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservationList;
    }
    @Override
    public List<Voiture> readAvailableVoitures() {
   return null;
    }



    @Override
    public Reservation_v readbyId(int id_r) {
        String query = "SELECT * FROM reservation_voiture WHERE id_r = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id_r);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id_voiture = rs.getInt("id_v");
                LocalDate date_debut = rs.getDate("date_debut").toLocalDate();
                LocalDate date_fin = rs.getDate("date_fin").toLocalDate();

                // Utilisez la méthode readById de la classe VoitureService pour obtenir la voiture associée
                Voiture voiture = new VoitureService().readbyId(id_voiture);

                // Créez et retournez un nouvel objet Reservation_v
                return new Reservation_v(id_r, date_debut, date_fin, voiture);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public List<Reservation_v> readAvailableReservations() {
        return null;
    }

}
