package pl.plauszta.ui.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.plauszta.TooManyMistakesException;
import pl.plauszta.game.Game;
import pl.plauszta.game.GameMode;
import pl.plauszta.game.Status;
import pl.plauszta.ui.gui.GuiGame;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static pl.plauszta.ui.gui.controllers.SceneChanger.changeScene;

public class GameController implements Initializable {
    public static final String PLAYER_1 = "Player1";
    public static final String PLAYER_2 = "Player2";
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

        if (fieldWithChar.getText().trim().equals("") || !fieldWithChar.getText().matches("[a-z]")) {
            updateScene();
            String message = "enter the letter!";
            makeAlert(message).showAndWait();
            return;
        }

        char letter = fieldWithChar.getText().toLowerCase().charAt(0);
        game.getHangman().checkLetter(letter);

        updateScene();

        if (game.getHangman().isEnd()) {
            endGameOccurrence(event, game);
        }
    }

    private void endGameOccurrence(ActionEvent event, Game game) throws IOException {
        StringBuilder message = new StringBuilder();
        if (game.getHangman().getStatus().equals(Status.GUESSED)) {
            updateScores();

            message.append("Congrats! You guess the word ")
                    .append(game.getHangman().getWord())
                    .append(" :)");
        } else {
            message.append("Buu! You lose. The word was ").append(game.getHangman().getWord());
        }

        if (GameMode.ONE_PLAYER.equals(game.getMode())) {
            message.append("\nPlayer score: ").append(scorePlayer2);
        } else {
            message.append("\n").append(PLAYER_1).append(" score: ").append(scorePlayer1)
                    .append("\n").append(PLAYER_2).append(" score: ").append(scorePlayer2);
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
            prepareNewGame(game);
        } else {
            scorePlayer2 = 0;
            scorePlayer1 = 0;
            player2Guessing = true;
            changeScene(event, "home.fxml");
            Game.resetGame();
        }
    }

    private void prepareNewGame(Game game) {
        if (game.getMode().equals(GameMode.ONE_PLAYER)) {   //mode 1-player
            game.changeWordForHangman();
            System.out.println(game.getHangman().getWord());
            updateScene();
        } else {                                            //mode 2-player
            player2Guessing = !player2Guessing;
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("New Game");
            dialog.setHeaderText(player2Guessing ? PLAYER_1 : PLAYER_2);
            dialog.setContentText("Please enter your word or leave empty if you want random word:");

            Optional<String> word = dialog.showAndWait();

            if ("".equals(word.orElse("").trim())) {
                game.changeWordForHangman();
            } else {
                game.changeWordForHangman(word.get());
                updateScene();
            }
        }
    }

    private void updateScores() {
        if (player2Guessing) {
            scorePlayer2++;
        } else {
            scorePlayer1++;
        }
    }

    private void updateScene() {
        fieldWithChar.setText("");
        StringBuilder letters = new StringBuilder();
        final Game game = Game.getInstance();
        for (Character usedCharacter : game.getHangman().getUsedCharacters()) {
            if (usedCharacter.toString().matches("[a-z]")) {
                letters.append(usedCharacter).append("  ");
            }
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
