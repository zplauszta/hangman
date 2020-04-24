package pl.plauszta.game;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class WordDatabase {
    private static WordDatabase INSTANCE = new WordDatabase();

    private static final String pathEnglishWords = "https://raw.githubusercontent.com/dwyl/english-words/master/words.txt";
    private static final String pathPolishWords = "slowa.txt";

    private List<String> words = new ArrayList<>();
    private boolean englishVersion = true;

    private WordDatabase() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Game already constructed");
        }
    }

    public static WordDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WordDatabase();
        }
        return INSTANCE;
    }

    public void init() {
        if (englishVersion) {
            downloadWordsFromUrl(pathEnglishWords);
        } else {
            downloadWordsFromFile(pathPolishWords);
        }

    }

    private void downloadWordsFromFile(String pathPolishWords) {
        final URL resource = WordDatabase.class.getClassLoader().getResource(pathPolishWords);
        try {
            final Path path = Paths.get(resource.toURI());
            try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    words.add(line);
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private void downloadWordsFromUrl(String pathEnglishWords) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(pathEnglishWords);
            URLConnection urlConnection = url.openConnection();

            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()))) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        words = Arrays.stream(content.toString().split("\n")).collect(Collectors.toList());
    }

    public List<String> getWords() {
        return words;
    }

    public void setEnglishVersion(boolean englishVersion) {
        this.englishVersion = englishVersion;
    }

    public boolean isEnglishVersion() {
        return englishVersion;
    }
}
