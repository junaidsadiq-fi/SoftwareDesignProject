package fi.tuni.weatheraccidentanalyzer.models;

public class SettingsModel {
    private boolean temperatureSelected;
    private boolean windspeedSelected;
    private boolean humiditySelected;
    private boolean dewPointSelected;
    private boolean rainfallSelected;
    private boolean rainIntensitySelected;
    private boolean snowDepthSelected;
    private boolean visibilitySelected;
    private boolean weatherConditionsSelected;

    // Getters and setters for each checkbox state
    public boolean isTemperatureSelected() {
        return temperatureSelected;
    }

    public void setTemperatureSelected(boolean temperatureSelected) {
        this.temperatureSelected = temperatureSelected;
    }

    public boolean isWindspeedSelected() {
        return windspeedSelected;
    }

    public void setWindspeedSelected(boolean windspeedSelected) {
        this.windspeedSelected = windspeedSelected;
    }

    public boolean isHumiditySelected() {
        return humiditySelected;
    }

    public void setHumiditySelected(boolean humiditySelected) {
        this.humiditySelected = humiditySelected;
    }
}
