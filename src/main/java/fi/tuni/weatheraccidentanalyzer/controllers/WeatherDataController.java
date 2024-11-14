package fi.tuni.weatheraccidentanalyzer.controllers;

import fi.tuni.weatheraccidentanalyzer.models.WeatherDataModel;

import java.util.List;

public class WeatherDataController {

    private final WeatherDataModel model;

    public WeatherDataController(WeatherDataModel model) {
        this.model = model;
    }

    public void run() {
        String stationId = "101289"; // Use "koko maa" to get all stations' data
        int year = 2020;
        List<String> parameters = List.of("t2m", "ws_10min", "wg_10min", "wd_10min", "rh", "td", "r_1h", "ri_10min",
                                          "snow_aws", "p_sea", "vis", "n_man", "wawa");
        
        processStationData(stationId, year, parameters); // Process single station
        model.saveResultsToJson();
        System.out.println("\nData saved for station " + stationId);
    }

    private void processStationData(String stationId, int year, List<String> parameters) {
        List<String> urls = model.buildUrls(stationId, year, parameters);
        int totalTasks = urls.size();
        int completedTasks = 0;

        // Process each URL and update the loading bar
        for (String url : urls) {
            try {
                String xmlContent = model.sendApiRequest(url);
                model.parseAndCategorizeXML(xmlContent);  // Changed to match the latest model method
                completedTasks++;
                displayProgress(completedTasks, totalTasks); // Update loading bar
            } catch (Exception e) {
            }
        }
        System.out.println();
    }

    // Simple text-based loading bar
    private void displayProgress(int completed, int total) {
        int barWidth = 50; // Width of the loading bar
        int progress = (int) ((double) completed / total * barWidth);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barWidth; i++) {
            if (i < progress) {
                bar.append("=");
            } else {
                bar.append(" ");
            }
        }
        bar.append("] ");
        int percent = (int) ((double) completed / total * 100);
        bar.append(percent).append("% Complete");

        System.out.print("\r" + bar.toString()); // Print on the same line
    }
}
