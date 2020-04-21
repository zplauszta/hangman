package pl.zuzu.ui.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.zuzu.game.Game;
import pl.zuzu.game.GameMode;
import pl.zuzu.ui.gui.GuiGame;

import java.io.IOException;
import java.net.URL;

public class HomeController {
    Game game = Game.getInstance();

    @FXML
    public void startOnePlayerGame(ActionEvent event) throws IOException {
        game.setMode(GameMode.ONE_PLAYER);
        game.getWordDatabase().init();
        game.setRandomWordForHangman();


        changeScene(event, "game.fxml");
    }

    @FXML
    public void startTwoPlayersGame(ActionEvent event) throws IOException {
        game.setMode(GameMode.TWO_PLAYERS);

        changeScene(event, "game.fxml");
    }

    private void changeScene(ActionEvent event, String name) throws IOException {
        final Button clickedButton = (Button) event.getTarget();
        Stage stage = (Stage) clickedButton.getScene().getWindow();

        final URL homeResource = GuiGame.class.getClassLoader().getResource(name);
        final Pane homePane = FXMLLoader.load(homeResource);
        final Scene scene = new Scene(homePane);
        stage.setScene(scene);
        stage.show();
    }
}
