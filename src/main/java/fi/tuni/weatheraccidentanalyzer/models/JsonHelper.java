package fi.tuni.weatheraccidentanalyzer.models;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonHelper {
    private List<Double> dailyAverages;
    private List<Double> monthlyAverages;
    private List<String> selectedParameters;

    public JsonHelper(List<String> selectedParameters) {
        this.selectedParameters = selectedParameters;
    };

    public void calculateDailyAverages(String parameter) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("weather_data.json");
        if (inputStream == null) {
            throw new IOException("Resource 'weather_data.json' not found in classpath");
        }

        // Read the JSON file content into a string
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        // Parse JSON using Gson
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        // Extract the "Temperatures" object
        JsonObject temperaturesJson = jsonObject.getAsJsonObject(parameter);

        // Convert to a Map<String, Double>
        Map<String, Double> temperatures = new HashMap<>();
        for (String key : temperaturesJson.keySet()) {
            temperatures.put(key, temperaturesJson.get(key).getAsDouble());
        }

        // Group temperatures by day
        Map<String, List<Double>> groupedByDay = temperatures.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().split("T")[0], // Extract the date
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        // Calculate the average for each day
        List<Double> dailyAverages = groupedByDay.values().stream()
                .map(temps -> temps.stream().mapToDouble(Double::doubleValue).average().orElse(0.0))
                .collect(Collectors.toList());

        // Print the daily averages
        this.dailyAverages = dailyAverages;
        System.out.println(dailyAverages);
    }

    public List<Double> getDailyAverages() {
        return this.dailyAverages;
    }

    public void calculateMonthlyAverages(String parameter) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("weather_data.json");
        if (inputStream == null) {
            throw new IOException("Resource 'weather_data.json' not found in classpath");
        }

        // Read the JSON file content into a string
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        // Parse JSON using Gson
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        // Extract the "Temperatures" object
        JsonObject temperaturesJson = jsonObject.getAsJsonObject(parameter);

        // Convert to a Map<String, Double>
        Map<String, Double> temperatures = new HashMap<>();
        for (String key : temperaturesJson.keySet()) {
            temperatures.put(key, temperaturesJson.get(key).getAsDouble());
        }

        // Group temperatures by month and sort the keys to ensure correct month order
        Map<String, List<Double>> groupedByMonth = temperatures.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().substring(0, 7), // Extract the "YYYY-MM" part
                        TreeMap::new, // Use a TreeMap to keep keys in sorted order
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        // Calculate the average for each month in sorted order
        List<Double> monthlyAverages = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : groupedByMonth.entrySet()) {
            List<Double> monthlyTemps = entry.getValue();
            double average = monthlyTemps.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            monthlyAverages.add(average);
        }

        // Store the monthly averages
        this.monthlyAverages = monthlyAverages;
        System.out.println("Monthly Averages: " + monthlyAverages);
    }

    public List<Double> getMonthlyAverages() {
        return this.monthlyAverages;
    }
}
