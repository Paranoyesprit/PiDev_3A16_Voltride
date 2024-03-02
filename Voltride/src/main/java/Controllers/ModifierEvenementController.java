package Controllers;

import Entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import Service.EvenementService;

import java.time.LocalDate;

public class ModifierEvenementController {
    private final EvenementService es = new EvenementService();
    private Evenement EvenementToModify;

    @FXML
    private TextField AdresseMD;

    @FXML
    private DatePicker DateMD;

    @FXML
    private TextField TypeMD;

    @FXML
    void ModifierEvenement(ActionEvent event) {
        try {
            // Mettre à jour les données de l'événement avec les nouvelles valeurs
            EvenementToModify.setType(TypeMD.getText());
            EvenementToModify.setAdresseEvenement(AdresseMD.getText());
            EvenementToModify.setDateEvenement(DateMD.getValue());

            // Appeler la méthode de mise à jour dans le service de l'événement
            es.update(EvenementToModify.getId_event(), EvenementToModify);

            // Afficher une alerte pour confirmer la modification
            showAlert("Modification réussie", "L'événement a été modifié avec succès.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification de l'événement.");
        }
    }

    public void initData(Evenement evenement) {
        this.EvenementToModify = evenement;
        // Afficher les détails de l'événement à modifier dans les champs de texte
        TypeMD.setText(evenement.getType());
        AdresseMD.setText(evenement.getAdresseEvenement());
        DateMD.setValue(evenement.getDateEvenement());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
