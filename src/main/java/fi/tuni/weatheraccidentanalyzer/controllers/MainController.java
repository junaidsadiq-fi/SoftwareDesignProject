package fi.tuni.weatheraccidentanalyzer.controllers;

import fi.tuni.weatheraccidentanalyzer.models.BarChartModel;
import fi.tuni.weatheraccidentanalyzer.models.LineChartModel;
import fi.tuni.weatheraccidentanalyzer.models.PieChartModel;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private LineChart<String, Number> lineChart;
    //@FXML
    //private Label messageLabel;

    private final BarChartModel barChartModel = new BarChartModel();
    private final PieChartModel pieChartModel = new PieChartModel();
    private final LineChartModel lineChartModel = new LineChartModel();

    @FXML
    private void handleDayButtonClick() {
        System.out.println("Day button clicked!");
    }

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
        System.out.println("Dashboard button clicked!");
    }

    @FXML
    private void handleSettingsButtonClick() {
        System.out.println("Settings button clicked!");
    }

    private void setChartVisibility() {
        //messageLabel.setVisible(false);
        pieChart.setVisible(true);
        barChart.setVisible(true);
        lineChart.setVisible(true);
    }
}