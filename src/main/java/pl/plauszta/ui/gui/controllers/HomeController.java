package pl.plauszta.ui.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import pl.plauszta.game.Game;
import pl.plauszta.game.GameMode;
import pl.plauszta.game.WordDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static pl.plauszta.ui.gui.controllers.SceneChanger.changeScene;

public class HomeController implements Initializable {
    Game game;

    @FXML
    ChoiceBox<String> choiceBoxLanguage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxLanguage.getItems().add("English");
        choiceBoxLanguage.getItems().add("Polish");
        choiceBoxLanguage.setValue(WordDatabase.getInstance().isEnglishVersion() ? "English" : "Polish");
        choiceBoxLanguage.setTooltip(new Tooltip("Select the language"));

        choiceBoxLanguage.getSelectionModel()
                .selectedIndexProperty()
                .addListener((observableValue, value, newValue) -> {
                    if (value.intValue() != newValue.intValue()) {
                        WordDatabase.getInstance().setEnglishVersion(newValue.intValue() == 0);
                    }
                });

        WordDatabase.getInstance().init();
    }

    @FXML
    public void startOnePlayerGame(ActionEvent event) throws IOException {
        game = Game.getInstance();
        game.setMode(GameMode.ONE_PLAYER);
        game.changeWordForHangman();

        boolean englishVersion = WordDatabase.getInstance().isEnglishVersion();
        changeScene(event, englishVersion ? "gameNewVersion.fxml" : "gameNewVersionPol.fxml");
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

        return result.orElse("");
    }
}
