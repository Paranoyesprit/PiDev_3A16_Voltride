package Service;

import Entities.Admin;
import Entities.User;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User>{

    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;

    public UserService() {
        conn= DataSource.getInstance().getCnx();
    }

    public void add(User u) {
        String requette = "INSERT INTO utilisateur (cin, nom, prenom, email, mot_de_passe, date_inscription, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(requette);
            pst.setInt(1, u.getCin());
            pst.setString(2, u.getNom());
            pst.setString(3, u.getPrenom());
            pst.setString(4, u.getEmail());
            pst.setString(5, u.getMotDePasse());
            pst.setDate(6, new java.sql.Date(u.getDateInscription().getTime()));
            pst.setString(7, u.getImage());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id_u) {
        String deleteUserQuery = "DELETE FROM utilisateur WHERE id_u = ?";
        try {
            pst = conn.prepareStatement(deleteUserQuery);
            pst.setInt(1, id_u);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int id_u, User user) {
        String updateUserQuery = "UPDATE utilisateur SET cin=?, nom=?, prenom=?, email=?, mot_de_passe=?, date_inscription=?, image=? WHERE id_u=?";
        try {
            pst = conn.prepareStatement(updateUserQuery);
            pst.setInt(1, user.getCin());
            pst.setString(2, user.getNom());
            pst.setString(3, user.getPrenom());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getMotDePasse());
            pst.setDate(6, new java.sql.Date(user.getDateInscription().getTime()));
            pst.setString(7, user.getImage());
            pst.setInt(8, id_u);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> readAll() {
        String requette = "SELECT * FROM utilisateur";
        List<User> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(requette);
            while (rs.next()) {
                int id_u = rs.getInt("id_u");
                int cin = rs.getInt("cin");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("mot_de_passe");
                Date dateInscription = rs.getDate("date_inscription");
                String image = rs.getString("image");

                User user = new User(id_u, cin, nom, prenom, email, motDePasse, dateInscription, image);
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<User> readAllU() {
        String requette = "SELECT * FROM utilisateur LEFT JOIN admin ON utilisateur.id_u = admin.id_u WHERE admin.id_u IS NULL";
        List<User> list = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(requette);
            while (rs.next()) {
                int id_u = rs.getInt("id_u");
                int cin = rs.getInt("cin");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("mot_de_passe");
                Date dateInscription = rs.getDate("date_inscription");
                String image = rs.getString("image");

                User user = new User(id_u, cin, nom, prenom, email, motDePasse, dateInscription, image);
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    @Override
    public User readbyId(int id_u) {
        String requette = "SELECT * FROM utilisateur WHERE id_u = ?";
        try {
            pst = conn.prepareStatement(requette);
            pst.setInt(1, id_u);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int cin = rs.getInt("cin");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("mot_de_passe");
                Date dateInscription = rs.getDate("date_inscription");
                String image = rs.getString("image");

                return new User(id_u, cin, nom, prenom, email, motDePasse, dateInscription, image);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User getUserIdByEmail(String email) {
        String requete = "SELECT * FROM utilisateur WHERE email = ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id_u = rs.getInt("id_u");
                int cin = rs.getInt("cin");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String motDePasse = rs.getString("mot_de_passe");
                Date dateInscription = rs.getDate("date_inscription");
                String image = rs.getString("image");

                // Créer et retourner l'objet User avec les informations récupérées
                return new User(id_u, cin, nom, prenom, email, motDePasse, dateInscription, image);
            } else {
                System.out.println("User not found for email: " + email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void addAdmin(Admin admin) {
        String addUserQuery = "INSERT INTO utilisateur (cin, nom, prenom, email, mot_de_passe, date_inscription, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(addUserQuery, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, admin.getCin());
            pst.setString(2, admin.getNom());
            pst.setString(3, admin.getPrenom());
            pst.setString(4, admin.getEmail());
            pst.setString(5, admin.getMotDePasse());
            pst.setDate(6, new java.sql.Date(admin.getDateInscription().getTime()));
            pst.setString(7, admin.getImage());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            String addAdminQuery = "INSERT INTO admin (id_u, departement) VALUES (?, ?)";
            pst = conn.prepareStatement(addAdminQuery);
            pst.setInt(1, userId);
            pst.setString(2, admin.getDepartement());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Admin getAdminByEmail(String email) {
        String query = "SELECT * FROM utilisateur JOIN admin ON utilisateur.id_u = admin.id_u WHERE utilisateur.email = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapAdminFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'administrateur par e-mail.", e);
        }
        return null;
    }

    private Admin mapAdminFromResultSet(ResultSet rs) throws SQLException {
        int id_u = rs.getInt("id_u");
        int cin = rs.getInt("cin");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String email = rs.getString("email");
        String motDePasse = rs.getString("mot_de_passe");
        Date dateInscription = rs.getDate("date_inscription");
        String image = rs.getString("image");
        String departement = rs.getString("departement");
        return new Admin(id_u, cin, nom, prenom, email, motDePasse, dateInscription, image, departement);
    }

    public List<Admin> readAllAdmins() {
        String query = "SELECT utilisateur.id_u, utilisateur.cin, utilisateur.nom, utilisateur.prenom, utilisateur.email, utilisateur.mot_de_passe, utilisateur.date_inscription, utilisateur.image, admin.departement " +
                "FROM utilisateur " +
                "INNER JOIN admin ON utilisateur.id_u = admin.id_u";
        List<Admin> adminList = new ArrayList<>();
        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(query);
            while (rs.next()) {
                int id_u = rs.getInt("id_u");
                int cin = rs.getInt("cin");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("mot_de_passe");
                Date dateInscription = rs.getDate("date_inscription");
                String image = rs.getString("image");
                String departement = rs.getString("departement");

                Admin admin = new Admin(id_u, cin, nom, prenom, email, motDePasse, dateInscription, image, departement);
                adminList.add(admin);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return adminList;
    }

    public void deleteAdmin(int id_u) {
        String query = "DELETE FROM admin WHERE id_u = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, id_u);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAdmin(int id_u, Admin admin) {
        String updateAdminQuery = "UPDATE utilisateur SET cin=?, nom=?, prenom=?, email=?, mot_de_passe=?, date_inscription=?, image=? WHERE id_u=?";
        try {
            pst = conn.prepareStatement(updateAdminQuery);
            pst.setInt(1, admin.getCin());
            pst.setString(2, admin.getNom());
            pst.setString(3, admin.getPrenom());
            pst.setString(4, admin.getEmail());
            pst.setString(5, admin.getMotDePasse());
            pst.setDate(6, new java.sql.Date(admin.getDateInscription().getTime()));
            pst.setString(7, admin.getImage());
            pst.setInt(8, id_u);
            pst.executeUpdate();

            // Mise à jour de la table admin pour le département
            String updateAdminDepartementQuery = "UPDATE admin SET departement=? WHERE id_u=?";
            pst = conn.prepareStatement(updateAdminDepartementQuery);
            pst.setString(1, admin.getDepartement());
            pst.setInt(2, id_u);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Admin readAdminById(int id_u) {
        String query = "SELECT * FROM admin INNER JOIN utilisateur ON admin.id_u = utilisateur.id_u WHERE admin.id_u = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, id_u);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int cin = rs.getInt("cin");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("mot_de_passe");
                Date dateInscription = rs.getDate("date_inscription");
                String image = rs.getString("image");
                String departement = rs.getString("departement");

                return new Admin(id_u, cin, nom, prenom, email, motDePasse, dateInscription, image, departement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    public int getUserIdFromSession() {
        int userId = -1;
        String query = "SELECT user_id FROM session ORDER BY login_time DESC LIMIT 1"; // Sélectionner le dernier user_id ajouté à la table sessions
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'identifiant de l'utilisateur depuis la table sessions.", e);
        }
        return userId;
    }


}
