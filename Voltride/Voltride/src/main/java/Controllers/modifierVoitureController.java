package Controller;

import Entities.Voiture;
import Service.VoitureService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class modifierVoitureController {

        private final VoitureService vs = new VoitureService();
        private Voiture voitureToModify;

        @FXML
        private TextField id_etat_modifer;

        @FXML
        private TextField id_kilometrage_modifer;

        @FXML
        private TextField id_marque_modifer;

        @FXML
        private TextField id_modele_modifer;

        @FXML
        private TextField id_prix_modifer;

        public void initData(Voiture voiture) {
                this.voitureToModify = voiture;
                // Afficher les détails de la voiture à modifier dans les champs de texte
                id_marque_modifer.setText(voiture.getMarque());
                id_modele_modifer.setText(voiture.getModele());
                id_etat_modifer.setText(voiture.getEtat());
                id_prix_modifer.setText(String.valueOf(voiture.getPrix_location()));
                id_kilometrage_modifer.setText(String.valueOf(voiture.getKilometrage()));
        }

        @FXML
        void modifierVoiture(ActionEvent event) {
                try {
                        // Mettre à jour les données de la voiture avec les nouvelles valeurs
                        voitureToModify.setMarque(id_marque_modifer.getText());
                        voitureToModify.setModele(id_modele_modifer.getText());
                        voitureToModify.setEtat(id_etat_modifer.getText());
                        voitureToModify.setPrix_location(Float.parseFloat(id_prix_modifer.getText()));
                        voitureToModify.setKilometrage(Float.parseFloat(id_kilometrage_modifer.getText()));

                        // Appeler la méthode de mise à jour dans le service de voiture
                        vs.update(voitureToModify.getId_v(), voitureToModify);

                        // Afficher une alerte pour confirmer la modification
                        showAlert("Modification réussie", "La voiture a été modifiée avec succès.");
                } catch (Exception e) {
                        showAlert("Erreur", "Une erreur s'est produite lors de la modification de la voiture.");
                }
        }


        private void showAlert(String title, String content) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(content);
                alert.showAndWait();
        }
}
