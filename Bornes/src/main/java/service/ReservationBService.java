    package service;

    import entities.Reservation_b;
    import entities.borne;
    import utils.Datasource;

    import java.sql.*;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

        public class ReservationBService implements IService<Reservation_b> {
        private Connection conn;

        private PreparedStatement pst;
        public ReservationBService() {conn= Datasource.getInstance().getCnx();}
        @Override
        public void addpst(Reservation_b r) {
            // Fetching the id of the corresponding borne
            int id_borne = getBorneIdByEmplacement(r.getBorne().getEmplacement()); // You need to implement this method

            if (id_borne == -1) {
                throw new RuntimeException("Borne not found!"); // Handle the case when the corresponding borne is not found
            }

            try {
                // Updating the etat of the corresponding borne to "indisponible"
                updateBorneEtat(id_borne, "indisponible"); // You need to implement this method

                // Inserting a new row into the reservation_b table
                String requete = "INSERT INTO reservation_b (id_b, date_d, date_f) VALUES (?, ?, ?)";
                pst = conn.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, id_borne);
                pst.setTimestamp(2, Timestamp.valueOf(r.getDate_d()));
                pst.setTimestamp(3, Timestamp.valueOf(r.getDate_f()));

                pst.executeUpdate();

                // Retrieve the auto-generated key (id_r)
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
            String requete = "delete from reservation_b where id_r = ?";
            try {
                pst = conn.prepareStatement(requete);
                pst.setInt(1, id_r);
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


            @Override
            public void update(int id_r, Reservation_b upReservationB) {
                String updateQuery = "UPDATE reservation_b SET date_d = ?, date_f = ? WHERE id_r = ?";

                try (PreparedStatement pst = conn.prepareStatement(updateQuery)) {
                    // Set the new date_d and date_f values
                    pst.setTimestamp(1, Timestamp.valueOf(upReservationB.getDate_d()));
                    pst.setTimestamp(2, Timestamp.valueOf(upReservationB.getDate_f()));
                    // Specify the id_r to identify the reservation to update
                    pst.setInt(3, id_r);

                    // Execute the update query
                    pst.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
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
                        LocalDateTime date_d = rs.getTimestamp("date_d").toLocalDateTime();
                        LocalDateTime date_f = rs.getTimestamp("date_f").toLocalDateTime();

                        // Read borne data excluding id
                        int id_b = rs.getInt("id_b");
                        String emplacement = rs.getString("emplacement");
                        int capacite = rs.getInt("capacite");
                        String etat = rs.getString("etat");
                        LocalDate date_inst = rs.getDate("date_inst").toLocalDate();

                        // Create the corresponding borne object
                        borne correspondingBorne = new borne(id_b, emplacement, capacite, etat, date_inst);

                        // Create the Reservation_b object
                        Reservation_b reservation = new Reservation_b(id_r, date_d, date_f, correspondingBorne);
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

