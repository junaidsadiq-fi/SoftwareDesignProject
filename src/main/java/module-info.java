module fi.tuni.weatheraccidentanalyzer {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;

    requires java.xml;
    requires com.google.gson;

    exports fi.tuni.weatheraccidentanalyzer.controllers;
    opens fi.tuni.weatheraccidentanalyzer.controllers to javafx.fxml;
    exports fi.tuni.weatheraccidentanalyzer.models;
    opens fi.tuni.weatheraccidentanalyzer.models to javafx.fxml;
}