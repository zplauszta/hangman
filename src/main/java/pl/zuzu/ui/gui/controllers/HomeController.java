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
import pl.zuzu.game.Hangman;
import pl.zuzu.ui.gui.GuiGame;

import java.io.IOException;
import java.net.URL;

public class HomeController {

    @FXML
    public void startOnePlayerGame(ActionEvent event) throws IOException {
        Game.getInstance().setMode(GameMode.ONE_PLAYER);
        Game.getInstance().getWordDatabase().init();
        Game.getInstance().setRandomWordForHangman();


        changeSceneToGame(event);
    }

    @FXML
    public void startTwoPlayersGame(ActionEvent event) throws IOException {
        Game.getInstance().setMode(GameMode.TWO_PLAYERS);

        changeSceneToGame(event);
    }

    private void changeSceneToGame(ActionEvent event) throws IOException {
        final Button clickedButton = (Button) event.getTarget();
        Stage stage = (Stage) clickedButton.getScene().getWindow();

        final URL homeResource = GuiGame.class.getClassLoader().getResource("game.fxml");
        final Pane homePane = FXMLLoader.load(homeResource);
        final Scene scene = new Scene(homePane);
        stage.setScene(scene);
        stage.show();
    }
}
