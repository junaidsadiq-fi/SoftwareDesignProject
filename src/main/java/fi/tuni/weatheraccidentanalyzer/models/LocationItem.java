package fi.tuni.weatheraccidentanalyzer.models;

public class LocationItem {
    private String displayName;
    private String trafficApiCode;
    private String weatherApiCode;

    public LocationItem(String displayName, String trafficApiCode, String weatherApiCode) {
        this.displayName = displayName;
        this.trafficApiCode = trafficApiCode;
        this.weatherApiCode = weatherApiCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTrafficApiCode() {
        return trafficApiCode;
    }

    public String getWeatherApiCode() {
        return weatherApiCode;
    }

    @Override
    public String toString() {
        return displayName; // This ensures the ComboBox displays the name
    }
}
