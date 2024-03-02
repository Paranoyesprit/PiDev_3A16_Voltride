package Controllers;

import Entities.Evenement;
import Entities.Reservation_e;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import Service.ReservationEService;

import java.time.LocalDate;

public class AjouterReservationEventController {

    @FXML
    private TextField ResCommentaire;

    @FXML
    private TextField ResnbrPersonne;

    private final ReservationEService reservationService = new ReservationEService();

    private Evenement reservedEvent; // Ajouter un champ pour stocker l'événement réservé

    // Méthode pour initialiser les champs avec les détails de l'événement réservé
    public void setreservedEvent(Evenement reservedEvent) {
        this.reservedEvent = reservedEvent;
        // Mettez à jour les champs avec les détails de l'événement réservé, si nécessaire
    }

    @FXML
    void ajouterReservation(ActionEvent event) {
        int nbrPersonne = Integer.parseInt(ResnbrPersonne.getText());
        String commentaire = ResCommentaire.getText();

        if (nbrPersonne > 0 && reservedEvent != null) {
            // Créer une nouvelle instance de Reservation_e avec le nombre de personnes et le commentaire
            Reservation_e reservation = new Reservation_e(nbrPersonne, commentaire, reservedEvent);

            // Ajouter la réservation en utilisant le service de réservation
            reservationService.add(reservation);

            // Afficher un message de succès
            showAlert("Réservation ajoutée", "La réservation a été ajoutée avec succès.");
        } else {
            // Afficher un message d'erreur si le nombre de personnes est invalide ou si l'événement n'est pas défini
            showAlert("Erreur", "Veuillez entrer un nombre de personnes valide et vérifier que l'événement est correctement défini.");
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
