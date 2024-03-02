package gestion_service_apreslocation.controllers;

import gestion_service_apreslocation.entities.Bus;
import gestion_service_apreslocation.entities.ServiceApreslocation;
import gestion_service_apreslocation.entities.Type;
import gestion_service_apreslocation.service.ServiceApreslocationServices;
import gestion_service_apreslocation.service.TypeService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class AjouterServiceApreslocationController {

    @FXML
    private TextField txtid;

    @FXML
    private ComboBox<Type> typeComboBox;

    @FXML
    private TextField txttechnicien;

    @FXML
    private TextField txtdescription;

    @FXML
    private TextField txtstatut;

    @FXML
    private TextField txtcout;


    @FXML
    private void initialize() {
        populateTypeComboBox();
    }

    @FXML
    void addservice_apreslocation(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        Type selectedType = typeComboBox.getValue();
        String typeName = selectedType.getTypeName();
        TypeService typeService = new TypeService();
        Type type = typeService.getTypeByName(typeName);
        String technicien = txttechnicien.getText();
        String description = txtdescription.getText();
        String statut = txtstatut.getText();
        double cout = Double.parseDouble(txtcout.getText());

        ServiceApreslocation p = new ServiceApreslocation(type, technicien, description, statut, cout);
        ServiceApreslocationServices ps = new ServiceApreslocationServices();
        ps.add(p);
        Bus.getInstance().notifyTableRefreshed();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void populateTypeComboBox() {
        TypeService typeService = new TypeService();
        List<Type> types = typeService.getAllTypes();
        typeComboBox.setItems(FXCollections.observableArrayList(types));
    }

    private boolean validateInput() {
        // Check if type selected wala le
        if (typeComboBox.getValue() == null) {
            displayErrorMessage("Veuillez sélectionner un type.");
            return false;
        }

        // controle de saisie technicien
        String technicien = txttechnicien.getText().trim();
        if (!technicien.matches("[a-zA-Z]+")) {
            displayErrorMessage("Le champ technicien ne peut contenir que des lettres.");
            return false;
        }

        // controle de saisie description
        String description = txtdescription.getText().trim();
        if (!description.matches("[a-zA-Z\\s]+")) {
            displayErrorMessage("Le champ description ne peut contenir que des lettres et des espaces.");
            return false;
        }


        // controle de saisie statut
        String statut = txtstatut.getText().trim();
        if (!statut.equals("disponible") && !statut.equals("bientôt disponible")) {
            displayErrorMessage("Le statut doit être 'disponible' ou 'bientôt disponible'.");
            return false;
        }

        // controle de saisie cout
        String coutText = txtcout.getText().trim();
        if (!coutText.matches("\\d+(\\.\\d{1,2})?")) {
            displayErrorMessage("Le champ cout doit être un nombre avec au plus 2 décimales.");
            return false;
        }

        return true;
    }

//message d erreur
    private void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
