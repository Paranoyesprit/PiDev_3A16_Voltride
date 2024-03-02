package Controllers;

import Entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Service.EvenementService;

import java.io.IOException;
import java.time.LocalDate;

public class AjouterEvenementController {
    private final EvenementService ps = new EvenementService();

    @FXML
    private TextField adresseEvenementTF;

    @FXML
    private DatePicker dateEvenementTF;

    @FXML
    private TextField typeTF;

    @FXML
    void ajouter(ActionEvent event) {
        String type = typeTF.getText();
        String adresseEvenement = adresseEvenementTF.getText();
        LocalDate dateEvenement = dateEvenementTF.getValue();

        // Regular expressions for checking if the input contains only letters
        String stringPattern = "[a-zA-Z]+";

        if (type != null && type.matches(stringPattern) && adresseEvenement != null && adresseEvenement.matches(stringPattern) && dateEvenement != null) {
            ps.add(new Evenement(type, adresseEvenement, dateEvenement));
            // Show success message
            showAlert("Success", "Event added successfully.");

            // Close the current AjouterBorne window
            Stage stage = (Stage) typeTF.getScene().getWindow();
            stage.close();

            // Open AfficherBorne window
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage afficherStage = new Stage();
                afficherStage.setScene(scene);
                afficherStage.setTitle("Afficher Evenement");
                afficherStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Show error message if any of the fields are empty or contain invalid characters
            showAlert("Error", "Please fill in all the fields with valid string values.");
        }
    }




    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
