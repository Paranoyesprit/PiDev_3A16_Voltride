package Controllers;

import Entities.Bus;
import Entities.ServiceApreslocation;
import Entities.Type;
import Service.ServiceApreslocationServices;
import Service.TypeService;
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

        ServiceApreslocation service = new ServiceApreslocation(type, technicien, description, statut, cout);
        serviceApresLocationService.update(id, service);
        Bus.getInstance().notifyTableRefreshed();

        Stage stage = (Stage) txtid.getScene().getWindow();
        stage.close();
    }
    // nafs lahkeya mtaa controle de saisie fel ajouter serviceapreslocation
    private boolean validateInput() {

        if (typeComboBox.getValue() == null) {
            displayErrorMessage("Veuillez sélectionner un type.");
            return false;
        }


        String technicien = txttechnicien.getText().trim();
        if (!technicien.matches("[a-zA-Z]+")) {
            displayErrorMessage("Le champ technicien ne peut contenir que des lettres.");
            return false;
        }


        String description = txtdescription.getText().trim();
        if (!description.matches("[a-zA-Z\\s]+")) {
            displayErrorMessage("Le champ description ne peut contenir que des lettres et des espaces.");
            return false;
        }



        String statut = txtstatut.getText().trim();
        if (!statut.equals("disponible") && !statut.equals("bientôt disponible")) {
            displayErrorMessage("Le statut doit être 'disponible' ou 'bientôt disponible'.");
            return false;
        }


        String coutText = txtcout.getText().trim();
        if (!coutText.matches("\\d+(\\.\\d{1,2})?")) {
            displayErrorMessage("Le champ cout doit être un nombre avec au plus 2 décimales.");
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
