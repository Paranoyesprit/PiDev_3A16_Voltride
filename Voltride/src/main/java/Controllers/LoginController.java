package Controllers;

import Entities.Admin;
import Entities.User;
import Entities.UserSession;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import javax.swing.*;
import java.io.IOException;

public class LoginController {

        @FXML
        private Button loginButton;

        @FXML
        private TextField email_log;

        @FXML
        private PasswordField mp_log;

        private final UserService userService = new UserService();
        private Stage stage;

        public void setStage(Stage stage) {
                this.stage = stage;
        }

        @FXML
        void login(ActionEvent event) {
                String email = email_log.getText();
                String password = mp_log.getText();

                if (email.isEmpty() || password.isEmpty()) {
                        showAlert("Erreur de Connexion", "Veuillez remplir tous les champs.");
                        return;
                }

                Admin admin = userService.getAdminByEmail(email);
                User user = userService.getUserIdByEmail(email);

                if (admin != null && admin.getMotDePasse().equals(password)) {
                        redirectToAdminPage(admin.getDepartement());
                } else if (user != null && user.getMotDePasse().equals(password)) {
                        // Si l'utilisateur se connecte avec succès, ajoutez une session
                        if (user != null && user.getMotDePasse().equals(password)) {
                                UserSession.getInstance().setCurrentUser(user);
                                redirectToMenu();
                        }

                        redirectToMenu();
                } else {
                        showAlert("Erreur de Connexion", "Adresse e-mail ou mot de passe incorrect. Veuillez réessayer.");
                }
        }

        private void redirectToAdminPage(String department) {
                String adminPagePath;
                switch (department) {
                        case "G.VOITURES":
                                adminPagePath = "/1.fxml";
                                break;
                        case "G.BORNES":
                                adminPagePath = "/2.fxml";
                                break;
                        case "G.EVENEMENTS":
                                adminPagePath = "/3.fxml";
                                break;
                        case "G.SERVICES_AL":
                                adminPagePath = "/4.fxml";
                                break;
                        case "G.USERS":
                                adminPagePath = "/AfficherGestionUser.fxml";
                                break;
                        default:
                                adminPagePath = "/UserDash.fxml";
                                break;
                }
                redirectToPage(adminPagePath);
        }

        private void redirectToMenu() {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Menu.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage currentStage = (Stage) loginButton.getScene().getWindow();
                        currentStage.setScene(scene);
                        currentStage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("Erreur de Navigation", "Impossible de charger le menu. Veuillez réessayer.");
                }
        }

        private void redirectToPage(String pagePath) {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(pagePath));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage currentStage = (Stage) loginButton.getScene().getWindow();
                        currentStage.setScene(scene);
                        currentStage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("Erreur de Navigation", "Impossible de charger la page. Veuillez réessayer.");
                }
        }

        private void showAlert(String title, String content) {
                JOptionPane.showMessageDialog(null, content, title, JOptionPane.ERROR_MESSAGE);
        }

        @FXML
        void inscri(ActionEvent event) {

                redirectToPage("/Inscription.fxml", event);

        }
        private void redirectToPage(String pagePath, ActionEvent event) {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(pagePath));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        currentStage.setScene(scene);
                        currentStage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("Erreur de Navigation", "Impossible de charger la page. Veuillez réessayer.");
                }
        }
}
