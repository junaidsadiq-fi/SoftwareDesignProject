package fi.tuni.weatheraccidentanalyzer.models;

import java.util.ArrayList;
import java.util.List;

public class SettingsModel {
    private boolean temperatureSelected;
    private boolean windSpeedSelected;
    private boolean windGustSelected;
    private boolean windDirectionSelected;
    private boolean humiditySelected;
    private boolean dewPointSelected;
    private boolean rainAmountSelected;
    private boolean rainIntensitySelected;
    private boolean snowDepthSelected;
    private boolean pressureSelected;
    private boolean visibilitySelected;
    private boolean cloudCoverageSelected;
    private boolean weatherPhenomenaSelected;
    private LocationItem locationSelection = new LocationItem("", "", "");
    private ParameterHelper parameterHelper = new ParameterHelper();

    // Getters and setters for each checkbox state

    public boolean isTemperatureSelected() {
        return temperatureSelected;
    }

    public void setTemperatureSelected(boolean temperatureSelected) {
        this.temperatureSelected = temperatureSelected;
    }

    public boolean isWindSpeedSelected() {
        return windSpeedSelected;
    }

    public void setWindSpeedSelected(boolean windSpeedSelected) {
        this.windSpeedSelected = windSpeedSelected;
    }

    public boolean isWindGustSelected() {
        return windGustSelected;
    }

    public void setWindGustSelected(boolean windGustSelected) {
        this.windGustSelected = windGustSelected;
    }

    public boolean isWindDirectionSelected() {
        return windDirectionSelected;
    }

    public void setWindDirectionSelected(boolean windDirectionSelected) {
        this.windDirectionSelected = windDirectionSelected;
    }

    public boolean isHumiditySelected() {
        return humiditySelected;
    }

    public void setHumiditySelected(boolean humiditySelected) {
        this.humiditySelected = humiditySelected;
    }

    public boolean isDewPointSelected() {
        return dewPointSelected;
    }

    public void setDewPointSelected(boolean dewPointSelected) {
        this.dewPointSelected = dewPointSelected;
    }

    public boolean isRainAmountSelected() {
        return rainAmountSelected;
    }

    public void setRainAmountSelected(boolean rainAmountSelected) {
        this.rainAmountSelected = rainAmountSelected;
    }

    public boolean isRainIntensitySelected() {
        return rainIntensitySelected;
    }

    public void setRainIntensitySelected(boolean rainIntensitySelected) {
        this.rainIntensitySelected = rainIntensitySelected;
    }

    public boolean isSnowDepthSelected() {
        return snowDepthSelected;
    }

    public void setSnowDepthSelected(boolean snowDepthSelected) {
        this.snowDepthSelected = snowDepthSelected;
    }

    public boolean isPressureSelected() {
        return pressureSelected;
    }

    public void setPressureSelected(boolean pressureSelected) {
        this.pressureSelected = pressureSelected;
    }

    public boolean isVisibilitySelected() {
        return visibilitySelected;
    }

    public void setVisibilitySelected(boolean visibilitySelected) {
        this.visibilitySelected = visibilitySelected;
    }

    public boolean isCloudCoverageSelected() {
        return cloudCoverageSelected;
    }

    public void setCloudCoverageSelected(boolean cloudCoverageSelected) {
        this.cloudCoverageSelected = cloudCoverageSelected;
    }

    public boolean isWeatherPhenomenaSelected() {
        return weatherPhenomenaSelected;
    }

    public void setWeatherPhenomenaSelected(boolean weatherPhenomenaSelected) {
        this.weatherPhenomenaSelected = weatherPhenomenaSelected;
    }

    public void setLocationSelection(LocationItem location) {
        this.locationSelection = location;
    }

    public LocationItem getLocationSelection() {
        return this.locationSelection;
    }

    public List<String> getSelected() {
        List<String> selectedCodes = new ArrayList<>();

        if (temperatureSelected) {
            selectedCodes.add("t2m");
        }
        if (windSpeedSelected) {
            selectedCodes.add("ws_10min");
        }
        if (windGustSelected) {
            selectedCodes.add("wg_10min");
        }
        if (windDirectionSelected) {
            selectedCodes.add("wd_10min");
        }
        if (humiditySelected) {
            selectedCodes.add("rh");
        }
        if (dewPointSelected) {
            selectedCodes.add("td");
        }
        if (rainAmountSelected) {
            selectedCodes.add("r_1h");
        }
        if (rainIntensitySelected) {
            selectedCodes.add("ri_10min");
        }
        if (snowDepthSelected) {
            selectedCodes.add("snow_aws");
        }
        if (pressureSelected) {
            selectedCodes.add("p_sea");
        }
        if (visibilitySelected) {
            selectedCodes.add("vis");
        }
        if (cloudCoverageSelected) {
            selectedCodes.add("n_man");
        }
        if (weatherPhenomenaSelected) {
            selectedCodes.add("wawa");
        }

        return selectedCodes;
    }
}
