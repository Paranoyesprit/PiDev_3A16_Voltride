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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class AjouterServiceApreslocationController {

    @FXML
    private TextField txtid;

    @FXML
    private ComboBox <Type> typeComboBox;

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
    @FXML
    private void initialize(){
        populateTypeComboBox();
    }
    @FXML
    void addservice_apreslocation(ActionEvent event) {
        Type selectedType = typeComboBox.getValue();
        String typeName = selectedType.getTypeName();
        TypeService typeService = new TypeService();
        Type type = typeService.getTypeByName(typeName);
        String technicien = txttechnicien.getText();
        String description = txtdescription.getText();
        String statut = txtstatut.getText();
        double cout = Double.parseDouble(txtcout.getText());
        int id_client = Integer.parseInt(txtid_client.getText());
        int id_voiture = Integer.parseInt(txtid_voiture.getText());

        ServiceApreslocation p = new ServiceApreslocation(type, technicien, description, statut, cout, id_client, id_voiture);
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
}
