package Controllers;

import Entities.Bus;
import Entities.Type;
import Service.TypeService;
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
        String typeName = txttypeName.getText().trim();
        if (typeName.isEmpty()) {
            System.out.println("Veuillez inserer un type.");
            showValidationMessage("Veuillez inserer un type.");
            return false;
        } else if (!typeName.matches("[a-zA-Z\\s]+")) {
            System.out.println("Le nom du type ne doit contenir que des caractères alphabétiques et des espaces.");
            showValidationMessage("Le nom du type ne doit contenir que des caractères alphabétiques et des espaces.\n");
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