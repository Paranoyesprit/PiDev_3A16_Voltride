package Controllers;

import Controllers.AfficherAdminController;
import Entities.Admin;
import Entities.PasswordEncryption;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

public class AjouterAdminController {

    private final UserService us = new UserService();

    @FXML
    private TextField cin_a;

    @FXML
    private TextField email_a;

    @FXML
    private TextField image_a;

    @FXML
    private TextField mp_a;

    @FXML
    private TextField nom_a;

    @FXML
    private TextField prenom_a;

    @FXML
    private ComboBox<String> dep;

    @FXML
    private Button ajoutadmin;

    @FXML
    void ajouterAdmin(ActionEvent event) {
        try {
            // Crypter le mot de passe en utilisant AES
            String password = PasswordEncryption.encrypt(mp_a.getText());

            // Créer une nouvelle instance d'Admin avec les informations saisies par l'utilisateur
            Admin admin = new Admin(
                    Integer.parseInt(cin_a.getText()),
                    nom_a.getText(),
                    prenom_a.getText(),
                    email_a.getText(),
                    password,
                    new Date(System.currentTimeMillis()),
                    image_a.getText(),
                    dep.getSelectionModel().getSelectedItem() // Sélectionner l'élément actuellement sélectionné dans le ComboBox
            );

            // Ajouter cet administrateur en utilisant le service UserService
            us.addAdmin(admin);

            // Charger la vue AfficherAdmin.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAdmin.fxml"));
            Parent root = loader.load();
            AfficherAdminController listController = loader.getController();
            listController.loadAdminData();
            closeWindow(event);
        } catch (Exception e) {
            // Gérer les exceptions qui peuvent survenir pendant l'ajout de l'administrateur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Mettre à jour le champ de texte avec le chemin absolu du fichier sélectionné
            image_a.setText(selectedFile.getAbsolutePath());
        }
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
