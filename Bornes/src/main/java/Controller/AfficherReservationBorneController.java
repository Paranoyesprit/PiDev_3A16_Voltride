package Controller;

import entities.borne;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.BorneService;

import java.io.IOException;
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
    private final BorneService bs = new BorneService();



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
        if (resaffTV != null) {
            resaffTV.getItems().addAll(bornes);
        } else {
            System.err.println("Aucune borne n'est disponible.");
        }
    }

    private void configureReserverColumn() {
        reserverTC.setCellFactory(param -> new TableCell<>() {
            private final Button reserverButton = new Button("Réserver");

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
        // Utilisez la borne récupérée ici
        // Par exemple, vous pouvez l'envoyer à la vue d'ajout de réservation ou effectuer d'autres actions nécessaires
        System.out.println("borne réservée : " + borneToReserver);
        // Vous pouvez également appeler une fonction pour passer la voiture à la vue d'ajout de réservation
        openReservationView(borneToReserver);
    }
    private void openReservationView(borne borneToReserver) {
        try {
            // Charger l'interface d'ajout de réservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservationBorne.fxml"));
            Parent root = loader.load();

            // Accéder au contrôleur de l'interface d'ajout de réservation
              AjouterReservationBorneController controller= loader.getController();

            // Appeler une méthode pour passer la voiture au contrôleur de l'interface d'ajout de réservation
            controller.setReservedBorne(borneToReserver);

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
