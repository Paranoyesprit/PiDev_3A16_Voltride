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

public class AfficherBorneController {

    @FXML
    private TableView<borne> affTV;

    @FXML
    private TableColumn<borne, Integer> idbTC;

    @FXML
    private TableColumn<borne, String> emplacementTC;

    @FXML
    private TableColumn<borne, Integer> capaciteTC;

    @FXML
    private TableColumn<borne, String> etatTC;

    @FXML
    private TableColumn<borne, String> dateinstTC;

    @FXML
    private TableColumn<borne, Void> deleteTC;

    @FXML
    private TableColumn<borne, Void> modifyTC;


    private final BorneService borneService = new BorneService();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        idbTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        emplacementTC.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
        capaciteTC.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        etatTC.setCellValueFactory(new PropertyValueFactory<>("etat"));
        dateinstTC.setCellValueFactory(new PropertyValueFactory<>("date_inst"));
        configureDeleteColumn();
        configureModifyColumn();
        loadBorneData();
    }
    private void configureDeleteColumn() {
        // Set up the delete button column
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
                        borne borneToDelete = getTableView().getItems().get(getIndex());
                        deleteBorne(borneToDelete);
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
                        borne borneToModify = getTableView().getItems().get(getIndex());
                        modifyBorne(borneToModify);
                    });
                }
            }
        });
    }

    private void loadBorneData() {
        // Read all bornes from the service
        List<borne> bornes = borneService.readAll();

        // Add the bornes to the TableView
        affTV.getItems().addAll(bornes);
    }

    private void deleteBorne(borne borneToDelete) {
        // Delete the item from the database
        borneService.delete(borneToDelete.getId());

        // Remove the item from the TableView
        affTV.getItems().remove(borneToDelete);
    }
    private void modifyBorne(borne borneToModify) {
        try {
            // Charger l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierBorne.fxml"));
            Parent root = loader.load();

            // Passer les données de la borne à modifier au contrôleur de l'interface de modification
            ModifierBorneController modifierController = loader.getController();
            modifierController.initData(borneToModify);

            // Afficher l'interface de modification
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Modifier Borne");
            stage.showAndWait();
            loadBorneData();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface de modification.");
        }
    }
    @FXML
    private void ajouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterBorne.fxml"));
            Parent root = loader.load();



            // Afficher l'interface de l'ajout
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter Borne");
            stage.showAndWait();
            loadBorneData();

        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface de l'ajout.");
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
