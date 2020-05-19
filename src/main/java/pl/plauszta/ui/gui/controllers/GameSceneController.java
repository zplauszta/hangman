package pl.plauszta.ui.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.plauszta.game.Game;
import pl.plauszta.game.GameMode;
import pl.plauszta.game.Status;
import pl.plauszta.ui.gui.GuiGame;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static pl.plauszta.ui.gui.controllers.SceneChanger.changeScene;

public class GameSceneController implements Initializable {
    public static final String PLAYER_1 = "Player1";
    public static final String PLAYER_2 = "Player2";

    @FXML
    public VBox buttons;

    @FXML
    public VBox buttonsPol;

    @FXML
    private ImageView imageOfHangman;

    @FXML
    private Label guessedLetters;


    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private boolean player2Guessing = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guessedLetters.setText(Game.getInstance().getHangman().getGuessedLetters());
        setNeededButtons();
    }

    private void setNeededButtons() {
        if (Game.getInstance().getWordDatabase().isEnglishVersion()) {
            buttons.setVisible(true);
            buttonsPol.setVisible(false);
        } else {
            buttons.setVisible(false);
            buttonsPol.setVisible(true);
        }
    }

    public void buttonClicked(ActionEvent event) throws IOException {
        final Game game = Game.getInstance();

        char letter = ((Button) event.getSource()).getText().toLowerCase().charAt(0);
        game.getHangman().checkLetter(letter);
        ((Button) event.getSource()).setDisable(true);
        updateScene();

        if (game.getHangman().isEnd()) {
            endGameOccurrence(event, game);
        }
    }

    private void endGameOccurrence(ActionEvent event, Game game) throws IOException {
        StringBuilder message = makeEndMessage(game);
        Alert alert = endGameAlert(message);
        ButtonType buttonPlayAgain = new ButtonType("Play!");
        ButtonType buttonBackToHome = new ButtonType("Back to home");
        alert.getButtonTypes().setAll(buttonPlayAgain, buttonBackToHome);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonPlayAgain) {
            prepareNewGame(game);
        } else {
            cleanAndBackHome(event);
        }
    }

    private void cleanAndBackHome(ActionEvent event) throws IOException {
        scorePlayer2 = 0;
        scorePlayer1 = 0;
        player2Guessing = true;
        Game.resetGame();
        changeScene(event, "homeScene.fxml");
    }

    private StringBuilder makeEndMessage(Game game) {
        StringBuilder message = new StringBuilder();
        if (game.getHangman().getStatus().equals(Status.GUESSED)) {
            updateScores();
            message.append("Congrats! You guess the word ");
        } else {
            message.append("Buu! You lose. The word was ");
        }
        message.append(game.getHangman().getWord());

        if (GameMode.ONE_PLAYER.equals(game.getMode())) {
            message.append("\nPlayer score: ").append(scorePlayer2);
        } else {
            message.append("\n").append(PLAYER_1).append(" score: ").append(scorePlayer1)
                    .append("\n").append(PLAYER_2).append(" score: ").append(scorePlayer2);
        }
        return message;
    }

    private Alert endGameAlert(StringBuilder message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("End of game");
        alert.setHeaderText(null);
        alert.setContentText(message + "\nDo you want guess a word again?");
        return alert;
    }

    private void prepareNewGame(Game game) {
        if (game.getMode().equals(GameMode.ONE_PLAYER)) {   //mode 1-player
            game.changeWordForHangman();
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
                game.changeWordForHangman(word.toString());
            }
        }
        unlockButtons();
        updateScene();
    }

    private void unlockButtons() {
        if (Game.getInstance().getWordDatabase().isEnglishVersion()) {
            for (Node child : buttons.getChildren()) {
                for (Node node : ((HBox) child).getChildren()) {
                    node.setDisable(false);
                }
            }
        } else {
            for (Node child : buttonsPol.getChildren()) {
                for (Node node : ((HBox) child).getChildren()) {
                    node.setDisable(false);
                }
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
        final Game game = Game.getInstance();
        changeImage(game.getHangman().getStatus());
        guessedLetters.setText(game.getHangman().getGuessedLetters());
    }

    private void changeImage(Status status) {
        if (Status.GUESSED.equals(status)) {
            return;
        }
        String name = status.ordinal() + ".jpg";
        final URL resource = Objects.requireNonNull(GuiGame.class.getClassLoader().getResource(name), name + " resource not found!");
        imageOfHangman.setImage(new Image(resource.toString()));
    }
}
