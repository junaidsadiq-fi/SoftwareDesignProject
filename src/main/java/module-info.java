module fi.tuni.weatheraccidentanalyzer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    exports fi.tuni.weatheraccidentanalyzer.controllers;
    opens fi.tuni.weatheraccidentanalyzer.controllers to javafx.fxml;
    exports fi.tuni.weatheraccidentanalyzer.models;
    opens fi.tuni.weatheraccidentanalyzer.models to javafx.fxml;
}