package Controllers;


import Entities.PasswordEncryption;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.util.Base64;
import java.util.Random;

public class Getpasscontoller {

    @FXML
    private PasswordField cin_log;

    @FXML
    private TextField email_log;

    private final UserService userService = new UserService();


    @FXML
    void pwd(ActionEvent event) throws Exception {
        String email = email_log.getText();
        String cin = cin_log.getText(); // Suppose que cin_log est le champ où l'utilisateur saisit le numéro de CIN
        String password = generatePassword(); // Générer un mot de passe aléatoire
        String encryptedPassword = PasswordEncryption.encrypt(password); // Crypter le mot de passe en base 64

        // Vérifier si le numéro de CIN correspond à l'e-mail fourni
        if (userService.verifyCINByEmail(email, cin)) {
            try {
                // Mettre à jour le mot de passe crypté dans la base de données
                userService.updatePasswordByEmail(email, encryptedPassword);

                // Envoi de l'e-mail avec le nouveau mot de passe non crypté
                String subject = "Nouveau mot de passe";
                String message = "Votre nouveau mot de passe est : " + password;
                Gmailer gmailer = new Gmailer();
                gmailer.sendMail(email, subject, message); // Utiliser l'adresse e-mail saisie comme destinataire
                showAlert("Succès", "Le nouveau mot de passe a été envoyé à votre adresse e-mail.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de l'envoi de l'e-mail ou de la mise à jour du mot de passe.");
            }
        } else {
            showAlert("Erreur", "Numéro de CIN incorrect pour cette adresse e-mail.");
        }
    }

    // Méthode pour générer un mot de passe aléatoire
    private String generatePassword() {
        // Implémentation de la génération de mot de passe aléatoire (à adapter selon vos besoins)
        // Ici, nous générons un mot de passe de 8 caractères composé de chiffres et de lettres majuscules et minuscules.
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }
    private void showAlert(String title, String content) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.ERROR_MESSAGE);
    }
}
