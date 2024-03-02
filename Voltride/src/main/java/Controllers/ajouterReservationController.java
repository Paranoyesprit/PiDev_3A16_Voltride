package Controller;

import Entities.Reservation_v;
import Entities.Voiture;
import Service.ReservationVService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class ajouterReservationController {

    @FXML
    private DatePicker calDateDebut;

    @FXML
    private DatePicker calDateFin;

    private final ReservationVService reservationService = new ReservationVService();

    private Voiture reservedCar; // Ajouter un champ pour stocker la voiture réservée

    // Méthode pour initialiser les champs avec les détails de la voiture réservée
    public void setReservedCar(Voiture reservedCar) {
        this.reservedCar = reservedCar;
        // Mettez à jour les champs avec les détails de la voiture réservée, si nécessaire
    }

    @FXML
    void ajouterReservation(ActionEvent event) {
        LocalDate dateDebut = calDateDebut.getValue();
        LocalDate dateFin = calDateFin.getValue();

        if (dateDebut != null && dateFin != null && reservedCar != null) {
            // Créer une nouvelle instance de Reservation_v avec les dates sélectionnées et la voiture réservée
            Reservation_v reservation = new Reservation_v(dateDebut, dateFin, reservedCar);

            // Ajouter la réservation en utilisant le service de réservation
            reservationService.addV(reservation);

            // Afficher un message de succès
            showAlert("Réservation ajoutée", "La réservation a été ajoutée avec succès.");
        } else {
            // Afficher un message d'erreur si les dates ne sont pas sélectionnées ou si la voiture n'est pas définie
            showAlert("Erreur", "Veuillez sélectionner à la fois la date de début et la date de fin, et vérifier que la voiture est correctement définie.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
