package Controllers;

import Entities.Reservation_v;
import Entities.Voiture;
import Service.ReservationVService;
import Service.VoitureService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HboxListController {

    @FXML
    private GridPane gridPane;

    @FXML
    private DatePicker datePicker;

    private final VoitureService voitureService = new VoitureService();
    private final ReservationVService vs = new ReservationVService();
    @FXML
    public void initialize() {
        // Création du bouton pour tout le GridPane
        Button ajouterReservationButton = new Button("Ajouter une réservation");
        ajouterReservationButton.setOnAction(this::ajouterReservationButtonClicked);
        gridPane.add(ajouterReservationButton, 0, 0, 3, 1); // Ajouter le bouton à la première ligne du GridPane

        // Lecture de toutes les voitures
        List<Voiture> voitures = voitureService.readAll();
        int rowIndex = 1; // Commencer à partir de la deuxième ligne après le bouton
        int columnIndex = 0;
        for (Voiture voiture : voitures) {
            addCarToGridPane(voiture, rowIndex, columnIndex);
            columnIndex++;
            if (columnIndex == 3) {
                rowIndex++;
                columnIndex = 0;
            }
        }
    }

    // Méthode pour ajouter une voiture au GridPane
    private void addCarToGridPane(Voiture voiture, int rowIndex, int columnIndex) {
        VBox carBox = new VBox();
        carBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1px;");

        Label marqueLabel = new Label("Marque: " + voiture.getMarque());
        Label modeleLabel = new Label("Modèle: " + voiture.getModele());
        Label prixLabel = new Label("Prix: " + voiture.getPrix_location());
        Label kilometrageLabel = new Label("Kilométrage: " + voiture.getKilometrage());

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        try {
            File file = new File(voiture.getImage());
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button reserverButton = new Button("Réserver");
        reserverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterReservation.fxml"));
                Parent root;
                try {
                    root = loader.load();
                    ajouterReservationController controller = loader.getController();
                    controller.setReservedCar(voiture); // Passer la voiture réservée au contrôleur de la fenêtre de réservation
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        carBox.getChildren().addAll(marqueLabel, modeleLabel, prixLabel, kilometrageLabel, imageView, reserverButton);
        gridPane.add(carBox, columnIndex, rowIndex);
    }

    // Méthode pour gérer le clic sur le bouton "Ajouter une réservation"
    @FXML
    private void ajouterReservationButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/calendrier.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Consulter les réservations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour gérer le clic sur le bouton "Chercher"
    @FXML
    private void searchButtonClicked(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();

        // Efface le contenu actuel du GridPane
        gridPane.getChildren().removeIf(node -> node instanceof VBox);

        // Met à jour l'affichage avec les voitures disponibles pour la date sélectionnée
        List<Voiture> availableCars = vs.getAvailableCars(selectedDate, selectedDate);
        int rowIndex = 1;
        int columnIndex = 0;
        for (Voiture voiture : availableCars) {
            addCarToGridPane(voiture, rowIndex, columnIndex);
            columnIndex++;
            if (columnIndex == 3) {
                rowIndex++;
                columnIndex = 0;
            }
        }
    }


    private void showAlert(String erreur, String s) {
    }

}
