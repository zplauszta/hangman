package pl.zuzu.ui.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.zuzu.TooManyMistakesException;
import pl.zuzu.game.Game;
import pl.zuzu.game.Status;
import pl.zuzu.ui.gui.GuiGame;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static pl.zuzu.ui.gui.controllers.SceneChanger.changeScene;

public class GameController implements Initializable {
    @FXML
    private TextField fieldWithChar;

    @FXML
    private ImageView imageOfHangman;

    @FXML
    private Label guessedLetters;

    @FXML
    private TextArea usedLetters;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fieldWithChar.setTextFormatter(new TextFormatter<String>(this::limitToOneSign));
        guessedLetters.setText(Game.getInstance().getHangman().getGuessedLetters());
    }

    private TextFormatter.Change limitToOneSign(TextFormatter.Change change) {
        String newText = change.getControlNewText();
        if (newText.length() > 1) {
            return null;
        } else {
            return change;
        }
    }

    public void enterLetter(ActionEvent event) throws TooManyMistakesException, IOException {
        if (fieldWithChar.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Important Message");
            alert.setHeaderText(null);
            alert.setContentText("enter the letter!");
            alert.showAndWait();
            return;
        }
        char letter = fieldWithChar.getText().toLowerCase().charAt(0);
        final Game game = Game.getInstance();
        int stateOfGuess = game.getHangman().checkLetter(letter);
        guessedLetters.setText(game.getHangman().getGuessedLetters());
        if (stateOfGuess == -1) {
            changeImage(game.getHangman().getStatus());
        }
        updateScene();

        if (game.getHangman().isEnd()) {
            displayAlert();
            changeScene(event, "home.fxml");
            game.resetGame();
        }
    }

    private void displayAlert() {
        String message;
        final Game game = Game.getInstance();
        if (game.getHangman().getStatus().equals(Status.GUESSED)) {
            message = "Congrats! You guess the word "
                    + game.getHangman().getWord() + " :)";
        } else {
            message = "Buu! You lose. The word was "
                    + game.getHangman().getWord();
        }
        makeAlert(message).showAndWait();
    }

    private void updateScene() {
        fieldWithChar.setText("");
        StringBuilder letters = new StringBuilder();
        for (Character usedCharacter : Game.getInstance().getHangman().getUsedCharacters()) {
            letters.append(usedCharacter).append("  ");
        }
        usedLetters.setText(letters.toString());
    }

    private void changeImage(Status status) {
        String name = status.ordinal() + ".jpg";
        final URL resource = GuiGame.class.getClassLoader().getResource(name);
        imageOfHangman.setImage(new Image(resource.toString()));
    }

    private Alert makeAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Important Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
    }
}
