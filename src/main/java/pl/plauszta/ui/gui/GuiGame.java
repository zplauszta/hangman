package pl.plauszta.ui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;

public class GuiGame extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        final URL homeResource = GuiGame.class.getClassLoader().getResource("homeScene.fxml");
        final Pane homePane = FXMLLoader.load(homeResource);
        final Scene scene = new Scene(homePane);
        stage.setScene(scene);
        stage.setTitle("Hangman");
        stage.getIcons().add(new Image("6.jpg"));

        stage.show();
    }
}