package gestion_service_apreslocation.service;

import gestion_service_apreslocation.entities.ServiceApreslocation;
import gestion_service_apreslocation.entities.Participation;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipationServices implements IServiceP<Participation> {

    private Connection conn;
    private PreparedStatement pst;

    public ParticipationServices() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(Participation p) {
        String query = "INSERT INTO participation (ids, nbr_de_participant) VALUES (?, ?)";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, p.getIds().getIdservice());
            pst.setInt(2, p.getNbr_de_participant());
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error adding participation: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(int idp) {
        String query = "DELETE FROM participation WHERE idp = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, idp);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int idp, Participation participation) {
        String query = "UPDATE participation SET ids = ?, nbr_de_participant = ? WHERE idp = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, participation.getIds().getIdservice());
            pst.setInt(2, participation.getNbr_de_participant());
            pst.setInt(3, idp);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Participation> readAll() {
        String query = "SELECT * FROM participation";
        List<Participation> list = new ArrayList<>();
        try {
            pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ServiceApreslocation serviceApreslocation = new ServiceApreslocationServices().readById(rs.getInt("ids"));
                Participation participation = new Participation(
                        serviceApreslocation,
                        rs.getInt("nbr_de_participant")
                );
                participation.setIdp(rs.getInt("idp"));
                list.add(participation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Participation readById(int idp) {
        String query = "SELECT * FROM participation WHERE idp = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, idp);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ServiceApreslocation serviceApreslocation = new ServiceApreslocationServices().readById(rs.getInt("ids"));
                return new Participation(
                        serviceApreslocation,
                        rs.getInt("nbr_de_participant")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public int getNumberOfParticipants(int eventId) {
        String query = "SELECT nbr_de_participant FROM participation WHERE ids = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, eventId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("nbr_de_participant");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public Participation getParticipationByEvenementId(int evenementId) {
        String query = "SELECT * FROM participation WHERE ids = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, evenementId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ServiceApreslocationServices serviceApreslocationServices = new ServiceApreslocationServices();
                ServiceApreslocation serviceapreslocation = serviceApreslocationServices.readById(rs.getInt("idservice"));
                Participation participation = new Participation(
                        serviceapreslocation,
                        rs.getInt("nbr_de_participant")
                );
                participation.setIdp(rs.getInt("idp"));
                return participation;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void addParticipation(int serviceId) {
        String queryCheck = "SELECT * FROM participation WHERE idp = ?";
        String queryInsert = "INSERT INTO participation (ids, nbr_de_participant) VALUES (?, 1)";
        String queryUpdate = "UPDATE participation SET nbr_de_participant = nbr_de_participant + 1 WHERE ids = ?";

        try {
            // Check if participation entry exists for the given servicetId
            pst = conn.prepareStatement(queryCheck);
            pst.setInt(1, serviceId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // If entry exists, update the nbr_de_participant
                pst = conn.prepareStatement(queryUpdate);
                pst.setInt(1, serviceId);
                pst.executeUpdate();
            } else {
                // If entry doesn't exist, insert a new entry
                pst = conn.prepareStatement(queryInsert);
                pst.setInt(1, serviceId);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}