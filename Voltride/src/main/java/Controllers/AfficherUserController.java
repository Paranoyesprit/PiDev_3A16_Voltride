package Controllers;

import Entities.User;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherUserController {

    @FXML
    private TableView<User> affichage_cl;

    @FXML
    private Button ajouter;

    @FXML
    private TableColumn<User, Integer> id_cl;

    @FXML
    private TableColumn<User, Integer> cin_cl;

    @FXML
    private TableColumn<User, String> nom_cl;

    @FXML
    private TableColumn<User, String> prenom_cl;

    @FXML
    private TableColumn<User, String> email_cl;

    @FXML
    private TableColumn<User, String> mp_cl;

    @FXML
    private TableColumn<User, String> date_cl;

    @FXML
    private TableColumn<User, String> image_cl;

    @FXML
    private TableColumn<User, Void> colDeletec;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        id_cl.setCellValueFactory(new PropertyValueFactory<>("id_u"));
        cin_cl.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nom_cl.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom_cl.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email_cl.setCellValueFactory(new PropertyValueFactory<>("email"));
        mp_cl.setCellValueFactory(new PropertyValueFactory<>("motDePasse"));
        date_cl.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));
        image_cl.setCellValueFactory(new PropertyValueFactory<>("image"));
        configureDeleteColumnc();
        loadUserData();
    }

    private void configureDeleteColumnc() {
        colDeletec.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        User userToDelete = getTableView().getItems().get(getIndex());
                        deleteUser(userToDelete);
                    });
                }
            }
        });
    }

    void loadUserData() {
        List<User> users = userService.readAllU();
        affichage_cl.getItems().addAll(users);
    }

    private void deleteUser(User userToDelete) {
        userService.delete(userToDelete.getId_u());
        affichage_cl.getItems().remove(userToDelete);
    }




}
