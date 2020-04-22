module hangman {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    exports pl.zuzu.ui.gui to javafx.graphics;
    exports pl.zuzu.ui.gui.controllers to javafx.fxml;
}