package Controllers;

import Entities.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Service.EvenementService;

import java.io.IOException;
import java.util.List;

public class AfficherReservation {

    @FXML
    private TableColumn<Evenement, String> DateR;

    @FXML
    private TableColumn<Evenement, Void> Reserver;

    @FXML
    private TableColumn<Evenement, String> adresseR;

    @FXML
    private TableColumn<Evenement, String> typeR;
    @FXML
    private TableView<Evenement> listreserv;
    private final EvenementService es =new EvenementService();
    @FXML
    public void initialize() {
        typeR.setCellValueFactory(new PropertyValueFactory<>("type"));
        adresseR.setCellValueFactory(new PropertyValueFactory<>("adresseEvenement"));
        DateR.setCellValueFactory(new PropertyValueFactory<>("dateEvenement"));

        configureReserverColumn();
        loadEvenementData();
    }

    private void configureReserverColumn() {
        Reserver.setCellFactory(param -> new TableCell<>() {
            private final Button reserverButton = new Button("Réserver");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(reserverButton);
                    reserverButton.setOnAction(event -> {
                        Evenement evenementToReserver = getTableView().getItems().get(getIndex());
                        reserverEvenement(evenementToReserver);
                    });
                }
            }
        });
    }
    void loadEvenementData() {
       List<Evenement> evenements= es.readAll();
       listreserv.getItems().addAll(evenements);
    }
    private void reserverEvenement(Evenement evenementToReserver) {
        // Utilisez la voiture récupérée ici
        // Par exemple, vous pouvez l'envoyer à la vue d'ajout de réservation ou effectuer d'autres actions nécessaires
        System.out.println("Evenement réservée : " + evenementToReserver);
        // Vous pouvez également appeler une fonction pour passer la voiture à la vue d'ajout de réservation
        openReservationView(evenementToReserver);
    }

    private void openReservationView(Evenement evenementToReserver) {
        try {
            // Charger l'interface d'ajout de réservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservationEvent.fxml"));
            Parent root = loader.load();

            // Accéder au contrôleur de l'interface d'ajout de réservation
            AjouterReservationEventController controller = loader.getController();

            // Appeler une méthode pour passer la voiture au contrôleur de l'interface d'ajout de réservation
            controller.setreservedEvent(evenementToReserver);

            // Afficher l'interface d'ajout de réservation dans une nouvelle fenêtre
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter Réservation");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface d'ajout de réservation.");
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
