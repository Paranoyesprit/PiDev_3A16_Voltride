
package Controllers;

import Entities.Admin;
import Entities.borne;
import Service.BorneService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AfficherBorneController {

    @FXML
    private TableView<borne> affTV;

    @FXML
    private TableColumn<borne, Integer> idbTC;

    @FXML
    private TableColumn<borne, String> emplacementTC;

    @FXML
    private TableColumn<borne, Integer> capaciteTC;

    @FXML
    private TableColumn<borne, String> etatTC;

    @FXML
    private TableColumn<borne, String> dateinstTC;

    @FXML
    private TableColumn<borne, Void> deleteTC;

    @FXML
    private TableColumn<borne, Void> modifyTC;


    private final BorneService borneService = new BorneService();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        idbTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        emplacementTC.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
        capaciteTC.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        etatTC.setCellValueFactory(new PropertyValueFactory<>("etat"));
        dateinstTC.setCellValueFactory(new PropertyValueFactory<>("date_inst"));
        configureDeleteColumn();
        configureModifyColumn();
        loadBorneData();
    }
    private void configureDeleteColumn() {
        // Set up the delete button column
        deleteTC.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        borne borneToDelete = getTableView().getItems().get(getIndex());
                        deleteBorne(borneToDelete);
                    });
                }
            }
        });
    }
    @FXML
    private void generatePDF() {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float tableHeight = 20f;
            float cellMargin = 5f;
            float fontSize = 12f;

            // Draw border for the entire page
            contentStream.setLineWidth(1.5f);
            contentStream.moveTo(margin, margin);
            contentStream.lineTo(margin, yStart);
            contentStream.lineTo(margin + tableWidth, yStart);
            contentStream.lineTo(margin + tableWidth, margin);
            contentStream.lineTo(margin, margin);
            contentStream.stroke();

            // Add a rectangle for the page header
            float headerHeight = 50f;
            contentStream.setLineWidth(1.5f);
            contentStream.moveTo(margin, yStart);
            contentStream.lineTo(margin + tableWidth, yStart);
            contentStream.stroke();

            // Add your organization's logo or text in the header
            // Example: Add text "Your Organization Logo" at the center of the header
            contentStream.setFont(PDType0Font.load(document, new File("C:/Users/Kais/Downloads/Roboto/Roboto-BlackItalic.ttf")), 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + (tableWidth - 150) / 2, yStart - (headerHeight - 20) / 2);
            contentStream.endText();

            // Set up page title
            contentStream.setFont(PDType0Font.load(document, new File("C:/Users/Kais/Downloads/Roboto/Roboto-BlackItalic.ttf")), 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + cellMargin, yStart - headerHeight - 15);
            contentStream.showText("La listes des Bornes");
            contentStream.endText();
            yStart -= headerHeight + 50;

            // Draw table headers
            contentStream.setLineWidth(1.5f);
            contentStream.moveTo(margin, yStart);
            contentStream.lineTo(margin + tableWidth, yStart);
            contentStream.stroke();

            contentStream.setFont(PDType0Font.load(document, new File("C:/Users/Kais/Downloads/Roboto/Roboto-BlackItalic.ttf")), fontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + cellMargin, yStart - 15);
            contentStream.showText("ID");
            contentStream.newLineAtOffset(tableWidth / 4, 0);
            contentStream.showText("Emplacement");
            contentStream.newLineAtOffset(tableWidth / 4, 0);
            contentStream.showText("Capacité");
            contentStream.newLineAtOffset(tableWidth / 4, 0);
            contentStream.showText("État");
            contentStream.endText();
            yStart -= 30;

            // Draw table content
            List<borne> bornes = affTV.getItems();
            for (borne b : bornes) {
                contentStream.setLineWidth(1.0f);
                contentStream.moveTo(margin, yStart);
                contentStream.lineTo(margin + tableWidth, yStart);
                contentStream.stroke();

                contentStream.beginText();
                contentStream.setFont(PDType0Font.load(document, new File("C:/Users/Kais/Downloads/Roboto/Roboto-BlackItalic.ttf")), fontSize);
                contentStream.newLineAtOffset(margin + cellMargin, yStart - 15);
                contentStream.showText(String.valueOf(b.getId()));
                contentStream.newLineAtOffset(tableWidth / 4, 0);
                contentStream.showText(b.getEmplacement());
                contentStream.newLineAtOffset(tableWidth / 4, 0);
                contentStream.showText(String.valueOf(b.getCapacite()));
                contentStream.newLineAtOffset(tableWidth / 4, 0);
                contentStream.showText(b.getEtat());
                contentStream.endText();
                yStart -= tableHeight + cellMargin;
            }

            contentStream.close();

            // Save and open the document
            File file = new File("Bornes.pdf");
            document.save(file);
            document.close();
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while generating the PDF.");
        }
    }

    private void configureModifyColumn() {

        modifyTC.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modify");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                    modifyButton.setOnAction(event -> {
                        borne borneToModify = getTableView().getItems().get(getIndex());
                        modifyBorne(borneToModify);
                    });
                }
            }
        });
    }

    private void loadBorneData() {
        // Read all bornes from the service
        List<borne> bornes = borneService.readAll();

        // Add the bornes to the TableView
        affTV.getItems().addAll(bornes);
    }

    private void deleteBorne(borne borneToDelete) {
        // Delete the item from the database
        borneService.delete(borneToDelete.getId());

        // Remove the item from the TableView
        affTV.getItems().remove(borneToDelete);
    }
    private void modifyBorne(borne borneToModify) {
        try {
            // Charger l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierBorne.fxml"));
            Parent root = loader.load();

            // Passer les données de la borne à modifier au contrôleur de l'interface de modification
            Controllers.ModifierBorneController modifierController = loader.getController();
            modifierController.initData(borneToModify);

            // Afficher l'interface de modification
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Modifier Borne");
            stage.showAndWait(); // Attendre que la fenêtre de modification se ferme avant de recharger les données
            reloadBornData();
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface de modification.");
        }
    }
    @FXML
    private void ajouter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterBorne.fxml"));
            Parent root = loader.load();



            // Afficher l'interface de l'ajout
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter Borne");
            stage.showAndWait();
            loadBorneData();

        } catch (IOException e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ouverture de l'interface de l'ajout.");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML

    private void reloadBornData() {
        // Effacer toutes les données actuelles de la TableView
        affTV.getItems().clear();

        // Recharger les données à partir de la base de données
        List<borne> bornes = borneService.readAll();

        // Afficher les nouvelles données dans la TableView
        affTV.getItems().addAll(bornes);
    }


}