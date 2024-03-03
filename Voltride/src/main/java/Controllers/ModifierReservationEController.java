package Controllers;


import Entities.Reservation_e;
import Service.ReservationEService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierReservationEController {

    @FXML
    private TextField nbCom;

    @FXML
    private TextField nvnbrP;
    private final ReservationEService rbs = new ReservationEService();
    private Reservation_e reservationToModify;


    public void initData(Reservation_e reservation_e) {
        this.reservationToModify = reservation_e;
        // Afficher les détails de la réservation à modifier dans les champs appropriés
        nvnbrP.setText(Integer.toString(reservation_e.getNbrPersonne()));
        nbCom.setText(reservation_e.getCommentaire());
    }

    @FXML
    void modifierReservation(ActionEvent event) {
        try {
            // Mettre à jour les données de la réservation avec les nouvelles valeurs
            reservationToModify.setNbrPersonne(Integer.parseInt(nvnbrP.getText()));
            reservationToModify.setCommentaire(nbCom.getText());

            // Appeler la méthode de mise à jour dans le service de réservation
            rbs.update(reservationToModify.getId_e(), reservationToModify);
            Stage stage = (Stage) nvnbrP.getScene().getWindow();
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

