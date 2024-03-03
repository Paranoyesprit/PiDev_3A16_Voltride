package Controllers;
import Entities.Bus;
import Entities.Type;
import Service.TypeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherTypeController implements Initializable, Bus.EventListener {

    @FXML
    private TableView<Type> tableViewType;

    @FXML
    private TableColumn<Type, Integer> idColumn;

    @FXML
    private TableColumn<Type, String> typeNameColumn;

    @FXML
    private TableColumn<Type, Void> deleteColumn;

    @FXML
    private TableColumn<Type, Void> updateColumn;

    private final ObservableList<Type> types = FXCollections.observableArrayList();
    private TypeService typeService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeService = new TypeService();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeNameColumn.setCellValueFactory(new PropertyValueFactory<>("typeName"));

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Type type = getTableView().getItems().get(getIndex());
                    supprimerType(type.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });


        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Modifier");

            {
                updateButton.setOnAction(event -> {
                    Type type = getTableView().getItems().get(getIndex());
                    updateService(type.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        List<Type> typesList = typeService.readAll();
        types.addAll(typesList);
        tableViewType.setItems(types);
        Bus.getInstance().register(this);
    }

    @Override
    public void onTableRefreshed() {
        refreshTable();
    }

    public void supprimerType(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this event?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                typeService.delete(id);
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        types.clear();
        List<Type> evenementsList = typeService.readAll();
        types.addAll(evenementsList);
        tableViewType.setItems(types);
    }

    public void redirectToAddType(ActionEvent type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterType.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateService(int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateType.fxml"));
            Parent root = loader.load();

            UpdateTypeController updateController = loader.getController();
            updateController.initData(id);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
