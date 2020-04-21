import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    public void startOnePlayerGame(ActionEvent event) throws IOException {
        final Button clickedButton = (Button) event.getTarget();
        Stage stage = (Stage) clickedButton.getScene().getWindow();
        final Pane gamePane = FXMLLoader.load(HomeController.class.getClassLoader().getResource("game.fxml"));
        stage.getScene().setRoot(gamePane);

        stage.show();
    }
}
