package Service;

import Entities.Reservation_v;
import Entities.UserSession;
import util.DataSource;
import Entities.Voiture;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationVService implements IServicev<Reservation_v> {
    private final Connection conn;
    private Statement ste;
    private PreparedStatement pst;
    public ReservationVService() { conn= DataSource.getInstance().getCnx();}
    @Override
    public void addV(Reservation_v rv) {
        // Fetching the id of the corresponding voiture
        int id_voiture = getVoitureIdByMarque(rv.getVoiture().getMarque());

        if (id_voiture == -1) {
            throw new RuntimeException("Voiture not found!");
        }

        try {
            // Updating the etat of the corresponding voiture to "indisponible"
            updateVoitureEtat(id_voiture, "indisponible");

            // Get the id of the current user
            int id_user = UserSession.getInstance().getCurrentUser().getId_u(); // Assuming getId returns the user's id

            // Inserting a new row into the reservation_voiture table
            String query = "INSERT INTO reservation_voiture (id_v, id_u, date_debut, date_fin) VALUES (?, ?, ?, ?)";
            pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, id_voiture);
            pst.setInt(2, id_user);
            pst.setDate(3, Date.valueOf(rv.getDate_debut()));
            pst.setDate(4, Date.valueOf(rv.getDate_fin()));

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

    public boolean delete(int id_e) {
        String requete = "delete from reservation_voiture where id_r = ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, id_e);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
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

        // Récupérer l'ID de l'utilisateur connecté
        int userId = UserSession.getInstance().getCurrentUser().getId_u();

        // Requête SQL avec clause WHERE pour filtrer les réservations par ID utilisateur
        String query = "SELECT v.marque, v.modele, v.prix_location, v.kilometrage, rv.date_debut, rv.date_fin " +
                "FROM voiture v " +
                "JOIN reservation_voiture rv ON v.id_v = rv.id_v "+
                "JOIN utilisateur u ON rv.id_u = u.id_u " +
                "WHERE u.id_u = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                float prix_location = rs.getFloat("prix_location");
                float kilometrage = rs.getFloat("kilometrage");
                String image = rs.getString("image");
                LocalDate date_debut = rs.getDate("date_debut").toLocalDate();
                LocalDate date_fin = rs.getDate("date_fin").toLocalDate();

                // Créer l'objet voiture correspondant
                Voiture correspondingVoiture = new Voiture(marque, modele, null, prix_location, kilometrage,image);

                // Créer l'objet Reservation_v
                Reservation_v reservation = new Reservation_v(date_debut, date_fin, correspondingVoiture);
                reservationList.add(reservation);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservationList;
    }
    @Override
    public List<Reservation_v> readAllreservation() {
        List<Reservation_v> reservationList = new ArrayList<>();

        // Requête SQL pour récupérer toutes les réservations
        String query = "SELECT * FROM reservation_voiture";

        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            // Parcourir les résultats de la requête
            while (rs.next()) {
                int id_r = rs.getInt("id_r");
                LocalDate date_debut = rs.getDate("date_debut").toLocalDate();
                LocalDate date_fin = rs.getDate("date_fin").toLocalDate();

                // Utilisez la méthode readById de la classe VoitureService pour obtenir la voiture associée
                int id_voiture = rs.getInt("id_v");
                Voiture voiture = new VoitureService().readbyId(id_voiture);

                // Créez et ajoutez un nouvel objet Reservation_v à la liste
                reservationList.add(new Reservation_v(id_r, date_debut, date_fin, voiture));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservationList;
    }
    @Override
    public List<Reservation_v> readAllForUser(int id_u) {
        List<Reservation_v> reservationList = new ArrayList<>();

        // Récupérer l'ID de l'utilisateur connecté
        int userId = UserSession.getInstance().getCurrentUser().getId_u();

        // Requête SQL avec clause WHERE pour filtrer les réservations par ID utilisateur
        String query = "SELECT r.*, v.* FROM reservation_voiture r " +
                "JOIN voiture v ON r.id_v = v.id_v " +
                "JOIN utilisateur u ON r.id_u = u.id_u " +
                "WHERE u.id_u = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id_u);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {

                    String marque = rs.getString("marque");
                    String modele = rs.getString("modele");
                    float prix_location = rs.getFloat("prix_location");
                    float kilometrage = rs.getFloat("kilometrage");
                    String image = rs.getString("image");
                    LocalDate date_debut = rs.getDate("date_debut").toLocalDate();
                    LocalDate date_fin = rs.getDate("date_fin").toLocalDate();

                    // Créer l'objet voiture correspondant
                    Voiture correspondingVoiture = new Voiture(marque, modele, null, prix_location, kilometrage,image);

                    // Créer l'objet Reservation_v
                    Reservation_v reservation = new Reservation_v(date_debut, date_fin, correspondingVoiture);
                    reservationList.add(reservation);
                }
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


    public List<Reservation_v> readAllReservationsForMonth(LocalDate startDate, LocalDate endDate) {
        List<Reservation_v> reservations = new ArrayList<>();

        // Requête SQL pour récupérer les réservations pour le mois spécifié
        String query = "SELECT * FROM reservation_voiture WHERE date_debut >= ? AND date_fin <= ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setDate(1, java.sql.Date.valueOf(startDate));
            pst.setDate(2, java.sql.Date.valueOf(endDate));

            ResultSet rs = pst.executeQuery();

            // Parcourir le résultat et créer les objets Reservation_v correspondants
            while (rs.next()) {
                int id_r = rs.getInt("id_r");
                LocalDate date_debut = rs.getDate("date_debut").toLocalDate();
                LocalDate date_fin = rs.getDate("date_fin").toLocalDate();

                // Utilisez la méthode readById de la classe VoitureService pour obtenir la voiture associée
                int id_voiture = rs.getInt("id_v");
                Voiture voiture = new VoitureService().readbyId(id_voiture);

                // Créez et ajoutez un nouvel objet Reservation_v à la liste
                reservations.add(new Reservation_v(id_r, date_debut, date_fin, voiture));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs de manière appropriée
        }

        return reservations;
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
