package pl.plauszta.ui.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import pl.plauszta.game.Game;
import pl.plauszta.game.GameMode;

import java.io.IOException;
import java.util.Optional;

import static pl.plauszta.ui.gui.controllers.SceneChanger.changeScene;

public class HomeController {
    Game game;

    @FXML
    public void startOnePlayerGame(ActionEvent event) throws IOException {
        game = Game.getInstance();
        game.setMode(GameMode.ONE_PLAYER);
        game.changeWordForHangman();
        //System.out.println(game.getHangman().getWord());

        changeScene(event, "game.fxml");
    }

    @FXML
    public void startTwoPlayersGame(ActionEvent event) throws IOException {
        game = Game.getInstance();
        game.setMode(GameMode.TWO_PLAYERS);
        String word = showDialogForPassingWord();

        if ("".equals(word)) {
            return;
        }

        game.changeWordForHangman(word);
        changeScene(event, "game.fxml");
    }

    private String showDialogForPassingWord() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Game");
        dialog.setHeaderText("Player 1");
        dialog.setContentText("Please enter your word:");

        Optional<String> result = dialog.showAndWait();

        // The Java 8 way to get the response value (with lambda expression).
        //result.ifPresent(name -> System.out.println("Your name: " + name));

        return result.orElse("");
    }
}
