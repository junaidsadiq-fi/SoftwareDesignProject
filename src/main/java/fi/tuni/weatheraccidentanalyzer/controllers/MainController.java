package fi.tuni.weatheraccidentanalyzer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import fi.tuni.weatheraccidentanalyzer.controllers.TrafficAccidentsController;

public class MainController {
    @FXML
    private void handleDayButtonClick() {
        System.out.println("Day button clicked!");
    }

    @FXML
    private void handleMonthButtonClick() {
        System.out.println("Month button clicked!");
        TrafficAccidentsController trafficAccidentsController = new TrafficAccidentsController();
    }

    @FXML
    private void handleYearButtonClick() {
        System.out.println("Year button clicked!");
    }

    @FXML
    private void handleDashboardButtonClick() {
        System.out.println("Dashboard button clicked!");
    }

    @FXML
    private void handleSettingsButtonClick() {
        System.out.println("Settings button clicked!");
    }
}