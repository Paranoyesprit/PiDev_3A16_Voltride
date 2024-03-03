package Controllers;

import Entities.*;
import Service.ReservationVService;
import Service.VoitureService;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

public class consulterListReservationController {
    @FXML
    private GridPane gridPane;
    @FXML
    private final ReservationVService rs = new ReservationVService();
    @FXML
    private final VoitureService voitureService = new VoitureService();

    public void initialize() {
        // Lecture de toutes les voitures
        int id_user = UserSession.getInstance().getCurrentUser().getId_u();
        List<Reservation_v> reservations = rs.readAllForUser(id_user);
        int rowIndex = 1; // Commencer à partir de la deuxième ligne après le bouton
        int columnIndex = 0;
        for (Reservation_v reservation : reservations) {
            addCarToGridPane(reservation, rowIndex, columnIndex);
            columnIndex++;
            if (columnIndex == 3) {
                rowIndex++;
                columnIndex = 0;
            }
        }
    }

    // Méthode pour ajouter une réservation au GridPane
    private void addCarToGridPane(Reservation_v reservation, int rowIndex, int columnIndex) {
        Voiture voiture = reservation.getVoiture();
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

        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(event -> {
            // Supprimer la réservation associée à cette voiture
            rs.delete(reservation.getId_r());
            // Actualiser l'affichage
            refreshGridPane();
        });

        carBox.getChildren().addAll(marqueLabel, modeleLabel, prixLabel, kilometrageLabel, imageView, deleteButton);
        gridPane.add(carBox, columnIndex, rowIndex);
    }

    // Méthode pour actualiser le GridPane après suppression
    private void refreshGridPane() {
        gridPane.getChildren().clear(); // Effacer le contenu actuel
        initialize(); // Réinitialiser l'affichage avec les données mises à jour
    }
}
