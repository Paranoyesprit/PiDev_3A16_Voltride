package Controllers;

import Entities.Reservation_b;
import Service.ReservationBService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ModifierReservationBController {

    @FXML
    private DatePicker nvdatedDP;

    @FXML
    private DatePicker nvdatefDP;

    @FXML
    private TextField nvheure_dTF;

    @FXML
    private TextField nvheure_fTF;

    private final ReservationBService rbs = new ReservationBService();
    private Reservation_b reservationToModify;

    public void initData(Reservation_b reservation_b) {
        this.reservationToModify = reservation_b;
        // Afficher les détails de la réservation à modifier dans les champs de texte
        nvdatedDP.setValue(reservation_b.getDate_d());
        nvdatefDP.setValue(reservation_b.getDate_f());
        nvheure_dTF.setText(reservation_b.getHeure_d().format(DateTimeFormatter.ofPattern("HH:mm")));
        nvheure_fTF.setText(reservation_b.getHeure_f().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    @FXML
    void modifierReservation(ActionEvent event) {
        try {
            // Mettre à jour les données de la réservation avec les nouvelles valeurs
            reservationToModify.setDate_d(nvdatedDP.getValue());
            reservationToModify.setDate_f(nvdatefDP.getValue());

            // Parse the heure_d and heure_f from the text fields
            LocalTime heure_d = LocalTime.parse(nvheure_dTF.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime heure_f = LocalTime.parse(nvheure_fTF.getText(), DateTimeFormatter.ofPattern("HH:mm"));

            reservationToModify.setHeure_d(heure_d);
            reservationToModify.setHeure_f(heure_f);

            // Appeler la méthode de mise à jour dans le service de réservation
            rbs.update(reservationToModify.getId_r(), reservationToModify);
            Stage stage = (Stage) nvdatedDP.getScene().getWindow();
            stage.close();
            // Afficher une alerte pour confirmer la modification
            showAlert("Modification réussie", "La réservation a été modifiée avec succès.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification de la réservation.");
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
