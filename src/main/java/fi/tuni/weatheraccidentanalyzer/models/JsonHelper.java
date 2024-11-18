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

    public JsonHelper() {};

    public List<Double> calculateMonthlyAverages(String parameter) throws IOException {
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
        JsonObject dataJson = jsonObject.getAsJsonObject(parameter);

        ////////////////////// DEBUG PRINT
        /*if (temperaturesJson == null) {
            System.out.println("The JSON object for parameter '" + parameter + "' is null.");
            return Collections.emptyList();
        } else if (temperaturesJson.size() == 0) {
            System.out.println("The JSON object for parameter '" + parameter + "' is empty.");
            return Collections.emptyList();
        } else {
            System.out.println("The JSON object for parameter '" + parameter + "' has data:");
            System.out.println(temperaturesJson.toString()); // Print the JSON content for inspection
        }*/
        //////////////////////////////

        // Convert to a Map<String, Double>
        Map<String, Double> data = new HashMap<>();
        for (String key : dataJson.keySet()) {
            double value = dataJson.get(key).getAsDouble();
            if (!Double.isNaN(value)) { // Only add the value if it's not NaN
                data.put(key, value);
            }
        }

        // Group temperatures by month and sort the keys to ensure correct month order
        Map<String, List<Double>> groupedByMonth = data.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().substring(0, 7), // Extract the "YYYY-MM" part
                        TreeMap::new, // Use a TreeMap to keep keys in sorted order
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        // Calculate the average for each month in sorted order
        List<Double> monthlyAverages = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : groupedByMonth.entrySet()) {
            List<Double> monthlyData = entry.getValue();
            double average = monthlyData.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            monthlyAverages.add(average);
        }

        System.out.println("Monthly Averages: " + monthlyAverages);
        return monthlyAverages;
    }

    /*public List<Double> getTempMonthlyAverages() {
        return this.temperatureMonthlyAverages;
    }*/
}
