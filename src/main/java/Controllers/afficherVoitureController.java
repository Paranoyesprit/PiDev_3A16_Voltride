package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class afficherVoitureController {
    @FXML
    private TextField id_etat1;

    @FXML
    private TextField id_killometrage1;

    @FXML
    private TextField id_list;

    @FXML
    private TextField id_marque1;

    @FXML
    private TextField id_modele1;

    @FXML
    private TextField id_prix1;

    public void setId_etat1(String id_etat) {
        this.id_etat1.setText(id_etat);
    }

    public void setId_killometrage1(String id_killometrage1) {
        this.id_killometrage1.setText(id_killometrage1);
    }

    public void setId_list(String id_list) {
        this.id_list.setText(id_list);
    }

    public void setId_marque1(String id_marque1) {
        this.id_marque1.setText(id_marque1);
    }

    public void setId_modele1(String id_modele1) {
        this.id_modele1.setText(id_modele1);
    }

    public void setId_prix1(String id_prix1) {
        this.id_prix1.setText(id_prix1);
    }
}
