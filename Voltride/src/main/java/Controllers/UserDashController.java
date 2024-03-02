package Controllers;

import Entities.PasswordEncryption;
import Entities.User;
import Entities.UserSession;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class UserDashController {

    @FXML
    private TextField cin_um;

    @FXML
    private TextField email_um;


    @FXML
    private ImageView imageView;

    @FXML
    private TextField image_um;

    @FXML
    private Button modifieru;

    @FXML
    private TextField mp_um;

    @FXML
    private TextField nom_um;

    @FXML
    private TextField prenom_um;

    private final UserService userService = new UserService();

    private User usertomodify;


    public void initialize() {
        try {
            // Récupérer l'utilisateur courant depuis UserSession
            User currentUser = UserSession.getInstance().getCurrentUser();
            usertomodify = currentUser ;
            if (currentUser != null) {

                nom_um.setText(currentUser.getNom());
                prenom_um.setText(currentUser.getPrenom());
                cin_um.setText(String.valueOf(currentUser.getCin()));
                email_um.setText(currentUser.getEmail());
                String decodedPassword = PasswordEncryption.decrypt(currentUser.getMotDePasse());
                mp_um.setText(decodedPassword);
                image_um.setText(currentUser.getImage());
            } else {
                // Gérer le cas où l'utilisateur courant n'est pas défini dans la session
                // Affichez un message d'erreur ou gérez-le selon vos besoins
                throw new IllegalStateException("L'utilisateur courant n'est pas défini dans la session.");
            }
        } catch (Exception e) {
            // Gérer les exceptions qui peuvent survenir lors de l'initialisation des champs
            e.printStackTrace();
        }
    }



    @FXML
    void modifierUser(ActionEvent event) {
        try {
            if (usertomodify != null) {
                int cinmu = Integer.parseInt(cin_um.getText());
                usertomodify.setCin(cinmu);
                usertomodify.setNom(nom_um.getText());
                usertomodify.setPrenom(prenom_um.getText());
                usertomodify.setEmail(email_um.getText());
                String newPassword = PasswordEncryption.encrypt(mp_um.getText());
                usertomodify.setMotDePasse(newPassword);                usertomodify.setImage(image_um.getText());

                userService.update(usertomodify.getId_u(), usertomodify);

                showAlert("Modification réussie", "Modification réussie");
            } else {
                showAlert("Erreur", "Aucun utilisateur à modifier.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le format du numéro de CIN est incorrect.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification.");
        }
    }

    @FXML
    void supprimerUser(ActionEvent event) {
        try {
            if (usertomodify != null) {
                int userId = usertomodify.getId_u();
                userService.delete(userId);
                showAlert("Suppression réussie", "Votre compte a été supprimé avec succès.");
                redirectToLoginPage(event); // Redirection vers la page de connexion après la suppression
            } else {
                showAlert("Erreur", "Aucun utilisateur à supprimer.");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la suppression.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Mettez à jour le champ de texte avec le chemin absolu du fichier sélectionné
            image_um.setText(selectedFile.getAbsolutePath());
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }



    private void redirectToLoginPage(ActionEvent event) {
        try {
            // Charger l'interface de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la redirection vers la page de connexion.");
        }
    }





}
