package mavenproject.weatherapi;

import mavenproject.weatherapi.WeatherDataModel;
import mavenproject.weatherapi.WeatherDataView;

import java.util.List;
import java.util.Map;

public class WeatherDataController {

    private final WeatherDataModel model;
    private final WeatherDataView view;

    public WeatherDataController(WeatherDataModel model, WeatherDataView view) {
        this.model = model;
        this.view = view;
    }

    public void run() {
        // Fetch available stations and display them for user selection
        Map<String, String> stationMap = model.getStationMap();
        String stationId = view.askForStationId(stationMap);

        int year = view.askForYear();

        // Define parameters directly in the controller
        List<String> parameters = List.of("t2m", "ws_10min", "wg_10min", "wd_10min", "rh", "td", "r_1h", "ri_10min",
                                          "snow_aws", "p_sea", "vis", "n_man", "wawa");

        // Build URLs and fetch data
        List<String> urls = model.buildUrls(stationId, year, parameters);
        for (String url : urls) {
            try {
                String xmlContent = model.sendApiRequest(url);
                model.parseAndCategorizeXML(xmlContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Save the results to JSON
        model.saveResultsToJson();
    }
}
