package gestion_service_apreslocation.service;

import gestion_service_apreslocation.entities.Type;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeService implements IServiceS<Type> {

    private Connection conn;
    private PreparedStatement pst;

    public TypeService() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(Type type) {
        String query = "INSERT INTO Type (typeName) VALUES (?)";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, type.getTypeName());
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Type WHERE id = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int id, Type type) {
        String query = "UPDATE Type SET typeName = ? WHERE id = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, type.getTypeName());
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Type> readAll() {
        String query = "SELECT * FROM Type";
        List<Type> list = new ArrayList<>();
        try {
            pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id"); // Retrieve the ID from the database
                String typeName = rs.getString("typeName");
                Type type = new Type(typeName);
                type.setId(id); // Set the ID to the Type object
                list.add(type);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }



    @Override
    public Type readById(int id) {
        String query = "SELECT * FROM Type WHERE id = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Type type = new Type(rs.getString("typeName"));
                type.setId(rs.getInt("id")); // Set the correct ID
                return type;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public List<Type> getAllTypes() {
        List<Type> types = new ArrayList<>();
        String query = "SELECT * FROM Type";
        try {
            pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Type type = new Type(rs.getString("typeName"));
                types.add(type);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return types;
    }

    public Type getTypeByName(String typeName) {
        String query = "SELECT * FROM Type WHERE typeName = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, typeName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Type type = new Type(rs.getString("typeName"));
                type.setId(rs.getInt("id"));
                return type;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}