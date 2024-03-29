package Controllers;

import Entities.Bus;
import Entities.ServiceApreslocation;
import Entities.Type;
import Service.TypeService;
import Service.ServiceApreslocationServices;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherServiceApreslocationController implements Initializable, Bus.EventListener {

    @FXML
    private TableView<ServiceApreslocation> tableViewservices;

    @FXML
    private TableColumn<ServiceApreslocation, String> idColumn;

    @FXML
    private TableColumn<ServiceApreslocation, String> typeColumn;

    @FXML
    private TableColumn<ServiceApreslocation, String> technicienColumn;

    @FXML
    private TableColumn<ServiceApreslocation, String> descriptionColumn;

    @FXML
    private TableColumn<ServiceApreslocation, Double> statutColumn;
    @FXML
    private TableColumn<ServiceApreslocation, Integer> coutColumn;


    private final ObservableList<ServiceApreslocation> services = FXCollections.observableArrayList();
    @FXML
    private TableColumn<ServiceApreslocation, Void> deleteColumn;



    @FXML
    private TableColumn<ServiceApreslocation, Void> updateColumn;
    private Service.ServiceApreslocationServices ServiceApreslocationServices;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServiceApreslocationServices = new ServiceApreslocationServices();

        //n7adher les columns bch y affichi les attributs w yorbothhom mel base

        idColumn.setCellValueFactory(new PropertyValueFactory<>("idservice"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        technicienColumn.setCellValueFactory(new PropertyValueFactory<>("technicien"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        coutColumn.setCellValueFactory(new PropertyValueFactory<>("cout"));

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    ServiceApreslocation service = getTableView().getItems().get(getIndex());
                    supprimerService(service.getIdservice());
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
                    ServiceApreslocation serviceApreslocation = getTableView().getItems().get(getIndex());
                    updateService(serviceApreslocation.getIdservice());
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


        // loading lel data mel data base
        List<ServiceApreslocation> apreslocationList = ServiceApreslocationServices.readAll();
        services.addAll(apreslocationList);
        tableViewservices.setItems(services);
        Bus.getInstance().register(this);
    }

    public void onTableRefreshed() {
        refreshTable();
    }

    public void supprimerService(int id) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce Service");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ServiceApreslocationServices.delete(id);
            refreshTable();
        }
    }


    @FXML
    void handleAjouterService(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterServiceApreslocation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//fonction bch taamel refresh lel table
    private void refreshTable() {
        services.clear();
        List<ServiceApreslocation> evenementsList = ServiceApreslocationServices.readAll();
        services.addAll(evenementsList);
        tableViewservices.setItems(services);
    }
    public void updateService(int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateServiceApreslocation.fxml"));
            Parent root = loader.load();

            UpdateServiceApreslocationController updateController = loader.getController();
            updateController.initData(id);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectToAddType(ActionEvent actionEvent) {

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

    public void redirectToShowType(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherType.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}