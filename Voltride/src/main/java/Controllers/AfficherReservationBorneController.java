package Controllers;

import Entities.Voiture;
import Entities.borne;
import Service.ReservationBService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Service.BorneService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AfficherReservationBorneController {

    @FXML
    private TableColumn<borne, String> resdateinstTC;

    @FXML
    private TableView<borne> resaffTV;

    @FXML
    private TableColumn<borne, Integer> rescapTC;

    @FXML
    private TableColumn<borne, String> resempTC;

    @FXML
    private TableColumn<borne, Void> reserverTC;

    @FXML
    private TableColumn<borne, String> resetatTC;

    @FXML
    private DatePicker dateRecherche;
    private final BorneService bs = new BorneService();
    private final ReservationBService rbs= new ReservationBService();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        resempTC.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
        rescapTC.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        resetatTC.setCellValueFactory(new PropertyValueFactory<>("etat"));
        resdateinstTC.setCellValueFactory(new PropertyValueFactory<>("date_inst"));
        configureReserverColumn();
        loadBorneData();
    }

    @FXML
    void loadBorneData() {
        List<borne> bornes = bs.readAvailableBornes();
        resaffTV.getItems().setAll(bornes);
    }

    private void configureReserverColumn() {
        reserverTC.setCellFactory(param -> new TableCell<>() {
            private final Button reserverButton = new Button("Réserver");
            {
                // Définir la couleur du bouton
                reserverButton.setStyle("-fx-background-color: #19192AFF; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(reserverButton);
                    reserverButton.setOnAction(event -> {
                        borne borneToReserver = getTableView().getItems().get(getIndex());
                        reserverBorne(borneToReserver);
                    });
                }
            }
        });
    }

    private void reserverBorne(borne borneToReserver) {
        openReservationView(borneToReserver);
    }

    private void openReservationView(borne borneToReserver) {
        try {
            // Charger l'interface d'ajout de réservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservationBorne.fxml"));
            Parent root = loader.load();

            // Accéder au contrôleur de l'interface d'ajout de réservation
            AjouterReservationBorneController controller = loader.getController();

            // Appeler une méthode pour passer la voiture au contrôleur de l'interface d'ajout de réservation
            controller.setReservedBorne(borneToReserver);

            // Afficher l'interface d'ajout de réservation dans une nouvelle fenêtre
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter Réservation");
            stage.setOnHiding(e -> handleWindowClosed()); // Set event handler for window close
            stage.show();
        }catch (IOException e) {
        e.printStackTrace(); // Add this line to print the exception stack trace
        showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface d'ajout de réservation.");
    }

}

    private void handleWindowClosed() {
        // This method is called when the AjouterReservationBorne.fxml window is closed
        // Refresh the data in the TableView
        loadBorneData();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void consulterReservation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsulterReservationClient.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Consulter Mes Réservations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    @FXML
    private void buttonChercher(ActionEvent event) {
        LocalDate selectedDate = dateRecherche.getValue();

        // Clear the current contents of the TableView
        resaffTV.getItems().clear();

        // Update the TableView with the available cars for the selected date
        List<borne> availableBornes = rbs.getAvailableBornes(selectedDate, selectedDate);
        resaffTV.getItems().addAll(availableBornes);
    }

}
