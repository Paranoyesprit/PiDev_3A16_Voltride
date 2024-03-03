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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ajouterVoitureController {

    private final VoitureService vs = new VoitureService();

    // Déclaration d'une variable pour stocker le chemin de l'image
    private String imagePath;

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
    void ajouter(ActionEvent event) {
        String mq = id_marque.getText();
        String md = id_modele.getText();
        String et = id_etat.getText();
        String klText = id_kilometrage.getText();
        String pxText = id_prix.getText();

        // Check if any of the input fields are empty
        if (mq.isEmpty() || md.isEmpty() || et.isEmpty() || klText.isEmpty() || pxText.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return; // Stop execution if any field is empty
        }

        // Validate numeric inputs for kilometrage and prix
        float kl, px;
        try {
            kl = Float.parseFloat(klText);
            px = Float.parseFloat(pxText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid numeric format for kilometrage or prix.");
            return; // Stop execution if invalid numeric format
        }

        // Vérifier si l'image a été sélectionnée
        if (imagePath == null || imagePath.isEmpty()) {
            showAlert("Error", "Please select an image.");
            return; // Stop execution if no image is selected
        }

        // Si tous les champs sont valides, procéder à l'ajout de la voiture
        Voiture v = new Voiture(mq, md, et, px, kl, imagePath);
        VoitureService vs = new VoitureService();
        vs.addV(v);
        showAlert("Success", "Voiture ajoutée avec succès.");

        // Charger les données dans listController
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/list.fxml"));
            Parent root = loader.load();
            listController listController = loader.getController();
            listController.loadVoitureData();
        } catch (IOException e) {
            showAlert("Error", "An error occurred while loading voiture data.");
        }
    }

    @FXML
    void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        // Filtrer uniquement les fichiers image
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Afficher la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(null);

        // Vérifier si l'utilisateur a sélectionné un fichier
        if (selectedFile != null) {
            // Récupérer le chemin absolu du fichier sélectionné
            imagePath = selectedFile.getAbsolutePath();
            // Vous pouvez afficher le chemin de l'image ou l'utiliser pour autre chose si nécessaire
            System.out.println("Chemin de l'image sélectionnée : " + imagePath);
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
