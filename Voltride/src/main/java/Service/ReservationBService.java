package Service;

import Entities.Reservation_b;
import Entities.UserSession;
import Entities.Voiture;
import Entities.borne;
import util.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationBService implements IServiceb<Reservation_b> {
    private Connection conn;
    private PreparedStatement pst;

    public ReservationBService() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void addpst(Reservation_b r) {
        int id_borne = getBorneIdByEmplacement(r.getBorne().getEmplacement());

        if (id_borne == -1) {
            throw new RuntimeException("Borne not found!");
        }

        try {
            updateBorneEtat(id_borne, "indisponible");

            int id_user = UserSession.getInstance().getCurrentUser().getId_u();
            String requete = "INSERT INTO reservation_b (id_b, id_u, date_d, date_f, heure_d, heure_f) VALUES (?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, id_borne);
            pst.setInt(2, id_user);
            pst.setDate(3, Date.valueOf(r.getDate_d()));
            pst.setDate(4, Date.valueOf(r.getDate_f()));
            pst.setObject(5, Timestamp.valueOf(LocalDateTime.of(r.getDate_d(), r.getHeure_d())));
            pst.setObject(6, Timestamp.valueOf(LocalDateTime.of(r.getDate_f(), r.getHeure_f())));

            pst.executeUpdate();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id_r = generatedKeys.getInt(1);
                r.setId_r(id_r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateBorneEtat(int id_b, String newEtat) {
        String query = "UPDATE borne SET etat = ? WHERE id = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, newEtat);
            pst.setInt(2, id_b);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getBorneIdByEmplacement(String emplacement) {
        int id = -1;
        String query = "SELECT id FROM borne WHERE emplacement = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, emplacement);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    @Override
    public void delete(int id_r) {
        String requete = "DELETE FROM reservation_b WHERE id_r = ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, id_r);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<borne> getAvailableBornes(LocalDate startDate, LocalDate endDate) {
        List<borne> availableBornes = new ArrayList<>();

        // Requête SQL pour récupérer les voitures qui ne sont pas réservées dans les intervalles de dates spécifiés
        String query = "SELECT * FROM borne WHERE id NOT IN " +
                "(SELECT id_b FROM reservation_b " +
                "WHERE (date_d <= ? AND date_f >= ?) " +
                "OR (date_d <= ? AND date_f >= ?) " +
                "OR (date_d >= ? AND date_f <= ?))";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setDate(1, Date.valueOf(startDate));
            pst.setDate(2, Date.valueOf(startDate));
            pst.setDate(3, Date.valueOf(endDate));
            pst.setDate(4, Date.valueOf(endDate));
            pst.setDate(5, Date.valueOf(startDate));


            ResultSet rs = pst.executeQuery();

            // Parcourir le résultat et créer les objets Voiture correspondants
            while (rs.next()) {
                int id = rs.getInt("id");
                String emplacement = rs.getString("emplacement");
                String etat = rs.getString("etat");
                int capacite = rs.getInt("capacite");
                LocalDate date_inst = rs.getDate("date_inst").toLocalDate();


                // Créer et ajouter un nouvel objet Voiture à la liste
                borne borne = new borne(id, emplacement, capacite, etat, date_inst);
                availableBornes.add(borne);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return availableBornes;
    }
    @Override
    public void update(int id_r, Reservation_b upReservationB) {
        String updateQuery = "UPDATE reservation_b SET date_d = ?, heure_d = ?, date_f = ?, heure_f = ? WHERE id_r = ?";

        try (PreparedStatement pst = conn.prepareStatement(updateQuery)) {
            pst.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(upReservationB.getDate_d(), upReservationB.getHeure_d())));
            pst.setObject(2, upReservationB.getHeure_d());
            pst.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(upReservationB.getDate_f(), upReservationB.getHeure_f())));
            pst.setObject(4, upReservationB.getHeure_f());
            pst.setInt(5, id_r);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reservation_b> readAllForUser(int id_u) {
        List<Reservation_b> reservationList = new ArrayList<>();

        String query = "SELECT r.*, b.* FROM reservation_b r " +
                "JOIN borne b ON r.id_b = b.id " +
                "JOIN utilisateur u ON r.id_u = u.id_u " +
                "WHERE u.id_u = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id_u);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id_r = rs.getInt("id_r");
                    LocalDate date_d = rs.getTimestamp("date_d").toLocalDateTime().toLocalDate();
                    LocalTime heure_d = rs.getObject("heure_d", LocalTime.class);
                    LocalDate date_f = rs.getTimestamp("date_f").toLocalDateTime().toLocalDate();
                    LocalTime heure_f = rs.getObject("heure_f", LocalTime.class);

                    int id_b = rs.getInt("id_b");
                    String emplacement = rs.getString("emplacement");
                    int capacite = rs.getInt("capacite");
                    String etat = rs.getString("etat");

                    borne correspondingBorne = new borne(id_b, emplacement, capacite, etat, null);

                    Reservation_b reservation = new Reservation_b(id_r, LocalDateTime.of(date_d, heure_d != null ? heure_d : LocalTime.MIDNIGHT),
                            LocalDateTime.of(date_f, heure_f != null ? heure_f : LocalTime.MIDNIGHT), correspondingBorne);
                    reservationList.add(reservation);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservationList;
    }

    @Override
    public List<Reservation_b> readAll() {
        List<Reservation_b> reservationList = new ArrayList<>();

        String query = "SELECT r.*, b.* FROM reservation_b r " +
                "JOIN borne b ON r.id_b = b.id";

        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id_r = rs.getInt("id_r");
                LocalDate date_d = rs.getTimestamp("date_d").toLocalDateTime().toLocalDate();
                LocalTime heure_d = rs.getObject("heure_d", LocalTime.class);
                LocalDate date_f = rs.getTimestamp("date_f").toLocalDateTime().toLocalDate();
                LocalTime heure_f = rs.getObject("heure_f", LocalTime.class);

                int id_b = rs.getInt("id_b");
                String emplacement = rs.getString("emplacement");
                int capacite = rs.getInt("capacite");
                String etat = rs.getString("etat");

                borne correspondingBorne = new borne(id_b, emplacement, capacite, etat, null);

                Reservation_b reservation = new Reservation_b(id_r, LocalDateTime.of(date_d, heure_d), LocalDateTime.of(date_f, heure_f), correspondingBorne);

                reservationList.add(reservation);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservationList;
    }

    @Override
    public List<borne> readAvailableBornes() {
        return null;
    }

    @Override
    public Reservation_b readById(int id) {
        return null;
    }
}
