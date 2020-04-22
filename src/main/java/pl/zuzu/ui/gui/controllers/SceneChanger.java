package pl.zuzu.ui.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.zuzu.ui.gui.GuiGame;

import java.io.IOException;
import java.net.URL;

public class SceneChanger {

    static void changeScene(ActionEvent event, String name) throws IOException {
        final Node node = (Node) event.getTarget();
        Stage stage = (Stage) node.getScene().getWindow();
        final URL resource = GuiGame.class.getClassLoader().getResource(name);
        final Pane pane = FXMLLoader.load(resource);
        final Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
