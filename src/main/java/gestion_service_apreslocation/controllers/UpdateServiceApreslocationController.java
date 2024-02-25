package gestion_service_apreslocation.controllers;

import gestion_service_apreslocation.entities.Bus;
import gestion_service_apreslocation.entities.ServiceApreslocation;
import gestion_service_apreslocation.entities.Type;
import gestion_service_apreslocation.service.ServiceApreslocationServices;
import gestion_service_apreslocation.service.TypeService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        // Implement your validation logic here
        return true;
    }
}
