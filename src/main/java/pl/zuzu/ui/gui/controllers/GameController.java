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

public class GameController implements Initializable {
    Game game = Game.getInstance();

    @FXML
    TextField fieldWithChar;

    @FXML
    ImageView imageOfHangman;

    @FXML
    Label guessedLetters;

    @FXML
    TextArea usedLetters;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextField textField = new TextField();
        textField.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
            String newText = change.getControlNewText();
            if (newText.length() > 1) {
                return null;
            } else {
                return change;
            }
        }));

        guessedLetters.setText(Game.getInstance().getHangman().getGuessedLetters());
    }

    public void enterLetter(ActionEvent event) throws TooManyMistakesException, IOException {
        char letter = fieldWithChar.getText().charAt(0);
        int stateOfGuess = game.getHangman().checkLetter(letter);
        guessedLetters.setText(game.getHangman().getGuessedLetters());
        if (stateOfGuess == -1) {
            changeImage();
        }
        changeTextFields();

        if (game.getHangman().isEnd()) {
            displayAlert();
            changeScene(event, "home.fxml");
            game.resetGame();
        }
    }

    private void displayAlert() {
        String message;
        if (game.getHangman().getStatus().equals(Status.GUESSED)) {
            message = "Congrats! You guess the word "
                    + game.getHangman().getWord() + " :)";
        } else {
            message = "Buu! You lose. The word was "
                    + game.getHangman().getWord();
        }
        makeAlert(message).showAndWait();
    }

    private void changeTextFields() {
        fieldWithChar.setText("");
        StringBuilder letters = new StringBuilder();
        for (Character usedCharacter : game.getHangman().getUsedCharacters()) {
            letters.append(usedCharacter).append("  ");
        }
        usedLetters.setText(letters.toString());
    }

    private void changeImage() {
        final String url = imageOfHangman.getImage().getUrl();
        final int fileName = Integer.parseInt(url.substring(url.length() - 5, url.length() - 4)) + 1;
        imageOfHangman.setImage(new Image("file:/D:/projekty/hangman/target/classes/" + fileName + ".jpg"));
    }

    private Alert makeAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Important Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
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
