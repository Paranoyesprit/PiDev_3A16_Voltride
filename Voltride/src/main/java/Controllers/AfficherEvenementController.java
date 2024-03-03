package Controllers;

import Entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Service.EvenementService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AfficherEvenementController {

    @FXML
    private TableColumn<Evenement, String> AdresseTF;

    @FXML
    private TableColumn<Evenement, LocalDate> DateTF; // Modification du type de colonne

    @FXML
    private TableColumn<Evenement, String> TypeTF;

    @FXML
    private TableColumn<Evenement, Integer> ideventTF;

    @FXML
    private TableView<Evenement> affEV;
    @FXML
    private TableColumn<Evenement, Void> deleteTF;

    @FXML
    private TableColumn<Evenement, Void> modifyTF;
    @FXML
    private TableColumn<Evenement, Integer> placedispoTF;

    private final EvenementService evenementService = new EvenementService() {

    };


    @FXML
    public void initialize() {
        // Initialize TableView columns
        ideventTF.setCellValueFactory(new PropertyValueFactory<>("id_event"));
        TypeTF.setCellValueFactory(new PropertyValueFactory<>("type"));
        AdresseTF.setCellValueFactory(new PropertyValueFactory<>("adresseEvenement"));
        DateTF.setCellValueFactory(new PropertyValueFactory<>("dateEvenement"));
        placedispoTF.setCellValueFactory(new PropertyValueFactory<>("placesDispo")); // Ajout de la nouvelle colonne
        configureDeleteColumn();
        configureModifyColumn();

        loadEvenementData();
    }

    private void configureDeleteColumn() {
        // Set up the delete button column
        deleteTF.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        Evenement evenementToDelete = getTableView().getItems().get(getIndex());
                        deleteEvenement(evenementToDelete);
                    });
                }
            }
        });
    }

    private void configureModifyColumn() {

        modifyTF.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modifier");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                    modifyButton.setOnAction(event -> {
                        Evenement EvenementToModify = getTableView().getItems().get(getIndex());
                        modifyEvenement(EvenementToModify);
                    });
                }
            }
        });
    }

    private void loadEvenementData() {
        List<Evenement> evenements = evenementService.readAll();
        affEV.getItems().addAll(evenements);
    }

    private void deleteEvenement(Evenement evenementToDelete) {
        // Delete the item from the database
        evenementService.delete(evenementToDelete.getId_event());

        // Remove the item from the TableView
        affEV.getItems().remove(evenementToDelete);
    }

    private void modifyEvenement(Evenement EvenementToModify) {
        try {
            // Charger l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvenement.fxml"));
            Parent root = loader.load();

            // Passer les données de l'événement à modifier au contrôleur de l'interface de modification
            Controllers.ModifierEvenementController modifierController = loader.getController();
            modifierController.initData(EvenementToModify);

            // Afficher l'interface de modification
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Modifier Evenement");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface de modification.");
        }
    }

    @FXML
    void ajouter(ActionEvent event) {

        loadFXML("/AjouterEvenement.fxml");

    }

    private void showAlert(String erreur, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(erreur);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement du fichier FXML
        }
    }
}
