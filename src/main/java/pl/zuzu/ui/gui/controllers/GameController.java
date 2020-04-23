package pl.zuzu.ui.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.zuzu.TooManyMistakesException;
import pl.zuzu.game.Game;
import pl.zuzu.game.GameMode;
import pl.zuzu.game.Hangman;
import pl.zuzu.game.Status;
import pl.zuzu.ui.gui.GuiGame;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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

    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private boolean player2Guessing = true;

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
        final Game game = Game.getInstance();

        if (fieldWithChar.getText().trim().equals("")) {
            updateScene();
            makeAlert("enter the letter!").showAndWait();
            return;
        }

        char letter = fieldWithChar.getText().toLowerCase().charAt(0);
        game.getHangman().checkLetter(letter);

        updateScene();

        if (game.getHangman().isEnd()) {
            String message;
            if (game.getHangman().getStatus().equals(Status.GUESSED)) {
                if (player2Guessing) {
                    scorePlayer2++;
                } else {
                    scorePlayer1++;
                }

                message = "Congrats! You guess the word "
                        + game.getHangman().getWord() + " :)";
            } else {
                message = "Buu! You lose. The word was "
                        + game.getHangman().getWord();
            }

            if (GameMode.ONE_PLAYER.equals(game.getMode())) {
                message += "\nPlayer score: " + scorePlayer2;
            } else {
                message += "\nPlayer1 score: " + scorePlayer1 + "\nPlayer 2 score: " + scorePlayer2;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("End of game");
            alert.setHeaderText(null);
            alert.setContentText(message + "\nDo you want guess a word again?");

            ButtonType buttonPlayAgain = new ButtonType("Play!");
            ButtonType buttonBackToHome = new ButtonType("Back to home");

            alert.getButtonTypes().setAll(buttonPlayAgain, buttonBackToHome);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == buttonPlayAgain) {
                if (game.getMode().equals(GameMode.ONE_PLAYER)) {
                    game.setRandomWordForHangman();
                    System.out.println(game.getHangman().getWord());
                    updateScene();
                } else {
                    player2Guessing = !player2Guessing;
                    TextInputDialog dialog = new TextInputDialog("");
                    dialog.setTitle("New Game");
                    dialog.setHeaderText(player2Guessing ? "Player1" : "Player2");
                    dialog.setContentText("Please enter your word or leave empty if you want random word:");

                    Optional<String> word = dialog.showAndWait();

                    if ("".equals(word.orElse("").trim())) {
                        game.setRandomWordForHangman();
                    } else {
                        game.setHangman(new Hangman(word.get()));
                        updateScene();
                    }
                }
            } else {
                scorePlayer2 = 0;
                scorePlayer1 = 0;
                player2Guessing = true;
                changeScene(event, "home.fxml");
                Game.resetGame();
            }
        }
    }

    private void updateScene() {
        fieldWithChar.setText("");
        StringBuilder letters = new StringBuilder();
        final Game game = Game.getInstance();
        for (Character usedCharacter : game.getHangman().getUsedCharacters()) {
            letters.append(usedCharacter).append("  ");
        }
        usedLetters.setText(letters.toString());
        changeImage(game.getHangman().getStatus());
        guessedLetters.setText(game.getHangman().getGuessedLetters());
    }

    private void changeImage(Status status) {
        if (Status.GUESSED.equals(status)) {
            return;
        }
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
