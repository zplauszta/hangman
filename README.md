# Hangman
## Build standalone application
The standalone application can be build by executing:
- Linux:
```shell script
./mvnw javafx:jlink
```
- Windows:
```shell script
mvnw.cmd javafx:jlink
```

After command execution:
1. `target/hangman` directory contain executable application.
The application can be launched by executing file `target/hangman/bin/launcher`.
2. `target/hangman.zip` contains packaged application, ready to distribute.
