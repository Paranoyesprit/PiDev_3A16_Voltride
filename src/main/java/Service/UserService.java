package Service;

import Entities.Admin;
import Entities.User;
import util.DataSource;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService implements IService<User>{

    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;

    public UserService() {
        conn= DataSource.getInstance().getCnx();
    }


    // Méthode pour ajouter un utilisateur
    public void add(User u) {
        boolean isValid = true;

        // Vérifier la validité des données
        if (!validateCIN(u.getCin())) {
            JOptionPane.showMessageDialog(null, "Le CIN de l'utilisateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateEmail(u.getEmail())) {
            JOptionPane.showMessageDialog(null, "L'email de l'utilisateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateName(u.getNom())) {
            JOptionPane.showMessageDialog(null, "Le nom de l'utilisateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateName(u.getPrenom())) {
            JOptionPane.showMessageDialog(null, "Le prénom de l'utilisateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        // Vérifier si l'email est unique
        if (!isEmailUnique(u.getEmail())) {
            JOptionPane.showMessageDialog(null, "L'email est déjà utilisé par un autre utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        if (!isValid) {
            return;
        }

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
            JOptionPane.showMessageDialog(null, "Bienvenu à VOLTRIDE.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour vérifier la validité du CIN (8 chiffres)
    private boolean validateCIN(int cin) {
        String cinString = String.valueOf(cin);
        return cinString.matches("\\d{9}");
    }

    // Méthode pour vérifier la validité de l'email
    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Méthode pour vérifier que le nom ou prénom contiennent seulement des lettres
    private boolean validateName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    // Méthode pour vérifier si l'email est unique
    private boolean isEmailUnique(String email) {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return count == 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Autres méthodes de UserService restent inchangées...

    public void addAdmin(Admin admin) {
        boolean isValid = true;

        // Vérifier la validité des données
        if (!validateCIN(admin.getCin())) {
            JOptionPane.showMessageDialog(null, "Le CIN de l'administrateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateEmail(admin.getEmail())) {
            JOptionPane.showMessageDialog(null, "L'email de l'administrateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateName(admin.getNom())) {
            JOptionPane.showMessageDialog(null, "Le nom de l'administrateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateName(admin.getPrenom())) {
            JOptionPane.showMessageDialog(null, "Le prénom de l'administrateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        if (!isValid) {
            return;
        }

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
            JOptionPane.showMessageDialog(null, "Administrateur ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de l'administrateur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
        boolean isValid = true;

        // Vérifier la validité des données
        if (!validateCIN(user.getCin())) {
            JOptionPane.showMessageDialog(null, "Le CIN de l'utilisateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateEmail(user.getEmail())) {
            JOptionPane.showMessageDialog(null, "L'email de l'utilisateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateName(user.getNom())) {
            JOptionPane.showMessageDialog(null, "Le nom de l'utilisateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateName(user.getPrenom())) {
            JOptionPane.showMessageDialog(null, "Le prénom de l'utilisateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        // Vérifier si l'email est unique (sauf pour l'utilisateur que vous mettez à jour)
        String currentUserEmail = readbyId(id_u).getEmail();
        if (!currentUserEmail.equals(user.getEmail()) && !isEmailUnique(user.getEmail())) {
            JOptionPane.showMessageDialog(null, "L'email est déjà utilisé par un autre utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        if (!isValid) {
            return;
        }

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
    public boolean verifyCINByEmail(String email, String cin) {
        String requete = "SELECT * FROM utilisateur WHERE email = ? AND cin = ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setString(1, email);
            pst.setString(2, cin);
            ResultSet rs = pst.executeQuery();
            return rs.next(); // Si le résultat de la requête contient une ligne, cela signifie que le numéro de CIN est valide pour cet e-mail
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePasswordByEmail(String email, String encryptedPassword) {
        String requete = "UPDATE utilisateur SET mot_de_passe = ? WHERE email = ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setString(1, encryptedPassword);
            pst.setString(2, email);
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
        boolean isValid = true;

        // Vérifier la validité des données de l'administrateur
        if (!validateCIN(admin.getCin())) {
            JOptionPane.showMessageDialog(null, "Le CIN de l'administrateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateEmail(admin.getEmail())) {
            JOptionPane.showMessageDialog(null, "L'email de l'administrateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateName(admin.getNom())) {
            JOptionPane.showMessageDialog(null, "Le nom de l'administrateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }
        if (!validateName(admin.getPrenom())) {
            JOptionPane.showMessageDialog(null, "Le prénom de l'administrateur est invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        // Vérifier si l'email est unique (sauf pour l'administrateur que vous mettez à jour)
        String currentAdminEmail = readAdminById(id_u).getEmail();
        if (!currentAdminEmail.equals(admin.getEmail()) && !isEmailUnique(admin.getEmail())) {
            JOptionPane.showMessageDialog(null, "L'email est déjà utilisé par un autre administrateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        if (!isValid) {
            return;
        }
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

    public String getPasswordByEmail(String email) {
        String requete = "SELECT mot_de_passe FROM utilisateur WHERE email = ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("mot_de_passe");
            } else {
                System.out.println("User not found for email: " + email);
                return null; // Ou lancez une exception si nécessaire
            }
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

}
