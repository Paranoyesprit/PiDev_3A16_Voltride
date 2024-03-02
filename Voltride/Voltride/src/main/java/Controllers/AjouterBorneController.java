package Controllers;

import Entities.borne;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Service.BorneService;

import java.io.IOException;


public class AjouterBorneController {

    private final BorneService bs = new BorneService();

    @FXML
    private TextField capaciteTF;

    @FXML
    private DatePicker date_instDP;

    @FXML
    private TextField emplacementTF;

    @FXML
    private TextField etatTF;

    @FXML
    public void initialize() {
        // Add focus property listeners to the TextFields
        addFocusListener(capaciteTF);
        addFocusListener(emplacementTF);
        addFocusListener(etatTF);
    }


    @FXML
    void ajouter(ActionEvent event) {
        // Check if any of the required fields is empty
        if (isEmptyField(capaciteTF) || isEmptyField(emplacementTF) || isEmptyField(etatTF) || date_instDP.getValue() == null) {
            showAlert("Invalid Input", "Please fill in all the required fields.");
            return;
        }
        try {
            int capacite = Integer.parseInt(capaciteTF.getText());
            bs.addpst(new borne(emplacementTF.getText(), capacite, etatTF.getText(), date_instDP.getValue()));

            // Show success message
            showAlert("Success", "Borne added successfully.");


            // Close the current AjouterBorne window
            Stage stage = (Stage) capaciteTF.getScene().getWindow();
            stage.close();
            // Open AfficherBorne window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherBorne.fxml"));
            Parent root = loader.load();


        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid data.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isEmptyField(TextField textField) {
        if (textField.getText().trim().isEmpty()) {
            textField.setStyle("-fx-border-color: red;");
            textField.setPromptText("Field is required");
            return true;
        } else {
            textField.setStyle(""); // Remove red border
            textField.setPromptText(""); // Remove prompt text
            return false;
        }
    }

    private void addFocusListener(TextField textField) {
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) { // Focus lost
                    isEmptyField(textField);
                }
            }
        });
    }
}
