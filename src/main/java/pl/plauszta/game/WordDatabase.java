package pl.plauszta.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WordDatabase {
    private static WordDatabase instance = new WordDatabase();

    private static final String PATH_ENGLISH_WORDS = "https://raw.githubusercontent.com/dwyl/english-words/master/words.txt";
    private static final String PATH_POLISH_WORDS = "slowa.txt";

    private List<String> words;
    private boolean englishVersion = true;

    private WordDatabase() {
        if (instance != null) {
            throw new IllegalStateException("Game already constructed");
        }
    }

    public static WordDatabase getInstance() {
        if (instance == null) {
            instance = new WordDatabase();
        }
        return instance;
    }

    public void init() {
        if (englishVersion) {
            downloadWordsFromUrl(PATH_ENGLISH_WORDS);
        } else {
            downloadWordsFromFile(PATH_POLISH_WORDS);
        }

    }

    private void downloadWordsFromFile(String pathPolishWords) {
        final URL resource = WordDatabase.class.getClassLoader().getResource(pathPolishWords);
        try {
            final Path path = Paths.get(resource.toURI());
            words = readWordsFromFile(path);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private List<String> readWordsFromFile(Path path) {
        List<String> list = new ArrayList<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private void downloadWordsFromUrl(String pathEnglishWords) {
        String content;
        try {
            URL url = new URL(pathEnglishWords);
            URLConnection urlConnection = url.openConnection();
            content = readWordsFromUrl(urlConnection);
            words = Arrays.stream(content.split("\n")).collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String readWordsFromUrl(URLConnection urlConnection) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public List<String> getWords() {
        return words;
    }

    public void setEnglishVersion(boolean englishVersion) {
        if (englishVersion == this.englishVersion) {
            return;
        }

        this.englishVersion = englishVersion;

        if (englishVersion) {
            downloadWordsFromUrl(PATH_ENGLISH_WORDS);
        } else {
            downloadWordsFromFile(PATH_POLISH_WORDS);
        }
    }

    public boolean isEnglishVersion() {
        return englishVersion;
    }
}
