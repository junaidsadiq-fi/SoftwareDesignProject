package fi.tuni.weatheraccidentanalyzer.controllers;

import fi.tuni.weatheraccidentanalyzer.models.BarChartModel;
import fi.tuni.weatheraccidentanalyzer.models.LineChartModel;
import fi.tuni.weatheraccidentanalyzer.models.PieChartModel;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.File;

public class MainController {
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private Button dashboardButton;

    private final BarChartModel barChartModel = new BarChartModel();
    private final PieChartModel pieChartModel = new PieChartModel();
    private final LineChartModel lineChartModel = new LineChartModel();


    @FXML
    private void handleMonthButtonClick() {
        System.out.println("Month button clicked!");
        barChartModel.updateBarChartForMonth(barChart);
        pieChartModel.updatePieChartForMonth(pieChart);
        lineChartModel.updateLineChartForMonth(lineChart);

        setChartVisibility();
    }

    @FXML
    private void handleYearButtonClick() {
        System.out.println("Year button clicked!");
        barChartModel.updateBarChartForYear(barChart);
        pieChartModel.updatePieChartForYear(pieChart);
        lineChartModel.updateLineChartForYear(lineChart);

        setChartVisibility();

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
}