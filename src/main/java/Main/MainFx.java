package Main;

import Controllers.LoginController;
import Controllers.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);



        // Récupérer le contrôleur de la vue Login
        LoginController loginController = loader.getController();

        // Définir le stage pour le contrôleur LoginController
        loginController.setStage(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Voltride");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
