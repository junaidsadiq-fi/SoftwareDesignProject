package fi.tuni.weatheraccidentanalyzer.models;

public class ParameterHelper {
    public ParameterHelper () {}

    public String getWeatherParamNameFromCode(String code) {
        switch (code) {
            case "t2m":
                return "Temperatures";
            case "ws_10min":
                return "windSpeeds";
            case "wg_10min":
                return "windGusts";
            case "wd_10min":
                return "windDirections";
            case "rh":
                return "humidities";
            case "td":
                return "dewPoints";
            case "r_1h":
                return "rainAmounts";
            case "ri_10min":
                return "rainIntensities";
            case "snow_aws":
                return "snowDepths";
            case "p_sea":
                return "pressures";
            case "vis":
                return "visibilities";
            case "n_man":
                return "cloudCoverages";
            case "wawa":
                return "weatherPhenomena";
            default:
                return "";
        }
    }
}
