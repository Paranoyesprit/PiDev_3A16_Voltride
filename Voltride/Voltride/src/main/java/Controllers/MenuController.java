package Controllers;

import Service.UserService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MenuController {

    private Stage stage;
    private final UserService userService = new UserService();

    @FXML
    private Button CharfapageButton;

    @FXML
    private Button MahdipageButton;

    @FXML
    private Button FerpageButton;

    @FXML
    private Button AzizpageButton;

    @FXML
    private Button ParamButton;

    @FXML
    private Button DisconnectButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }



    @FXML
    void Charfapage(ActionEvent event) {
        redirectToPage("/Hbox.fxml", event);
    }

    @FXML
    void Mahdipage(ActionEvent event) {
        redirectToPage("/AfficherReservationBorne.fxml", event);
    }

    @FXML
    void Ferpage(ActionEvent event) {
        redirectToPage("/AfficherReservationE.fxml", event);
    }

    @FXML
    void Azizpage(ActionEvent event) {
        redirectToPage("/Chemin/Vers/PageAziz.fxml", event);
    }

    @FXML
    void Param(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserDash.fxml"));
            Pane userDashContent = loader.load();

            // Remplacer "aff" par l'ID correct du Pane dans votre fichier menu.fxml
            Pane affPane = (Pane) ((Node) event.getSource()).getScene().lookup("#aff");
            affPane.getChildren().setAll(userDashContent);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur de Navigation", "Impossible de charger la page de tableau de bord utilisateur. Veuillez réessayer.");
        }
    }


    @FXML
    void Deconnexion(ActionEvent event) {

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

    private void redirectToLoginPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur de Navigation", "Impossible de charger la page de connexion. Veuillez réessayer.");
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
