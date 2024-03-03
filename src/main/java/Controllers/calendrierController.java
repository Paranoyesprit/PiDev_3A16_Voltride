package Controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import Entities.Reservation_v;
import Entities.Voiture;
import Service.ReservationVService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class calendrierController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    private ReservationVService reservationService; // Service to handle reservation data access

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        reservationService = new ReservationVService(); // Initialize the reservation service
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        YearMonth yearMonth = YearMonth.of(dateFocus.getYear(), dateFocus.getMonthValue());
        int daysInMonth = yearMonth.lengthOfMonth(); // Obtenez le nombre de jours dans le mois

        // Calcul de la première journée de la semaine et la dernière journée de la semaine
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // List of reservations for a given month
        List<Reservation_v> reservations = reservationService.readAllReservationsForMonth(firstDayOfMonth, lastDayOfMonth);

        Map<LocalDate, List<Reservation_v>> reservationMap = new HashMap<>();
        for (Reservation_v reservation : reservations) {
            LocalDate startDate = reservation.getDate_debut();
            LocalDate endDate = reservation.getDate_fin();
            LocalDate date = startDate;
            while (!date.isAfter(endDate)) {
                reservationMap.computeIfAbsent(date, k -> new ArrayList<>()).add(reservation);
                date = date.plusDays(1);
            }
        }

        int numRows = (int) Math.ceil((double) (daysInMonth + firstDayOfMonth.getDayOfWeek().getValue() - 1) / 7);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / numRows) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int dayOfMonth = j + i * 7 + 1 - firstDayOfMonth.getDayOfWeek().getValue() + 1; // Calcule le jour du mois actuel
                if (dayOfMonth > 0 && dayOfMonth <= daysInMonth) { // Vérifie si le jour est valide pour ce mois
                    Text dayText = new Text(String.valueOf(dayOfMonth));
                    stackPane.getChildren().add(dayText);

                    LocalDate currentDate = yearMonth.atDay(dayOfMonth);
                    List<Reservation_v> reservationsForDate = reservationMap.get(currentDate);
                    if (reservationsForDate != null && !reservationsForDate.isEmpty()) {
                        // Si des réservations existent pour ce jour, affichez les données de la première réservation
                        Reservation_v firstReservation = reservationsForDate.get(0);
                        Voiture reservedVoiture = firstReservation.getVoiture();
                        Text reservationText = new Text(reservedVoiture.getMarque() + " " + reservedVoiture.getModele());
                        Rectangle reservationBox = new Rectangle(rectangleWidth, rectangleHeight / 3);
                        reservationBox.setFill(Color.LIGHTGRAY);
                        stackPane.getChildren().addAll(reservationBox, reservationText);

                        // Ajouter un gestionnaire d'événements pour afficher les détails de la réservation au clic
                        stackPane.setOnMouseClicked(e -> showReservationDetails(firstReservation));
                    }
                }

                calendar.getChildren().add(stackPane);
            }
        }
    }

    // Méthode pour afficher les détails de la réservation
    private void showReservationDetails(Reservation_v reservation) {
        Voiture reservedVoiture = reservation.getVoiture();

        // Obtenir le chemin d'accès local de l'image
        String imagePath = reservedVoiture.getImage();

        // Créer une URL à partir du chemin d'accès local
        String imageUrl = new File(imagePath).toURI().toString();

        // Construction du contenu de la boîte de dialogue avec toutes les informations de la voiture
        String detailsContent = "Marque : " + reservedVoiture.getMarque() + "\n" +
                "Modèle : " + reservedVoiture.getModele() + "\n" +
                "Prix de location : " + reservedVoiture.getPrix_location() + "\n" +
                "Kilométrage : " + reservedVoiture.getKilometrage() + "\n" +
                //  "Image : " + reservedVoiture.getImage() + "\n" + // Utilisez imagePath au lieu de imageUrl ici si vous voulez afficher le chemin d'accès local
                // Ajoutez d'autres champs de voiture si nécessaire
                "Date de début : " + reservation.getDate_debut() + "\n" +
                "Date de fin : " + reservation.getDate_fin();

        // Création d'une ImageView pour afficher l'image de la voiture
        ImageView imageView = new ImageView();
        Image image = new Image(imageUrl); // Utilisez imageUrl au lieu de imagePath ici
        imageView.setImage(image);
        imageView.setFitWidth(200); // Définissez la largeur de l'image
        imageView.setPreserveRatio(true); // Conservez le rapport largeur/hauteur de l'image

        // Création d'une VBox pour contenir les détails de la réservation en dessous de l'image
        VBox detailsBox = new VBox();
        detailsBox.getChildren().addAll(imageView, new Text(detailsContent));
        detailsBox.setSpacing(10);
        detailsBox.setPadding(new Insets(10));

        // Ajout d'un bouton de suppression avec un gestionnaire d'événements pour la suppression
        Button deleteButton = new Button("Supprimer reservation");
        deleteButton.setOnAction(event -> {
            boolean deleteSuccess = reservationService.delete(reservation.getId_r()); // Suppression de la réservation
            if (deleteSuccess) {
                refreshCalendar(); // Actualiser le calendrier après la suppression réussie
            } else {
                // Gérer les erreurs de suppression ici, si nécessaire
                showAlert("Erreur de suppression", "Une erreur s'est produite lors de la suppression de la réservation.");
            }
        });

        // Ajout d'un bouton de modification avec un gestionnaire d'événements pour la redirection vers une autre interface
        Button modifyButton = new Button("Modifier reservation");
        modifyButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierReservationVoiture.fxml"));
                Parent root = loader.load();
                modifierReservationVoitureController controller = loader.getController();
                controller.initData(reservation); // Passer la réservation à l'interface de modification
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        detailsBox.getChildren().addAll(deleteButton, modifyButton);
        Button printButton = new Button("Imprimer");
        printButton.setOnAction(event -> {
            try {
                // Générer le PDF avec les détails de la réservation
                generateReservationPDF(reservation);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        detailsBox.getChildren().add(printButton); // Ajouter le bouton "Imprimer" à la boîte de dialogue

        // Affichage de la boîte de dialogue avec tous les détails de la réservation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la réservation");
        alert.setHeaderText("Détails de la réservation de " + reservedVoiture.getMarque() + " " + reservedVoiture.getModele());
        alert.getDialogPane().setContent(detailsBox);
        alert.showAndWait();
    }

    private void generateReservationPDF(Reservation_v reservation) throws FileNotFoundException {
        String fileName = "reservation_details.pdf";
        List<String> reservationDetails = new ArrayList<>();
        reservationDetails.add("Marque : " + reservation.getVoiture().getMarque());
        reservationDetails.add("Modèle : " + reservation.getVoiture().getModele());
        reservationDetails.add("Prix de location : " + reservation.getVoiture().getPrix_location());
        reservationDetails.add("Kilométrage : " + reservation.getVoiture().getKilometrage());
        reservationDetails.add("Date de début : " + reservation.getDate_debut());
        reservationDetails.add("Date de fin : " + reservation.getDate_fin());

        // Ajoutez l'URL de l'image dans les détails de la réservation

        // Générer le PDF avec les détails de la réservation
        try {
            PDFGenerator.generatePDF(fileName, reservationDetails, "Détails de la réservation");
            showAlert("Succès", "Les détails de la réservation ont été imprimés avec succès sous le nom : " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la génération du PDF : " + e.getMessage());
        }
    }


    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour actualiser le calendrier après une suppression réussie
    private void refreshCalendar() {
        calendar.getChildren().clear(); // Effacez le calendrier existant
        drawCalendar(); // Redessinez le calendrier avec les réservations mises à jour
    }
}
