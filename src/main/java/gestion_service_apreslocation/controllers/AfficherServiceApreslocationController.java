package gestion_service_apreslocation.controllers;

import gestion_service_apreslocation.entities.Bus;
import gestion_service_apreslocation.entities.ServiceApreslocation;
import gestion_service_apreslocation.service.ServiceApreslocationServices;
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
    @FXML
    private TableColumn<ServiceApreslocation, Integer> id_clientColumn;
    @FXML
    private TableColumn<ServiceApreslocation, Integer> id_voitureColumn;

    private final ObservableList<ServiceApreslocation> services = FXCollections.observableArrayList();
    @FXML
    private TableColumn<ServiceApreslocation, Void> deleteColumn;



    @FXML
    private TableColumn<ServiceApreslocation, Void> updateColumn;
    private gestion_service_apreslocation.service.ServiceApreslocationServices ServiceApreslocationServices;



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
        id_clientColumn.setCellValueFactory(new PropertyValueFactory<>("id_client"));
        id_voitureColumn.setCellValueFactory(new PropertyValueFactory<>("id_voiture"));

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


        // Load data from the database
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

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
               ServiceApreslocationServices.delete(id);
                refreshTable();
            }
        });
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
}