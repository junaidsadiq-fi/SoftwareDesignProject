package mavenproject.weatherapi;

import mavenproject.weatherapi.WeatherDataModel;

import java.util.List;

public class WeatherDataController {

    private final WeatherDataModel model;

    public WeatherDataController(WeatherDataModel model) {
        this.model = model;
    }

    public void run() {
        String stationId = "101022"; // Set "koko maa" to get all stations' data
        int year = 2023;
        List<String> parameters = List.of("t2m", "ws_10min", "wg_10min", "wd_10min", "rh", "td", "r_1h", "ri_10min",
                                          "snow_aws", "p_sea", "vis", "n_man", "wawa");

        // Check if stationId is "koko maa" and fetch data for all stations
        if (stationId.equals("koko maa")) {
            for (String individualStationId : model.getStationMap().values()) {
                processStationData(individualStationId, year, parameters);
            }
            model.calculateAndSaveAverages(); // Calculate averages for all stations
        } else {
            processStationData(stationId, year, parameters); // Process single station
            model.saveResultsToJson();
        }
    }

    private void processStationData(String stationId, int year, List<String> parameters) {
        // Build URLs and fetch data for the specified station
        List<String> urls = model.buildUrls(stationId, year, parameters);
        for (String url : urls) {
            try {
                String xmlContent = model.sendApiRequest(url);
                model.parseAndCategorizeXML(xmlContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
