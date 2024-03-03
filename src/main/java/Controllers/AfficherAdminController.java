package Controllers;

import Entities.Admin;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AfficherAdminController {

    @FXML
    private TableView<Admin> affichage_u;

    @FXML
    private TableColumn<Admin, Integer> cin_u;

    @FXML
    private TableColumn<Admin, Void> colDelete;

    @FXML
    private TableColumn<Admin, Void> colModifier;

    @FXML
    private TableColumn<Admin, Date> date_i;

    @FXML
    private TableColumn<Admin, String> email_u;

    @FXML
    private TableColumn<Admin, Integer> id_u;

    @FXML
    private TableColumn<Admin, String> image_u;

    @FXML
    private TableColumn<Admin, String> mp_u;

    @FXML
    private TableColumn<Admin, String> nom_u;

    @FXML
    private TableColumn<Admin, String> prenom_u;

    @FXML
    private TableColumn<Admin, String> dep;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        id_u.setCellValueFactory(new PropertyValueFactory<>("id_u"));
        cin_u.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nom_u.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom_u.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email_u.setCellValueFactory(new PropertyValueFactory<>("email"));
        mp_u.setCellValueFactory(new PropertyValueFactory<>("motDePasse"));
        date_i.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));
        image_u.setCellValueFactory(new PropertyValueFactory<>("image"));
        dep.setCellValueFactory(new PropertyValueFactory<>("departement"));
        configureModifierColumn();
        configureDeleteColumn();
        loadAdminData();
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
                        Admin adminToDelete = getTableView().getItems().get(getIndex());
                        deleteAdmin(adminToDelete);
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
                        Admin AdminToModify = getTableView().getItems().get(getIndex());
                        modifyAdmin(AdminToModify);
                    });
                }
            }
        });
    }

    private void modifyAdmin(Admin AdminToModify) {
        try {
            // Charger l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAdmin.fxml"));
            Parent root = loader.load();

            // Passer les données de la voiture à modifier au contrôleur de l'interface de modification
            ModifierAdminController modifierController = loader.getController();
            modifierController.initData(AdminToModify);

            // Afficher l'interface de modification
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Modifier Admin");
            stage.showAndWait(); // Attendre que la fenêtre de modification se ferme avant de recharger les données
            reloadAdminData();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface de modification.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private void deleteAdmin(Admin adminToDelete) {
        userService.delete(adminToDelete.getId_u());
        affichage_u.getItems().remove(adminToDelete);
    }

    void loadAdminData() {
        List<Admin> admins = userService.readAllAdmins();
        affichage_u.getItems().addAll(admins);
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML

    private void reloadAdminData() {
        // Effacer toutes les données actuelles de la TableView
        affichage_u.getItems().clear();

        // Recharger les données à partir de la base de données
        List<Admin> admins = userService.readAllAdmins();

        // Afficher les nouvelles données dans la TableView
        affichage_u.getItems().addAll(admins);
    }

    public void ajouterA(javafx.event.ActionEvent actionEvent) {
        try {
            // Charger l'interface d'ajout de voiture
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAdmin.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter Admin");
            stage.showAndWait();
            reloadAdminData();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface d'ajout de voiture.");
        }
    }
    }




