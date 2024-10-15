package fi.tuni.weatheraccidentanalyzer.controllers;

import fi.tuni.weatheraccidentanalyzer.models.TrafficAccidents;
import fi.tuni.weatheraccidentanalyzer.service.TrafficService;

public class TrafficAccidentsController {
    private TrafficAccidents trafficAccidents;
    private TrafficService trafficService;

    public TrafficAccidentsController() {
      trafficService = new TrafficService();
      trafficAccidents = new TrafficAccidents("2009M01", 10);
      System.out.println(trafficService.getTrafficData());
      System.out.println(trafficAccidents.getPeriod());
    }

    public TrafficAccidents getTrafficAccidents() {
        return trafficAccidents;
    }
}
