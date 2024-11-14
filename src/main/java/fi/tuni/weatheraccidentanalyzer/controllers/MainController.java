package fi.tuni.weatheraccidentanalyzer.controllers;

import fi.tuni.weatheraccidentanalyzer.models.*;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainController {
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private Button dashboardButton;
    @FXML
    private ComboBox<String> yearComboBox;

    private final BarChartModel barChartModel = new BarChartModel();
    private final PieChartModel pieChartModel = new PieChartModel();
    private final LineChartModel lineChartModel = new LineChartModel();

    //private SettingsController settingsController;
    private static SettingsModel settingsModel = new SettingsModel();
    private WeatherDataModel weatherModel = new WeatherDataModel();
    //private final TrafficAccidentsController accidentController = new TrafficAccidentsController();


    /*@FXML
    private void handleMonthButtonClick() {
        System.out.println("Month button clicked!");
        barChartModel.updateBarChartForMonth(barChart);
        pieChartModel.updatePieChartForMonth(pieChart);
        lineChartModel.updateLineChartForMonth(lineChart);

        setChartVisibility();
    }*/

    /*@FXML
    private void handleYearButtonClick() {
        System.out.println("Year button clicked!");
        barChartModel.updateBarChartForYear(barChart);
        pieChartModel.updatePieChartForYear(pieChart);
        lineChartModel.updateLineChartForYear(lineChart);

        setChartVisibility();

    }*/

    @FXML
    private void handleFetchButtonClick() {
        // Take value from selection field and settings checkboxes.
        // TrafficAccidentsController(Integer year, String injuryType, List<String> roadUserCodes, String area)
        String selectedYearStr = yearComboBox.getValue();
        int selectedYear = Integer.parseInt(selectedYearStr);

        String injuryType = "louk";
        List<String> roadUsers = Arrays.asList("HA_SS", "KA_SS");
        String area = "MK06";
        System.out.println("Fetching traffic data...");
        TrafficAccidentsController accidentController = new TrafficAccidentsController(selectedYear, injuryType, roadUsers, area);
        YearlyAccidentData yearlyAccData = accidentController.getTrafficAccidents();
        System.out.println("... done");
        System.out.println("Fetching weather data...");
        WeatherDataController weatherController = new WeatherDataController(weatherModel);
        weatherController.run();
        System.out.println("... done");
    }

    @FXML
    private void handleDashboardButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fi/tuni/weatheraccidentanalyzer/views/main-view.fxml"));
            Parent mainViewRoot = loader.load();

            Stage stage = (Stage) dashboardButton.getScene().getWindow();
            stage.setScene(new Scene(mainViewRoot));

            String css = new File("src/main/resources/styles.css").toURI().toString();
            stage.getScene().getStylesheets().add(css);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettingsButtonClick() {
        System.out.println("Settings button clicked!");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fi/tuni/weatheraccidentanalyzer/views/settings-view.fxml"));
            Parent settingsRoot = loader.load();

            SettingsController settingsController = loader.getController();
            settingsController.setSettingsModel(settingsModel);

            Scene settingsScene = new Scene(settingsRoot);
            String css = new File("src/main/resources/styles.css").toURI().toString();
            settingsScene.getStylesheets().add(css);

            Stage stage = (Stage) dashboardButton.getScene().getWindow();
            stage.setScene(settingsScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setChartVisibility() {
        //messageLabel.setVisible(false);
        pieChart.setVisible(true);
        barChart.setVisible(true);
        lineChart.setVisible(true);
    }

    public static SettingsModel getSettingsModel() {
        return settingsModel;
    }

    public static void setSettingsModel(SettingsModel model) {
        settingsModel = model;
    }
}