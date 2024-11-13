package fi.tuni.weatheraccidentanalyzer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.List;

public class SettingsController {
    @FXML
    private VBox checkBoxContainer;

    @FXML
    private CheckBox temperatureCheckBox;
    @FXML
    private CheckBox windspeedCheckBox;
    @FXML
    private CheckBox humidityCheckBox;
    @FXML
    private CheckBox dewPointCheckBox;
    @FXML
    private CheckBox rainfallCheckBox;
    @FXML
    private CheckBox rainIntensityCheckBox;
    @FXML
    private CheckBox snowDepthCheckBox;
    @FXML
    private CheckBox visibilityCheckBox;
    @FXML
    private CheckBox weatherConditionsCheckBox;

    // USer can select only 3 of the weather options.
    private static final int MAX_SELECTION = 3;

    // This method is called just before the settings view becomes active.
    // Collect all the checkboxes into a list to make operations easier.
    @FXML
    public void initialize() {
        List<CheckBox> checkBoxes = List.of(
                temperatureCheckBox, windspeedCheckBox, humidityCheckBox,
                dewPointCheckBox, rainfallCheckBox, rainIntensityCheckBox,
                snowDepthCheckBox, visibilityCheckBox, weatherConditionsCheckBox
        );

        // Add listeners for the buttons to check how many buttons
        // are selected and if we need to disable the checkboxes.
        for (CheckBox checkBox : checkBoxes) {
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> handleSelection(checkBoxes));
        }
    }

    // Handle checkboxes. Disable all the rest checkboxes if MAX_SELECTION
    // amount is selected. Enable them if less are selected.
    private void handleSelection(List<CheckBox> checkBoxes) {
        long selectedCount = checkBoxes.stream().filter(CheckBox::isSelected).count();

        // Enable or disable checkboxes based on the selected count
        if (selectedCount >= MAX_SELECTION) {
            // Disable all unchecked checkboxes
            checkBoxes.stream()
                    .filter(cb -> !cb.isSelected())
                    .forEach(cb -> cb.setDisable(true));
        } else {
            // Enable all checkboxes if less than MAX_SELECTION is selected
            checkBoxes.forEach(cb -> cb.setDisable(false));
        }
    }
}
