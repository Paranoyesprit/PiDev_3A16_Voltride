package Controllers;

import Controllers.listController;
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
            Float kl = Float.parseFloat(id_kilometrage.getText());
            Float px = Float.parseFloat(id_prix.getText());
            Voiture v = new Voiture(mq, md, et, px, kl);
            VoitureService vs = new VoitureService();
            vs.addV(v);
            showAlert("Succées","voiture ajoutée avec succés");

            // Load the data in listController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/list.fxml"));

            Parent root = loader.load();
            listController listController = loader.getController();
            listController.loadVoitureData();

            // Show listController GUI

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

