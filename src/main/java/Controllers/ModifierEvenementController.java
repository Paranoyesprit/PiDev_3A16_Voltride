package Controllers;

import Entities.Evenement;
import Service.EvenementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

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
    private TextField placesdispoMD;

    @FXML
    void ModifierEvenement(ActionEvent event) {
        try {
            // Récupérer les valeurs des champs
            String type = TypeMD.getText();
            String adresse = AdresseMD.getText();
            LocalDate dateEvenement = DateMD.getValue();
            int placesDispo = Integer.parseInt(placesdispoMD.getText());

            // Vérifier si les valeurs saisies respectent les critères
            if (type.length() > 15) {
                showAlert("Erreur", "Le type ne doit pas dépasser 15 caractères.");
                return;
            }

            if (adresse.length() > 10) {
                showAlert("Erreur", "L'adresse ne doit pas dépasser 10 caractères.");
                return;
            }

            if (dateEvenement == null) {
                showAlert("Erreur", "Veuillez sélectionner une date.");
                return;
            }

            // Mettre à jour les données de l'événement avec les nouvelles valeurs
            EvenementToModify.setType(type);
            EvenementToModify.setAdresseEvenement(adresse);
            EvenementToModify.setDateEvenement(dateEvenement);
            EvenementToModify.setPlacesDispo(placesDispo);

            // Appeler la méthode de mise à jour dans le service de l'événement
            es.update(EvenementToModify.getId_event(), EvenementToModify);

            // Afficher une alerte pour confirmer la modification
            showAlert("Modification réussie", "L'événement a été modifié avec succès.");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le nombre de places disponibles doit être un nombre entier.");
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
        placesdispoMD.setText(String.valueOf(evenement.getPlacesDispo()));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
