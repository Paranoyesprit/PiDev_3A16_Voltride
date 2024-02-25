package Controller;

import entities.borne;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.BorneService;

import javafx.event.ActionEvent;

public class ModifierBorneController {


    @FXML
    private TextField nvcapaciteTF;

    @FXML
    private DatePicker nvdate_instDP;

    @FXML
    private TextField nvempTF;

    @FXML
    private TextField nvetatTF;

    private final BorneService bs = new BorneService();
    private borne borneToModify;

    public void initData(borne borne) {
        this.borneToModify = borne;
        // Afficher les détails de la borne à modifier dans les champs de texte
        nvempTF.setText(borne.getEmplacement());
        nvcapaciteTF.setText(String.valueOf(borne.getCapacite()));
        nvetatTF.setText(borne.getEtat());
        nvdate_instDP.setValue(borne.getDate_inst());

    }
    @FXML
    void modifierBorne(ActionEvent event) {
        try {
            // Mettre à jour les données de la borne avec les nouvelles valeurs
            borneToModify.setEmplacement(nvempTF.getText());
            borneToModify.setCapacite(Integer.parseInt(nvcapaciteTF.getText()));
            borneToModify.setEtat(nvetatTF.getText());
            borneToModify.setDate_inst(nvdate_instDP.getValue());


            // Appeler la méthode de mise à jour dans le service de borne
            bs.update(borneToModify.getId(), borneToModify);
            Stage stage = (Stage) nvcapaciteTF.getScene().getWindow();
            stage.close();
            // Afficher une alerte pour confirmer la modification
            showAlert("Modification réussie", "La borne a été modifiée avec succès.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification de la borne.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
