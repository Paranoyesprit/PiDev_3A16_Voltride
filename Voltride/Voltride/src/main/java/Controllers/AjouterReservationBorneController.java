package Controllers;

import Entities.Reservation_b;
import Entities.borne;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import Service.ReservationBService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AjouterReservationBorneController {

    private final ReservationBService reservationService = new ReservationBService();

    @FXML
    private DatePicker resdated;

    @FXML
    private DatePicker resdatef;
    private borne reservedBorne; // Ajouter un champ pour stocker la voiture réservée

    // Méthode pour initialiser les champs avec les détails de la voiture réservée
    public void setReservedBorne(borne reservedBorne) {
        this.reservedBorne = reservedBorne;
        // Mettez à jour les champs avec les détails de la voiture réservée, si nécessaire
    }

    @FXML
    void reserver(ActionEvent event)  {
        LocalDate dateDebut = resdated.getValue();
        LocalDate dateFin = resdatef.getValue();

        if (dateDebut != null && dateFin != null && reservedBorne != null) {
            // Convert LocalDate to LocalDateTime using atStartOfDay
            LocalDateTime debutDateTime = dateDebut.atStartOfDay();
            LocalDateTime finDateTime = dateFin.atStartOfDay();

            // Créer une nouvelle instance de Reservation_b avec les dates sélectionnées et la voiture réservée
            Reservation_b reservation = new Reservation_b(debutDateTime, finDateTime, reservedBorne);

            // Ajouter la réservation en utilisant le service de réservation
            reservationService.addpst(reservation);


            Stage stage = (Stage) resdatef.getScene().getWindow();
            stage.close();
            // Afficher un message de succès
            showAlert("Réservation ajoutée", "La réservation a été ajoutée avec succès.");
        } else {
            // Afficher un message d'erreur si les dates ne sont pas sélectionnées ou si la voiture n'est pas définie
            showAlertF("Erreur", "Veuillez sélectionner à la fois la date de début et la date de fin, et vérifier que la voiture est correctement définie.");
        }

    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlertF(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
