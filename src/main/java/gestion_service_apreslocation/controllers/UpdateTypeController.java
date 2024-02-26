package gestion_service_apreslocation.controllers;

import gestion_service_apreslocation.entities.Bus;
import gestion_service_apreslocation.entities.Type;
import gestion_service_apreslocation.service.TypeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateTypeController {

    @FXML
    private TextField txtTypeName;

    private TypeService typeService;
    private Type type;
    private int typeId; // This will hold the ID value internally

    public void initData(int typeId) {
        this.typeId = typeId; // Initialize the internal ID
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

        // Use the internal ID for updating
        Type updatedType = new Type(typeName);
        updatedType.setId(this.typeId); // Set the internal ID

        typeService.update(this.typeId, updatedType); // Pass the internal ID and updated Type object to the update method
        Bus.getInstance().notifyTableRefreshed();
        Stage stage = (Stage) txtTypeName.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        // Implement your validation logic here
        return true;
    }
}

