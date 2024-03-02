package Controllers;

import Entities.Admin;
import Entities.User;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;


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
    void ajouterAdmin(ActionEvent event)  {
        try {
            // Créez une nouvelle instance d'Admin avec les informations saisies par l'utilisateur
            Admin admin = new Admin(
                    Integer.parseInt(cin_a.getText()),
                    nom_a.getText(),
                    prenom_a.getText(),
                    email_a.getText(),
                    mp_a.getText(),
                    new Date(System.currentTimeMillis()),
                    image_a.getText(),
                    dep.getSelectionModel().getSelectedItem() // Sélectionnez l'élément actuellement sélectionné dans le ComboBox
            );

            // Ajoutez cet administrateur en utilisant le service UserService
            us.addAdmin(admin);
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

