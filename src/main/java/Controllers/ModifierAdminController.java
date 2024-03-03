package Controllers;

import Entities.Admin;
import Entities.PasswordEncryption;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Base64;

public class ModifierAdminController {

    @FXML
    private TextField cin_am;

    @FXML
    private ComboBox<String> depm;

    @FXML
    private TextField email_am;

    @FXML
    private TextField image_am;

    @FXML
    private Button modifiera;

    @FXML
    private TextField mp_am;

    @FXML
    private TextField nom_am;

    @FXML
    private TextField prenom_am;

    private final UserService us = new UserService();

    private Admin AdminToModify;

    public void initData(Admin admin) throws Exception {
        this.AdminToModify = admin;
        nom_am.setText(admin.getNom());
        prenom_am.setText(admin.getPrenom());
        cin_am.setText(String.valueOf(admin.getCin()));
        email_am.setText(admin.getEmail());
        String decodedPassword = PasswordEncryption.decrypt(admin.getMotDePasse());
        mp_am.setText(decodedPassword);
        image_am.setText(admin.getImage());
        String departement = admin.getDepartement();
        depm.setValue(departement);
    }

    private boolean updateAdmin() {
        try {
            // Récupérer les valeurs des champs texte
            int cinm = Integer.parseInt(cin_am.getText());
            String nom = nom_am.getText();
            String prenom = prenom_am.getText();
            String email = email_am.getText();
            String motDePasse = PasswordEncryption.encrypt(mp_am.getText());
            String image = image_am.getText();
            String departement = depm.getValue();

            // Mettre à jour les propriétés de l'objet AdminToModify
            AdminToModify.setCin(cinm);
            AdminToModify.setNom(nom);
            AdminToModify.setPrenom(prenom);
            AdminToModify.setEmail(email);
            AdminToModify.setMotDePasse(motDePasse);
            AdminToModify.setImage(image);
            AdminToModify.setDepartement(departement);

            // Appeler la méthode de service pour mettre à jour l'administrateur dans la base de données
            us.updateAdmin(AdminToModify.getId_u(), AdminToModify);

            // Retourner true si la mise à jour a réussi
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // Afficher les détails de l'exception
            System.err.println("Erreur lors de la mise à jour de l'administrateur : " + e.getMessage());
            // Retourner false en cas d'erreur
            return false;
        }
    }

    @FXML
    void ModifierAdmin(ActionEvent event) {
        // Sauvegarder les valeurs actuelles de l'administrateur
        int cinBefore = AdminToModify.getCin();
        String nomBefore = AdminToModify.getNom();
        String prenomBefore = AdminToModify.getPrenom();
        String emailBefore = AdminToModify.getEmail();
        String motDePasseBefore = AdminToModify.getMotDePasse();
        String imageBefore = AdminToModify.getImage();
        String departementBefore = AdminToModify.getDepartement();

        // Mettre à jour l'administrateur avec les nouvelles valeurs des champs texte
        boolean updateSuccess = updateAdmin();

        // Comparer les valeurs actuelles avec les nouvelles valeurs de l'administrateur
        boolean dataChanged = (cinBefore != AdminToModify.getCin()) ||
                !nomBefore.equals(AdminToModify.getNom()) ||
                !prenomBefore.equals(AdminToModify.getPrenom()) ||
                !emailBefore.equals(AdminToModify.getEmail()) ||
                !motDePasseBefore.equals(AdminToModify.getMotDePasse()) ||
                !imageBefore.equals(AdminToModify.getImage()) ||
                !departementBefore.equals(AdminToModify.getDepartement());

        // Afficher un message de succès seulement si les données ont été effectivement modifiées
        if (updateSuccess && dataChanged) {
            showAlert("Modification réussie", "L'administrateur a été modifié avec succès.");
            closeWindow(event);
        } else if (!updateSuccess) {
            // Afficher un message d'erreur si la mise à jour a échoué
            showAlert("Erreur lors de la modification", "Une erreur s'est produite lors de la modification de l'administrateur.");
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
            image_am.setText(selectedFile.getAbsolutePath());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
