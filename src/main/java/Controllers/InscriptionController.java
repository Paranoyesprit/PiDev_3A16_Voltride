package Controllers;
import Entities.PasswordEncryption;
import Entities.User;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Base64;

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
    public void ajouterClient(ActionEvent event) {
        try {
            // Crypter le mot de passe avec AES
            String password = PasswordEncryption.encrypt(mp_c.getText());

            // Créez une nouvelle instance d'utilisateur avec les informations saisies par l'utilisateur
            User user = new User(
                    Integer.parseInt(cin_c.getText()),
                    nom_c.getText(),
                    prenom_c.getText(),
                    email_c.getText(),
                    password,
                    new Date(System.currentTimeMillis()),
                    image_c.getText()
            );

            // Ajouter l'utilisateur en utilisant le service UserService
            us.add(user);

            // Recharger la scène de connexion après l'ajout de l'utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            LoginController loginController = loader.getController();
            inscription_c.getScene().setRoot(root);

        } catch (Exception e) {
            // Gérez les exceptions qui peuvent survenir pendant l'ajout de l'utilisateur
            e.printStackTrace();
        }
    }



    @FXML
    public void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            image_c.setText(selectedFile.getAbsolutePath());
        }
    }
}