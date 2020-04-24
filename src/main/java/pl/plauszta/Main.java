package pl.plauszta;

import javafx.application.Application;
import pl.plauszta.game.WordDatabase;
import pl.plauszta.ui.gui.GuiGame;

import java.util.*;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        //download a database
        WordDatabase.getInstance().setEnglishVersion(false);
        WordDatabase.getInstance().init();

        //ConsoleUi consoleUi = new ConsoleUi();
        //ConsoleUi.gameLoop();

        Application.launch(GuiGame.class);

        System.out.println("Goodbye");
        sc.close();
    }


}