package Controllers;

import Entities.Reservation_b;
import Entities.UserSession;
import Service.ReservationBService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ConsulterReservationClientController {

    @FXML
    private TableView<Reservation_b> affTV;

    @FXML
    private TableColumn<Reservation_b, Integer> capaciteTC;

    @FXML
    private TableColumn<Reservation_b, LocalDateTime> datedTC;

    @FXML
    private TableColumn<Reservation_b, LocalTime> heure_dTC; // Added column for "Heure début"

    @FXML
    private TableColumn<Reservation_b, LocalDateTime> datefTC;

    @FXML
    private TableColumn<Reservation_b, LocalTime> heure_fTC; // Added column for "Heure fin"

    @FXML
    private TableColumn<Reservation_b, String> emplacementTC;

    @FXML
    private TableColumn<Reservation_b, Integer> idrTC;

    @FXML
    private TableColumn<Reservation_b, Void> deleteTC;

    @FXML
    private TableColumn<Reservation_b, Void> modifyTC;

    private final ReservationBService reservationService = new ReservationBService();

    public void initialize() {
        // Initialize TableView columns
        idrTC.setCellValueFactory(new PropertyValueFactory<>("id_r"));
        datedTC.setCellValueFactory(new PropertyValueFactory<>("date_d"));
        heure_dTC.setCellValueFactory(new PropertyValueFactory<>("heure_d")); // Added column for "Heure début"
        datefTC.setCellValueFactory(new PropertyValueFactory<>("date_f"));
        heure_fTC.setCellValueFactory(new PropertyValueFactory<>("heure_f")); // Added column for "Heure fin"
        emplacementTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBorne().getEmplacement()));
        capaciteTC.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBorne().getCapacite()).asObject());
        configureDeleteColumn();
        configureModifyColumn();

        loadReservationData();
    }

    private void configureDeleteColumn() {
        deleteTC.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        Reservation_b reservationToDelete = getTableView().getItems().get(getIndex());
                        deleteReservation(reservationToDelete);
                    });
                }
            }
        });
    }

    private void configureModifyColumn() {
        modifyTC.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modify");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                    modifyButton.setOnAction(event -> {
                        Reservation_b reservationToModify = getTableView().getItems().get(getIndex());
                        modifyReservation(reservationToModify);
                    });
                }
            }
        });
    }

    private void loadReservationData() {
        // Clear existing items in the TableView
        affTV.getItems().clear();

        // Read reservations for the current user from the service
        int id_user = UserSession.getInstance().getCurrentUser().getId_u();
        List<Reservation_b> reservations = reservationService.readAllForUser(id_user);

        // Add the reservations to the TableView
        affTV.getItems().addAll(reservations);
    }

    private void deleteReservation(Reservation_b reservationToDelete) {
        // Delete the item from the database
        reservationService.delete(reservationToDelete.getId_r());

        // Remove the item from the TableView
        affTV.getItems().remove(reservationToDelete);
    }

    private void modifyReservation(Reservation_b reservationToModify) {
        try {
            // Charger l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservationB.fxml"));
            Parent root = loader.load();

            // Passer les données de la borne à modifier au contrôleur de l'interface de modification
            ModifierReservationBController modifierController = loader.getController();
            modifierController.initData(reservationToModify);

            // Afficher l'interface de modification
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Modifier Borne");
            stage.showAndWait();

            // Refresh the modified reservation in the TableView
            loadReservationData();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface de modification.");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
