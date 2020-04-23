package pl.zuzu.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

public class WordDatabase {
    public static WordDatabase INSTANCE = new WordDatabase();

    final private String path = "https://raw.githubusercontent.com/dwyl/english-words/master/words.txt";
    private List<String> words;

    private WordDatabase() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Game already constructed");
        }
    }

    public static WordDatabase getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new WordDatabase();
        }
        return INSTANCE;
    }

    public void init() {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(path);
            URLConnection urlConnection = url.openConnection();

            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()))) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException ex) { //FIXME exact exception
            ex.printStackTrace(); //FIXME my exception throw based on ex
        }

        words = Arrays.stream(content.toString().split("\n")).collect(Collectors.toList());
    }

    public List<String> getWords() {
        return words;
    }
}
