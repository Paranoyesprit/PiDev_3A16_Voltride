package Controllers;

import Entities.Bus;
import Entities.Type;
import Service.TypeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class UpdateTypeController {

    @FXML
    private TextField txtTypeName;

    private TypeService typeService;
    private Type type;
    private int typeId; // hold lel ID

    public void initData(int typeId) {
        this.typeId = typeId; // initialisition mta3 el ID
        typeService = new TypeService();

        Type type = typeService.readById(typeId);
        txtTypeName.setText(type.getTypeName());
    }

    @FXML
    void updateType(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        String typeName = txtTypeName.getText();

        // staamalna el ID lel update
        Type updatedType = new Type(typeName);
        updatedType.setId(this.typeId); // Set the internal ID

        typeService.update(this.typeId, updatedType); // Pass the internal ID and updated Type object to the update method
        Bus.getInstance().notifyTableRefreshed();
        Stage stage = (Stage) txtTypeName.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        String typeName = txtTypeName.getText().trim();
        if (typeName.isEmpty()) {
            displayErrorMessage("Le champ type ne peut pas être vide.");
            return false;
        }

        if (!typeName.matches("[a-zA-Z\\s]+")) {
            displayErrorMessage("Le champ type ne peut contenir que des lettres et des espaces.");
            return false;
        }

        return true;
    }


    private void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
