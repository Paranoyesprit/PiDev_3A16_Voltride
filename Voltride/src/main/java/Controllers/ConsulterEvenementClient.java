package Controllers;

import Entities.Reservation_e;
import Entities.UserSession;
import Service.ReservationEService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ConsulterEvenementClient {

    @FXML
    private TableColumn<Reservation_e, String> adresseTC;

    @FXML
    private TableView<Reservation_e> affTV;

    @FXML
    private TableColumn<Reservation_e, String> commentaireTC;

    @FXML
    private TableColumn<Reservation_e, LocalDate> dateTC;

    @FXML
    private TableColumn<Reservation_e, Integer> ideTC;

    @FXML
    private TableColumn<Reservation_e, Integer> nbrpTC;

    @FXML
    private TableColumn<Reservation_e, String> typeTC;
    @FXML
    private TableColumn<Reservation_e, Void> deleteTC;
    @FXML
    private TableColumn<Reservation_e, Void> modifyTC;

    private final ReservationEService reservationS = new ReservationEService();


    public void initialize() {
        // Initialize TableView columns
        ideTC.setCellValueFactory(new PropertyValueFactory<>("id_e"));
        nbrpTC.setCellValueFactory(new PropertyValueFactory<>("nbrPersonne"));
        commentaireTC.setCellValueFactory(new PropertyValueFactory<>("Commentaire"));
        adresseTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresseEvenement()));
        typeTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        dateTC.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateEvenement()));
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
                        Reservation_e reservationToDelete = getTableView().getItems().get(getIndex());
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
                        Reservation_e reservationToModify = getTableView().getItems().get(getIndex());
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
        List<Reservation_e> reservations = reservationS.readAllForUser(id_user);

        // Add the reservations to the TableView
        affTV.getItems().addAll(reservations);
    }

    private void deleteReservation(Reservation_e reservationToDelete) {
        // Delete the item from the database
        reservationS.delete(reservationToDelete.getId_e());

        // Remove the item from the TableView
        affTV.getItems().remove(reservationToDelete);
    }

    private void modifyReservation(Reservation_e reservationToModify) {
        try {
            // Charger l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservationE.fxml"));
            Parent root = loader.load();

            // Passer les données de la borne à modifier au contrôleur de l'interface de modification
            ModifierReservationEController modifierController = loader.getController();
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



