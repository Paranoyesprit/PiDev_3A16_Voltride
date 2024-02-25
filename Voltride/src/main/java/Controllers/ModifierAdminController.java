package Controllers;

import Entities.Admin;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

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

    public void initData(Admin admin) {
        this.AdminToModify = admin;
        nom_am.setText(admin.getNom());
        prenom_am.setText(admin.getPrenom());
        cin_am.setText(String.valueOf(admin.getCin()));
        email_am.setText(admin.getEmail());
        mp_am.setText(admin.getMotDePasse());
        image_am.setText(admin.getImage());

        String departement = admin.getDepartement();
        depm.setValue(departement);
    }

    @FXML
    void ModifierAdmin(ActionEvent event) {
        try {
            int cinm = Integer.parseInt(cin_am.getText());
            AdminToModify.setCin(cinm);
            AdminToModify.setNom(nom_am.getText());
            AdminToModify.setPrenom(prenom_am.getText());
            AdminToModify.setEmail(email_am.getText());
            AdminToModify.setMotDePasse(mp_am.getText());
            AdminToModify.setImage(image_am.getText());
            String departementm = depm.getValue();
            AdminToModify.setDepartement(departementm);

            // Mettre à jour les autres champs de l'administrateur avant d'appeler updateAdmin
            us.updateAdmin(AdminToModify.getId_u(), AdminToModify);

        } catch (Exception e) {
            e.printStackTrace();        }
    }

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

}
