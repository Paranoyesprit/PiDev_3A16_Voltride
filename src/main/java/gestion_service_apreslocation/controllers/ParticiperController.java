package gestion_service_apreslocation.controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import gestion_service_apreslocation.QRCodeGenerator;
import gestion_service_apreslocation.entities.ServiceApreslocation;
import gestion_service_apreslocation.service.ParticipationServices;
import gestion_service_apreslocation.service.ServiceApreslocationServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class ParticiperController implements Initializable {

    @FXML
    private VBox eventContainer;
    private int selectedServiceId;

    private ServiceApreslocationServices serviceApreslocationServices;
    private static final String ACCOUNT_SID = "AC90a46e0afc2a91fbb3cd8529aef44a5d";
    private static final String AUTH_TOKEN = "24f7b454b31d9a0c8b9829d8c562e8b8";
    private static final String TWILIO_NUMBER = "+13158186430";
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    private static final int ITEMS_PER_PAGE = 6; // Number of items per page (2 rows of 3 services)
    private Pagination pagination;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceApreslocationServices = new ServiceApreslocationServices();
        List<ServiceApreslocation> services = serviceApreslocationServices.readAll();

        // Calculate the number of pages based on the number of services and items per page
        int pageCount = (int) Math.ceil((double) services.size() / ITEMS_PER_PAGE);

        // Create pagination control with calculated page count
        pagination = new Pagination(pageCount, 0);
        pagination.setPageFactory(this::createPage);

        eventContainer.getChildren().add(pagination);
    }

    private VBox createPage(int pageIndex) {
        VBox pageBox = new VBox(5);

        // Get sublist of services for the current page
        List<ServiceApreslocation> services = serviceApreslocationServices.readAll();
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, services.size());
        List<ServiceApreslocation> sublist = services.subList(fromIndex, toIndex);

        // Divide sublist into two rows
        List<List<ServiceApreslocation>> rows = new ArrayList<>();
        for (int i = 0; i < sublist.size(); i += 3) {
            rows.add(sublist.subList(i, Math.min(i + 3, sublist.size())));
        }

        // Create and add service boxes for each row
        for (List<ServiceApreslocation> row : rows) {
            HBox rowBox = new HBox(5);
            for (ServiceApreslocation service : row) {
                VBox serviceBox = new VBox(5);
                serviceBox.getStyleClass().add("service-box");

                // Create labels and buttons for service details
                Label serviceNameLabel = new Label(service.getType().getTypeName());
                serviceNameLabel.getStyleClass().addAll("label-white", "service-name"); // Apply the label-white and service-name styles
                Label technicianLabel = new Label("Technicien/Personne en charge: " + service.getTechnicien());
                technicianLabel.getStyleClass().add("label-white"); // Apply the label-white style
                Label statusLabel = new Label("Status: " + service.getStatut());
                statusLabel.getStyleClass().add("label-white"); // Apply the label-white style
                Label costLabel = new Label("Prix: " + service.getCout() + " DT");
                costLabel.getStyleClass().add("label-white"); // Apply the label-white style


                Button participerButton = new Button("Résérver");
                participerButton.setOnAction(event -> {
                    if ("bientôt disponible".equals(service.getStatut())) {
                        showAlert("Service pas disponible", "Ce service n'est pas encore disponible.");
                        return; // Exit the event handler if the service is not available
                    }

                    // Proceed with the participation confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Participation");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to participate in this service?");
                    ParticipationServices participationServices = new ParticipationServices();
                    int selectedServiceId = participationServices.getNumberOfParticipants(service.getIdservice()) + 1;

                    // Show the confirmation dialog and wait for user response
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // User clicked OK, proceed with adding participation
                            participationServices.addParticipation(service.getIdservice());

                            // Generate QR code
                            String qrData = "You are the " + (participationServices.getNumberOfParticipants(service.getIdservice())) + " participant";
                            ImageView qrCodeImageView = QRCodeGenerator.generateQRCodeImage(qrData, 200, 200);

                            // Show QR code in a new stage
                            Stage qrCodeStage = new Stage();
                            qrCodeStage.initModality(Modality.APPLICATION_MODAL);
                            qrCodeStage.setTitle("QR Code");
                            VBox qrCodeBox = new VBox(10);
                            qrCodeBox.setPadding(new Insets(20));
                            qrCodeBox.getChildren().add(qrCodeImageView);
                            Scene qrCodeScene = new Scene(qrCodeBox, 300, 300);
                            qrCodeStage.setScene(qrCodeScene);
                            qrCodeStage.show();
                        }
                    });
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Enter Number");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Please enter your number:");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(number -> {
                        // Send the message using the messaging API
                        sendMessage(number, selectedServiceId);
                    });
                });


                Button detailsButton = new Button("Mes details personnel");
                detailsButton.setOnAction(event -> {
                    showServiceDetailsPopup(service);
                });

                HBox buttonBox = new HBox(5);
                buttonBox.getChildren().addAll(participerButton, detailsButton);

                serviceBox.getChildren().addAll(serviceNameLabel, technicianLabel, statusLabel, costLabel, buttonBox);
                rowBox.getChildren().add(serviceBox);
            }
            pageBox.getChildren().add(rowBox);
        }

        return pageBox;
    }


    private void showServiceDetailsPopup(ServiceApreslocation service) {
        Stage detailsStage = new Stage();
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.setTitle("Service Details");

        VBox detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(20));

        Label serviceNameLabel = new Label(service.getType().getTypeName());
        serviceNameLabel.setStyle("-fx-font-size: 24px;");

        // Retrieve the number of participants for the service directly
        ParticipationServices participationServices = new ParticipationServices();
        int numberOfParticipants = participationServices.getNumberOfParticipants(service.getIdservice());
        Label nbrDeParticipantLabel = new Label("Vous êtes le client numéro : " + numberOfParticipants + " dans notre liste");

        detailsBox.getChildren().addAll(serviceNameLabel, nbrDeParticipantLabel);

        Scene detailsScene = new Scene(detailsBox, 300, 200);
        detailsStage.setScene(detailsScene);
        detailsStage.show();
    }

    private void sendMessage(String number, int selectedServiceId) {
        System.out.println(selectedServiceId);
        if (!isValidPhoneNumber(number)) {
            showAlert("Invalid Phone Number", "Please enter a valid phone number.");
            return;
        }

        Message message = Message.creator(
                new PhoneNumber(number),
                new PhoneNumber(TWILIO_NUMBER),
                "You are the " + selectedServiceId + " participant"
        ).create();

        showAlert("Message Sent", "Message sent to " + number);
    }

    private boolean isValidPhoneNumber(String number) {
        // Check if the number is not null and matches the E.164 format
        return number != null && number.matches("^\\+[1-9]\\d{1,14}$");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}