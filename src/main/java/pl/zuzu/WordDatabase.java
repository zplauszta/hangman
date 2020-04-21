package pl.zuzu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

public class WordDatabase {
    String path = "https://raw.githubusercontent.com/dwyl/english-words/master/words.txt";
    List<String> words;

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