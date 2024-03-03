package Controllers;

import Entities.Evenement;
import Service.EvenementService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private TextField placedispoTF;

    @FXML
    public void ajouter(javafx.event.ActionEvent event) {
        String type = typeTF.getText();
        String adresseEvenement = adresseEvenementTF.getText();
        LocalDate dateEvenement = dateEvenementTF.getValue();
        int placesDispo= Integer.parseInt(placedispoTF.getText());
        // Vérifier si le type a moins de 10 caractères
        if (type.length() >= 10) {
            showAlert("Erreur", "Le type doit avoir moins de 10 caractères.");
            return;
        }

        // Vérifier si l'addresse est valide
        if (adresseEvenement.length() >= 15) {
            showAlert("Erreur", "L'adresse doit avoir au moins de 15 caracteres.");
            return;
        }

        // Regular expressions for checking if the input contains only letters
        String stringPattern = "[a-zA-Z]+";

        // Vérifier si tous les champs sont remplis et si les types sont valides
        if (type != null && type.matches(stringPattern) && adresseEvenement != null && adresseEvenement.matches(stringPattern) && dateEvenement != null) {
            ps.add(new Evenement(type, adresseEvenement, dateEvenement, placesDispo));
            // Afficher un message de succès
            showAlert("Succès", "L'événement a été ajouté avec succès.");

            // Fermer la fenêtre AjouterEvenement actuelle
            Stage stage = (Stage) typeTF.getScene().getWindow();
            stage.close();

            // Ouvrir la fenêtre AfficherEvenement
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
            // Afficher un message d'erreur si l'un des champs est vide ou contient des caractères invalides
            showAlert("Erreur", "Veuillez remplir tous les champs avec des valeurs de chaîne de caractères valides.");
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
