package Controllers;

import Entities.Reservation_b;
import Entities.SendSms;
import Entities.borne;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Service.ReservationBService;

import java.time.LocalDate;
import java.time.LocalTime;

public class AjouterReservationBorneController {

    private final ReservationBService reservationService = new ReservationBService();

    @FXML
    private DatePicker resdated;

    @FXML
    private DatePicker resdatef;

    @FXML
    private TextField heureDTextField;

    @FXML
    private TextField heureFTextField;

    private borne reservedBorne;

    public void setReservedBorne(borne reservedBorne) {
        this.reservedBorne = reservedBorne;
    }

    @FXML
    void reserver(ActionEvent event) {
        LocalDate dateDebut = resdated.getValue();
        LocalDate dateFin = resdatef.getValue();

        if (dateDebut != null && dateFin != null && reservedBorne != null) {
            // Assuming time format in text fields is HH:mm
            LocalTime heureDebut = LocalTime.parse(heureDTextField.getText());
            LocalTime heureFin = LocalTime.parse(heureFTextField.getText());

            Reservation_b reservation = new Reservation_b(dateDebut, heureDebut, dateFin, heureFin, reservedBorne);

            reservationService.addpst(reservation);
            SendSms.SendSms(reservation,"votre reservation commence:","+21655695969");

            Stage stage = (Stage) resdatef.getScene().getWindow();
            stage.close();

            showAlert("Réservation ajoutée", "La réservation a été ajoutée avec succès.");
        } else {
            showAlertF("Erreur", "Veuillez sélectionner à la fois la date de début et la date de fin, et vérifier que la borne est correctement définie.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertF(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
