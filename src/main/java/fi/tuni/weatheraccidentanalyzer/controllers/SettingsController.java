package fi.tuni.weatheraccidentanalyzer.controllers;

import fi.tuni.weatheraccidentanalyzer.models.SettingsModel;
import fi.tuni.weatheraccidentanalyzer.models.LocationItem;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.List;

public class SettingsController {
    @FXML
    private VBox checkBoxContainer;

    @FXML
    private CheckBox temperatureCheckBox;
    @FXML
    private CheckBox windSpeedCheckBox;
    @FXML
    private CheckBox windGustCheckBox;
    @FXML
    private CheckBox windDirectionCheckBox;
    @FXML
    private CheckBox humidityCheckBox;
    @FXML
    private CheckBox dewPointCheckBox;
    @FXML
    private CheckBox rainAmountCheckBox;
    @FXML
    private CheckBox rainIntensityCheckBox;
    @FXML
    private CheckBox snowDepthCheckBox;
    @FXML
    private CheckBox pressureCheckBox;
    @FXML
    private CheckBox visibilityCheckBox;
    @FXML
    private CheckBox cloudCoverageCheckBox;
    @FXML
    private CheckBox weatherPhenomenaCheckBox;
    @FXML
    private ComboBox<LocationItem> locationSelection;

    // USer can select only 3 of the weather options.
    private static final int MAX_SELECTION = 3;
    private SettingsModel settingsModel = new SettingsModel();

    public void setSettingsModel(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
    }

    // This method is called just before the settings view becomes active.
    // Collect all the checkboxes into a list to make operations easier.
    @FXML
    public void initialize() {
        // Populate the location selection ComboBox with LocationItem
        // objects so we can later easily access the API specific codes
        // for the locations
        locationSelection.setItems(FXCollections.observableArrayList(
                new LocationItem("Uusimaa", "MK01", "101022"),
                new LocationItem("Varsinais-Suomi", "MK02", "100947"),
                new LocationItem("Satakunta", "MK04", "101267"),
                new LocationItem("Kanta-Häme", "MK05", "101150"),
                new LocationItem("Pirkanmaa", "MK06", "101310"),
                new LocationItem("Päijät-Häme", "MK07", "101185"),
                new LocationItem("Kymenlaakso", "MK08", "101231"),
                new LocationItem("Etelä-Karjala", "MK09", "101246"),
                new LocationItem("Etelä-Savo", "MK10", "101418"),
                new LocationItem("Pohjois-Savo", "MK11", "101572"),
                new LocationItem("Pohjois-Karjala", "MK12", "101603"),
                new LocationItem("Keski-Suomi", "MK13", "101339"),
                new LocationItem("Etelä-Pohjanmaa", "MK14", "101289"),
                new LocationItem("Pohjanmaa", "MK15", "101485"),
                new LocationItem("Keski-Pohjanmaa", "MK16", "101528"),
                new LocationItem("Lappi", "MK19", "101950"),
                new LocationItem("Ahvenanmaa", "MK21", "100917")
        ));

        // Add a listener to handle location selection change
        locationSelection.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Update the SettingsModel with the selected location
                    settingsModel.setLocationSelection(newValue);
                    //System.out.println("Selected location: " + newValue.getDisplayName() + " (" + newValue.getWeatherApiCode() + ")");
                }
            }
        );

        List<CheckBox> checkBoxes = List.of(
                temperatureCheckBox, windSpeedCheckBox, windGustCheckBox, windDirectionCheckBox,
                humidityCheckBox, dewPointCheckBox, rainAmountCheckBox, rainIntensityCheckBox,
                snowDepthCheckBox, pressureCheckBox, visibilityCheckBox, cloudCoverageCheckBox,
                weatherPhenomenaCheckBox
        );

        // Add listeners for the buttons to check how many buttons
        // are selected and if we need to disable the checkboxes.
        for (CheckBox checkBox : checkBoxes) {
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> handleSelection(checkBoxes));
        }

        // Store all the states to the SettingsModel
        setSelection();
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
        setSelection();
    }

    private void setSelection() {
        // Store the selections to the settingsModel
        settingsModel.setTemperatureSelected(temperatureCheckBox.isSelected());
        settingsModel.setWindSpeedSelected(windSpeedCheckBox.isSelected());
        settingsModel.setWindGustSelected(windGustCheckBox.isSelected());
        settingsModel.setWindDirectionSelected(windDirectionCheckBox.isSelected());
        settingsModel.setHumiditySelected(humidityCheckBox.isSelected());
        settingsModel.setDewPointSelected(dewPointCheckBox.isSelected());
        settingsModel.setRainAmountSelected(rainAmountCheckBox.isSelected());
        settingsModel.setRainIntensitySelected(rainIntensityCheckBox.isSelected());
        settingsModel.setSnowDepthSelected(snowDepthCheckBox.isSelected());
        settingsModel.setPressureSelected(pressureCheckBox.isSelected());
        settingsModel.setVisibilitySelected(visibilityCheckBox.isSelected());
        settingsModel.setCloudCoverageSelected(cloudCoverageCheckBox.isSelected());
        settingsModel.setWeatherPhenomenaSelected(weatherPhenomenaCheckBox.isSelected());
    }

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }
}
