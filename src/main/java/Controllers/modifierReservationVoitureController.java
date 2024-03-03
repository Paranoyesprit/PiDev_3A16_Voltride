package Controllers;


import Entities.Reservation_v;
import Service.ReservationVService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;

public class modifierReservationVoitureController {



    @FXML
    private DatePicker modifDateDReservation;

    @FXML
    private DatePicker modifDateFReservation;
    private Reservation_v reservationToModify;

    private final ReservationVService rs = new ReservationVService();

    public void initData(Reservation_v reservation_v) {
        this.reservationToModify = reservation_v;
        // Afficher les détails de la borne à modifier dans les champs de texte
        modifDateDReservation.setValue(LocalDate.from(reservation_v.getDate_debut()));
        modifDateFReservation.setValue(LocalDate.from(reservation_v.getDate_fin()));

    }

    @FXML
    void modReservationV(ActionEvent event) {
        try {
            // Mettre à jour les données de la reservation avec les nouvelles valeurs

            reservationToModify.setDate_debut(modifDateDReservation.getValue());
            reservationToModify.setDate_fin(modifDateFReservation.getValue());


            // Appeler la méthode de mise à jour dans le service de reservation
            rs.update(reservationToModify.getId_r(), reservationToModify);
            Stage stage = (Stage) modifDateDReservation.getScene().getWindow();
            stage.close();
            // Afficher une alerte pour confirmer la modification
            showAlert("Modification réussie", "La borne a été modifiée avec succès.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification de la borne.");
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
