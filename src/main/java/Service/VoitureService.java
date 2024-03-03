package Service;

import Entities.Reservation_v;
import Entities.Voiture;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoitureService implements IServicev<Voiture>{

    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;
    public VoitureService() {
        conn= DataSource.getInstance().getCnx();
    }



    public void addV(Voiture v){
        String requette="insert into Voiture (marque,modele,etat,prix_location,kilometrage,image) values (?,?,?,?,?,?)";
        try {
            pst=conn.prepareStatement(requette);
            pst.setString(1, v.getMarque());
            pst.setString(2, v.getModele());
            pst.setString(3, v.getEtat());
            pst.setFloat(4, v.getPrix_location());
            pst.setFloat(5, v.getKilometrage());
            pst.setString(6, v.getImage());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
     public boolean delete(int id_v) {
        String requete = "DELETE FROM Voiture WHERE id_v = ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, id_v);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    @Override
    public void update(int id_v, Voiture voiture) {
        String requette = "UPDATE Voiture SET marque=?, modele=?, etat=?, prix_location=?, kilometrage=?, image=? WHERE id_v=?";
        try {
            pst = conn.prepareStatement(requette);
            pst.setString(1, voiture.getMarque());
            pst.setString(2, voiture.getModele());
            pst.setString(3, voiture.getEtat());
            pst.setFloat(4, voiture.getPrix_location());
            pst.setFloat(5, voiture.getKilometrage());
            pst.setString(6, voiture.getImage());
            pst.setInt(6, id_v);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Voiture> readAll() {
        String requette = "SELECT * FROM Voiture";
        List<Voiture> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(requette);
            while (rs.next()) {
                // Utilisez les bonnes colonnes de la base de données et assurez-vous de ne pas convertir
                // la colonne 'etat' en float
                list.add(new Voiture(rs.getInt("id_v"), rs.getString("marque"), rs.getString("modele"), rs.getString("etat"), rs.getFloat("prix_location"), rs.getFloat("kilometrage"), rs.getString("image")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }



    @Override
    public List<Voiture> readAvailableVoitures() {
        List<Voiture> availableVoitures = new ArrayList<>();

        String query = "SELECT * FROM voiture WHERE etat = 'disponible'";

        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int id_v = rs.getInt("id_v");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                String etat = rs.getString("etat");
                float prix_location = rs.getFloat("prix_location");
                float kilometrage = rs.getFloat("kilometrage");
                String image = rs.getString("image");

                // Création de l'objet Voiture
                Voiture voiture = new Voiture(id_v, marque, modele, etat, prix_location, kilometrage,image);
                availableVoitures.add(voiture);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return availableVoitures;
    }

    @Override
    public Voiture readbyId(int id_v) {
        String requette = "SELECT * FROM Voiture WHERE id_v = ?";
        try {
            pst = conn.prepareStatement(requette);
            pst.setInt(1, id_v);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Voiture(rs.getInt("id_v"), rs.getString("marque"), rs.getString("modele"), rs.getString("etat"), rs.getFloat("prix_location"),rs.getFloat("kilometrage"),rs.getString("image"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Reservation_v> readAllreservation() {
        return null;
    }

    @Override
    public List<Reservation_v> readAllForUser(int id_u) {
        return null;
    }

    @Override
    public List<Voiture> readAvailableReservations() {
        return null;
    }

}
