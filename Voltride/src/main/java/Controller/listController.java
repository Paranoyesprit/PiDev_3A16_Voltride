package Controller;

import Entities.Voiture;
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
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class listController {

    @FXML
    private TableColumn<Voiture, Void> colDelete;

    @FXML
    private TableColumn<Voiture, Void> colModifier;

    @FXML
    private TableColumn<Voiture, String> colEtat;

    @FXML
    private TableColumn<Voiture, Float> colKillometrage;

    @FXML
    private TableColumn<Voiture, String> colMarque;

    @FXML
    private TableColumn<Voiture, String> colModele;

    @FXML
    private TableColumn<Voiture, Float> colPrix;

    @FXML
    private TableColumn<Voiture, Integer> colId;

    @FXML
    private TableView<Voiture> tableview;

    @FXML
    private TextField id_marque;

    @FXML
    private TextField id_modele;

    @FXML
    private TextField id_etat;

    @FXML
    private TextField id_kilometrage;

    @FXML
    private TextField id_prix;

    private final VoitureService vs = new VoitureService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<Voiture, Integer>("id_v"));
        colMarque.setCellValueFactory(new PropertyValueFactory<Voiture, String>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<Voiture, String>("modele"));
        colEtat.setCellValueFactory(new PropertyValueFactory<Voiture, String>("etat"));
        colPrix.setCellValueFactory(new PropertyValueFactory<Voiture, Float>("prix_location"));
        colKillometrage.setCellValueFactory(new PropertyValueFactory<Voiture, Float>("kilometrage"));
        configureDeleteColumn();
        configureModifierColumn();
        loadVoitureData();
    }

    private void configureDeleteColumn() {
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        Voiture voitureToDelete = getTableView().getItems().get(getIndex());
                        deleteVoiture(voitureToDelete);
                    });
                }
            }
        });
    }

    private void configureModifierColumn() {
        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modifier");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                    modifyButton.setOnAction(event -> {
                        Voiture voitureToModify = getTableView().getItems().get(getIndex());
                        modifyVoiture(voitureToModify);
                    });
                }
            }
        });
    }

    void loadVoitureData() {
        List<Voiture> voitures = vs.readAll();
        tableview.getItems().addAll(voitures);
    }

    private void deleteVoiture(Voiture voitureToDelete) {
        vs.delete(voitureToDelete.getId_v());
        tableview.getItems().remove(voitureToDelete);
    }

    private void modifyVoiture(Voiture voitureToModify) {
        try {
            // Charger l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierVoiture.fxml"));
            Parent root = loader.load();

            // Passer les données de la voiture à modifier au contrôleur de l'interface de modification
            modifierVoitureController modifierController = loader.getController();
            modifierController.initData(voitureToModify);

            // Afficher l'interface de modification
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Modifier Voiture");
            stage.show();
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

    @FXML
    private void ajouter() {
        try {
            // Charger l'interface d'ajout de voiture
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterVoiture.fxml"));
            Parent root = loader.load();

            // Afficher l'interface d'ajout de voiture dans une nouvelle fenêtre
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter Voiture");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface d'ajout de voiture.");
        }
    }

}
