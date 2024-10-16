package fi.tuni.weatheraccidentanalyzer.controllers;

import fi.tuni.weatheraccidentanalyzer.models.TrafficAccidents;
import fi.tuni.weatheraccidentanalyzer.service.TrafficService;

import org.json.JSONArray;
import org.json.JSONObject;

public class TrafficAccidentsController {
  private TrafficAccidents trafficAccidents;
  private TrafficService trafficService;
  JSONObject obj = new JSONObject("{\"name\": \"John\"}");
  public TrafficAccidentsController(String period) {
    trafficService = new TrafficService();
    
    JSONObject jsonObject = new JSONObject(trafficService.getTrafficData(period));

    // Extract the "value" array
    JSONArray valueArray = jsonObject.getJSONArray("value");

    // Get the first value from the array
    Object accidentCount = valueArray.get(0);

    trafficAccidents = new TrafficAccidents(period, (Integer) accidentCount);
    // Print the first value
    System.out.println("Accidents in " + trafficAccidents.getPeriod() + ":" + trafficAccidents.getAccidentCount());
  }

  public TrafficAccidents getTrafficAccidents() {
    return trafficAccidents;
  }
}
