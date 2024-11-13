package fi.tuni.weatheraccidentanalyzer.models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

import java.io.IOException;

public class WeatherAccidentAnalyzer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WeatherAccidentAnalyzer.class.getResource("/fi/tuni/weatheraccidentanalyzer/views/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1000);

        // Load the styles file.
        String css = new File("src/main/resources/styles.css").toURI().toString();
        scene.getStylesheets().add(css);

        stage.setTitle("Weather Accindent Analyzer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}