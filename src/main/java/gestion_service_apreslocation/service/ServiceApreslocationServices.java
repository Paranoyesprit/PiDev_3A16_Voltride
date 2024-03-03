package gestion_service_apreslocation.service;

import gestion_service_apreslocation.entities.ServiceApreslocation;
import utils.DataSource;
import gestion_service_apreslocation.entities.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceApreslocationServices implements IServiceS<ServiceApreslocation> {

    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;


    public ServiceApreslocationServices() {
        conn = DataSource.getInstance().getCnx();
    }


    @Override
    public void add(ServiceApreslocation l) {
        String requete = "INSERT INTO service_apreslocation (type, technicien, description, statut, cout) VALUES (?, ?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, l.getType().getId());
            pst.setString(2,l.getTechnicien());
            pst.setString(3, l.getDescription());
            pst.setString(4, l.getStatut());
            pst.setDouble(5, l.getCout());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int i) {
        String requete = "DELETE FROM service_apreslocation WHERE idservice = ?";
        try{
            pst = conn.prepareStatement(requete);
            pst.setInt(1 ,i);
            pst.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(int i, ServiceApreslocation l) {
        String requete = "UPDATE service_apreslocation SET type = ?, technicien = ?, description= ?, statut = ?, cout = ? WHERE idservice =?";
        try{
            pst = conn.prepareStatement(requete);
            pst.setInt(1, l.getType().getId());
            pst.setString(2,l.getTechnicien());
            pst.setString(3, l.getDescription());
            pst.setString(4, l.getStatut());
            pst.setDouble(5, l.getCout());
            pst.setInt(6, i);
            pst.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ServiceApreslocation> readAll() {
        String requete = "select * from service_apreslocation";
        List<ServiceApreslocation> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(requete);
            while (rs.next()) {
                int typeId = rs.getInt("type");
                Type type = readTypeById(typeId);
                ServiceApreslocation sa = new ServiceApreslocation(
                        type,
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getDouble(6) // Assuming this is the correct column index for the service ID
                );
                sa.setIdservice(rs.getInt(1)); // Setting the service ID after creating the object

                list.add(sa);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public ServiceApreslocation readById(int id) {
        String query = "SELECT * FROM service_apreslocation WHERE idservice = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int typeId = rs.getInt("type");
                Type type = readTypeById(typeId);
                ServiceApreslocation service = new ServiceApreslocation(
                        type,
                        rs.getString("technicien"),
                        rs.getString("description"),
                        rs.getString("statut"),
                        rs.getDouble("cout")
                );
                service.setIdservice(rs.getInt("idservice")); // Set the ID here
                return service;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    private Type readTypeById(int id) {
       String query = "SELECT * FROM Type WHERE id = ?";
       try {
           pst = conn.prepareStatement(query);
           pst.setInt(1, id);
           ResultSet rs = pst.executeQuery();
           if (rs.next()) {
               return new Type(rs.getString("typeName"));
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
       return null;
   }
    public int getNumberOfParticipants(int serviceId) {
        int numberOfParticipants = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DataSource.getInstance().getCnx();
            String query = "SELECT COUNT(*) AS participant_count FROM participation WHERE ids = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, serviceId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                numberOfParticipants = resultSet.getInt("participant_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberOfParticipants;
    }
}


