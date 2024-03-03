package Controllers;

import Entities.Admin;
import Entities.PasswordEncryption;
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
                String password;
                try {
                        password = PasswordEncryption.encrypt(mp_log.getText());
                } catch (Exception e) {
                        showAlert("Erreur", "Une erreur s'est produite lors du cryptage du mot de passe.");
                        return;
                }

                if (email.isEmpty() || password.isEmpty()) {
                        showAlert("Erreur de Connexion", "Veuillez remplir tous les champs.");
                        return;
                }

                Admin admin = userService.getAdminByEmail(email);
                User user = userService.getUserIdByEmail(email);

                if (admin != null && admin.getMotDePasse().equals(password)) {
                        redirectToAdminPage(admin.getDepartement());
                } else if (user != null && user.getMotDePasse().equals(password)) {
                        UserSession.getInstance().setCurrentUser(user);
                        redirectToMenu();
                } else {
                        showAlert("Erreur de Connexion", "Adresse e-mail ou mot de passe incorrect. Veuillez réessayer.");
                }
        }

        private void redirectToAdminPage(String department) {
                String adminPagePath = switch (department) {
                        case "G.VOITURES" -> "/list.fxml";
                        case "G.BORNES" -> "/AfficherBorne.fxml";
                        case "G.EVENENEMENTS" -> "/AfficherEvenement.fxml";
                        case "G.SERVICES_AL" -> "/AfficherServiceApreslocation.fxml";
                        case "G.USERS" -> "/AfficherGestionUser.fxml";
                        default -> "/UserDash.fxml";
                };
                redirectToPage(adminPagePath);
        }

        private void redirectToMenu() {
                redirectToPage("/Menu.fxml");
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(content);
                alert.showAndWait();
        }

        @FXML
        void inscri(ActionEvent event) {
                redirectToPage("/Inscription.fxml", event);
        }

        @FXML
        void pwd(ActionEvent event) {
                redirectToPage("/Getpass.fxml", event);
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
