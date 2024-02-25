package Controller;

import Entities.Voiture;
import Service.VoitureService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ajouterVoitureController {

    private final VoitureService vs = new VoitureService();

    @FXML
    private TextField id_etat;

    @FXML
    private TextField id_kilometrage;

    @FXML
    private TextField id_marque;

    @FXML
    private TextField id_modele;

    @FXML
    private TextField id_prix;

    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        try {
            String mq = id_marque.getText();
            String md = id_modele.getText();
            String et = id_etat.getText();
            String kilomStr = id_kilometrage.getText();
            String prixStr = id_prix.getText();

            // Vérifier si les champs obligatoires sont vides
            if (mq.isEmpty() || md.isEmpty() || et.isEmpty() || kilomStr.isEmpty() || prixStr.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
                return; // Sortir de la méthode si les champs obligatoires ne sont pas remplis
            }

            // Vérifier si les champs numériques sont valides
            try {
                Float kl = Float.parseFloat(kilomStr);
                Float px = Float.parseFloat(prixStr);

                // Créer une voiture uniquement si les champs numériques sont valides
                Voiture v = new Voiture(mq, md, et, px, kl);
                VoitureService vs = new VoitureService();
                vs.addV(v);
                showAlert("Succès", "Voiture ajoutée avec succès");

                // Recharger les données dans listController


                // Close the current AjouterBorne window
                Stage stage = (Stage) id_kilometrage.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/list.fxml"));
                Parent root = loader.load();
                listController listController = loader.getController();
                listController.loadVoitureData();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez saisir des valeurs numériques valides pour le kilométrage et le prix.");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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

