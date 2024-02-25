package Controller;

import   Entities.Voiture;
import Service.VoitureService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class afficherReservationController {

    @FXML
    private TableColumn<Voiture, String> colEtatlRes;

    @FXML
    private TableColumn<Voiture, Float> colKilometrageRes;

    @FXML
    private TableColumn<Voiture, String> colMarqueRes;

    @FXML
    private TableColumn<Voiture, String> colModelRes;

    @FXML
    private TableColumn<Voiture, Float> colPrixRes;

    @FXML
    private TableView<Voiture> listReservation;

    @FXML
    private TableColumn<Voiture, Void> colReserver;

    private final VoitureService vs = new VoitureService();

    @FXML
    public void initialize() {
        colMarqueRes.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModelRes.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colEtatlRes.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colPrixRes.setCellValueFactory(new PropertyValueFactory<>("prix_location"));
        colKilometrageRes.setCellValueFactory(new PropertyValueFactory<>("kilometrage"));
        configureReserverColumn();
        loadVoitureData();
    }

    private void configureReserverColumn() {
        colReserver.setCellFactory(param -> new TableCell<>() {
            private final Button reserverButton = new Button("Réserver");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(reserverButton);
                    reserverButton.setOnAction(event -> {
                        Voiture voitureToReserver = getTableView().getItems().get(getIndex());
                        reserverVoiture(voitureToReserver);
                    });
                }
            }
        });
    }
    void loadVoitureData() {
        List<Voiture> voitures = vs.readAvailableVoitures();
        if (listReservation != null) {
            listReservation.getItems().addAll(voitures);
        } else {
            System.err.println("TableView listReservation is null.");
        }
    }
    private void reserverVoiture(Voiture voitureToReserver) {
        // Utilisez la voiture récupérée ici
        // Par exemple, vous pouvez l'envoyer à la vue d'ajout de réservation ou effectuer d'autres actions nécessaires
        System.out.println("Voiture réservée : " + voitureToReserver);
        // Vous pouvez également appeler une fonction pour passer la voiture à la vue d'ajout de réservation
        openReservationView(voitureToReserver);
    }

    private void openReservationView(Voiture voitureToReserver) {
        try {
            // Charger l'interface d'ajout de réservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterReservation.fxml"));
            Parent root = loader.load();

            // Accéder au contrôleur de l'interface d'ajout de réservation
            ajouterReservationController controller = loader.getController();

            // Appeler une méthode pour passer la voiture au contrôleur de l'interface d'ajout de réservation
            controller.setReservedCar(voitureToReserver);

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
