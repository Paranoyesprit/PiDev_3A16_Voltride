package Controllers;

import Entities.Admin;
import Entities.User;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Date;

public class InscriptionController {

    private final UserService us = new UserService();


    @FXML
    private TextField cin_c;

    @FXML
    private TextField email_c;

    @FXML
    private TextField image_c;

    @FXML
    private Button inscription_c;

    @FXML
    private TextField mp_c;

    @FXML
    private TextField nom_c;

    @FXML
    private TextField prenom_c;

    @FXML
    void ajouterClient(ActionEvent event) {
        try {
            // Créez une nouvelle instance d'Admin avec les informations saisies par l'utilisateur
            User user = new User(
                    Integer.parseInt(cin_c.getText()),
                    nom_c.getText(),
                    prenom_c.getText(),
                    email_c.getText(),
                    mp_c.getText(),
                    new Date(System.currentTimeMillis()),
                    image_c.getText()
            );

            // Ajoutez cet administrateur en utilisant le service UserService
            us.add(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAdmin.fxml"));

            Parent root = loader.load();
            AfficherAdminController listController = loader.getController();
            listController.loadAdminData();
        } catch (Exception e) {
            // Gérez les exceptions qui peuvent survenir pendant l'ajout de l'administrateur
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    }


