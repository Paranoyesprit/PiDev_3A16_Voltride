package gestion_service_apreslocation.controllers;

import gestion_service_apreslocation.entities.Bus;
import gestion_service_apreslocation.entities.Type;
import gestion_service_apreslocation.service.TypeService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AjouterTypeController {

    @FXML
    private TextField txttypeName;

    @FXML
    private Label validationLabel;

    @FXML
    void addType(ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        String typeName = txttypeName.getText();

        Type type = new Type(typeName);

        TypeService typeService = new TypeService();
        typeService.add(type);
        Bus.getInstance().notifyTableRefreshed();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        if (txttypeName.getText() == null || txttypeName.getText().isEmpty()) {
            System.out.println("Please insert a type.");
            showValidationMessage("Please insert a type.");
            return false;
        } else {
            hideValidationMessage();
        }
        return true;
    }

    private void showValidationMessage(String message) {
        validationLabel.setText(message);
        validationLabel.setStyle("-fx-text-fill: red;");
        fadeInValidationMessage();
    }

    private void hideValidationMessage() {
        validationLabel.setText("");
    }

    private void fadeInValidationMessage() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(validationLabel.opacityProperty(), 1)));
        timeline.play();
    }

    private void fadeOutValidationMessage() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(validationLabel.opacityProperty(), 0)));
        timeline.setOnFinished(event -> hideValidationMessage());
        timeline.play();
    }
}