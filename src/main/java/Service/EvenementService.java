package Service;

import Entities.Evenement;
import Entities.Reservation_e;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IServicee<Evenement> {
    private final Connection conn;
    private Statement ste;
    private PreparedStatement pst;

    public EvenementService() {
        conn = DataSource.getInstance().getCnx();
    }

    public void add(Evenement v) {
        String requette = "insert into evenement (type, adresseEvenement, dateEvenement, placesDispo) values (?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(requette);
            pst.setString(1, v.getType());
            pst.setString(2, v.getAdresseEvenement());
            pst.setDate(3, Date.valueOf(v.getDateEvenement()));
            pst.setInt(4, v.getPlacesDispo()); // Ajoutez cette ligne pour inclure placesDispo
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id_e) {

    }

    public void update(int id_event, Evenement evenement) {
        String requette = "UPDATE evenement SET type=?, adresseEvenement=?, dateEvenement=?, placesDispo=? WHERE id_event=?";
        try {
            pst = conn.prepareStatement(requette);
            pst.setString(1, evenement.getType());
            pst.setString(2, evenement.getAdresseEvenement());
            pst.setDate(3, Date.valueOf(evenement.getDateEvenement()));
            pst.setInt(4, evenement.getPlacesDispo());
            pst.setInt(5, id_event);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Evenement> readAll() {
        String requette = "select * from evenement";
        List<Evenement> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(requette);
            while (rs.next()) {
                list.add(new Evenement(
                        rs.getInt(1),
                        rs.getString("type"),
                        rs.getString("adresseEvenement"),
                        rs.getDate("dateEvenement").toLocalDate(),
                        rs.getInt("placesDispo")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Evenement> readAvailableEvenement() {
        return null;
    }

    public Evenement readbyId(int id_event) {
        String requette = "SELECT * FROM evenement WHERE id_event = ?";
        try {
            pst = conn.prepareStatement(requette);
            pst.setInt(1, id_event);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Evenement(
                        rs.getInt("id_event"),
                        rs.getString("type"),
                        rs.getString("adresseEvenement"),
                        rs.getDate("dateEvenement").toLocalDate(),
                        rs.getInt("placesDispo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }





    @Override
    public List<Evenement> readAvailableReservations() {
        return null;
    }

    @Override
    public List<Reservation_e> readAllForUser(int id_u) {
        return null;
    }
}
