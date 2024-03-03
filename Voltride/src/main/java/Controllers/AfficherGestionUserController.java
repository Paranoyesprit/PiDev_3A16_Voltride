package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficherGestionUserController {

    // Méthode pour afficher la page 1.fxml
    @FXML
    void admins(ActionEvent event) {
        loadFXML("/AfficherAdmin.fxml");
    }

    // Méthode pour afficher la page 2.fxml
    @FXML
    void clients(ActionEvent event) {
        loadFXML("/AfficherUser.fxml");
    }

    // Méthode générique pour charger un fichier FXML
    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement du fichier FXML
        }
    }
    @FXML
    void Deconnexion(ActionEvent event) {
        redirectToPage("/Login.fxml");


    }


    @FXML
    private Button loginButton;

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
}
