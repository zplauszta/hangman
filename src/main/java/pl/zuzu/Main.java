package pl.zuzu;

import javafx.application.Application;
import pl.zuzu.game.WordDatabase;
import pl.zuzu.ui.gui.GuiGame;

import java.util.*;

public class Main {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        //download a database
        WordDatabase.getInstance().init();
        //ConsoleUi.gameLoop();

        Application.launch(GuiGame.class);

        System.out.println("Goodbye");
        sc.close();
    }


}