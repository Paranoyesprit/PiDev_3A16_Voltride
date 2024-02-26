package gestion_service_apreslocation.controllers;

import gestion_service_apreslocation.entities.Bus;
import gestion_service_apreslocation.entities.ServiceApreslocation;
import gestion_service_apreslocation.entities.Type;
import gestion_service_apreslocation.service.ServiceApreslocationServices;
import gestion_service_apreslocation.service.TypeService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class UpdateServiceApreslocationController {

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
    private TextField txtid_client;

    @FXML
    private TextField txtid_voiture;


    private ServiceApreslocationServices serviceApresLocationService;

    public void initData(int serviceId) {
        serviceApresLocationService = new ServiceApreslocationServices();

        ServiceApreslocation service = serviceApresLocationService.readById(serviceId);
        txtid.setText(String.valueOf(serviceId));
        Type eventType = service.getType();
        if (eventType != null) {
            TypeService typeService = new TypeService();
            List<Type> types = typeService.readAll();
            typeComboBox.setItems(FXCollections.observableArrayList(types));

            Type selectedType = types.stream()
                    .filter(type -> type.getId() == eventType.getId())
                    .findFirst()
                    .orElse(null);

            typeComboBox.setValue(selectedType);
        }

        txttechnicien.setText(service.getTechnicien());
        txtdescription.setText(service.getDescription());
        txtstatut.setText(service.getStatut());
        txtcout.setText(String.valueOf(service.getCout()));
        txtid_client.setText(String.valueOf(service.getId_client()));
        txtid_voiture.setText(String.valueOf(service.getId_voiture()));
    }

    @FXML
    void updateService(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        int id = Integer.parseInt(txtid.getText());
        Type type = typeComboBox.getValue();
        String technicien = txttechnicien.getText();
        String description = txtdescription.getText();
        String statut = txtstatut.getText();
        double cout = Double.parseDouble(txtcout.getText());
        int idClient = Integer.parseInt(txtid_client.getText());
        int idVoiture = Integer.parseInt(txtid_voiture.getText());

        ServiceApreslocation service = new ServiceApreslocation(type, technicien, description, statut, cout, idClient, idVoiture);
        serviceApresLocationService.update(id, service);
        Bus.getInstance().notifyTableRefreshed();

        Stage stage = (Stage) txtid.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        // Check if type is selected
        if (typeComboBox.getValue() == null) {
            displayErrorMessage("Veuillez sélectionner un type.");
            return false;
        }

        // Check if technicien contains only letters
        String technicien = txttechnicien.getText().trim();
        if (!technicien.matches("[a-zA-Z]+")) {
            displayErrorMessage("Le champ technicien ne peut contenir que des lettres.");
            return false;
        }

        // Check if description contains only letters
        String description = txtdescription.getText().trim();
        if (!description.matches("[a-zA-Z]+")) {
            displayErrorMessage("Le champ description ne peut contenir que des lettres.");
            return false;
        }

        // Check if statut is either "disponible" or "bientôt disponible"
        String statut = txtstatut.getText().trim();
        if (!statut.equals("disponible") && !statut.equals("bientôt disponible")) {
            displayErrorMessage("Le statut doit être 'disponible' ou 'bientôt disponible'.");
            return false;
        }

        // Check if cout is a valid double with up to 2 decimal places
        String coutText = txtcout.getText().trim();
        if (!coutText.matches("\\d+(\\.\\d{1,2})?")) {
            displayErrorMessage("Le champ cout doit être un nombre avec au plus 2 décimales.");
            return false;
        }

        // Check if id_client is "1"
        String idClient = txtid_client.getText().trim();
        if (!idClient.equals("1")) {
            displayErrorMessage("L'ID client doit être '1'.");
            return false;
        }

        // Check if id_voiture is "1"
        String idVoiture = txtid_voiture.getText().trim();
        if (!idVoiture.equals("1")) {
            displayErrorMessage("L'ID voiture doit être '1'.");
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
