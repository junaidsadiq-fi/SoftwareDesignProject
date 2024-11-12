package fi.tuni.weatheraccidentanalyzer.controllers;

import fi.tuni.weatheraccidentanalyzer.models.AccidentData;
import fi.tuni.weatheraccidentanalyzer.models.MonthlyAccidentData;
import fi.tuni.weatheraccidentanalyzer.models.YearlyAccidentData;
import fi.tuni.weatheraccidentanalyzer.service.TrafficService;

import org.json.JSONArray;
import org.json.JSONObject;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class TrafficAccidentsController {
  private AccidentData trafficAccidents;
  private TrafficService trafficService;

  public TrafficAccidentsController(Integer year, String injuryType) {
    trafficService = new TrafficService();
    List<AccidentData> roadUserAccidents;
    List<MonthlyAccidentData> monthlyAccidentDataList = new ArrayList<>();

    for (int month = 1; month <= 12; month++) {
      YearMonth yearMonth = YearMonth.of(year, month);
      String formattedMonth = yearMonth.getYear() + "M" + String.format("%02d", yearMonth.getMonthValue());

      JSONObject jsonObject = new JSONObject(trafficService.getTrafficData(formattedMonth, injuryType));

      roadUserAccidents = getRoadUserAccidents(jsonObject);

      MonthlyAccidentData monthlyAccidentData = new MonthlyAccidentData(formattedMonth, roadUserAccidents);

      monthlyAccidentDataList.add(monthlyAccidentData);

    }

    YearlyAccidentData yearlyAccidentData = new YearlyAccidentData(String.valueOf(year), monthlyAccidentDataList, "Pirkanmaa", "SSS");

    for (int i = 0; i < yearlyAccidentData.getMonthlyData().size(); i++) {
      System.out.println(yearlyAccidentData.getMonthlyData().get(i).getAccidents().get(0).getRoadUserType());
      System.out.println(yearlyAccidentData.getMonthlyData().get(i).getAccidents().get(0).getAccidentCount());
    }

  }

  public AccidentData getTrafficAccidents() {
    return trafficAccidents;
  }

  public List<AccidentData> getRoadUserAccidents(JSONObject jsonResponse) {
    List<AccidentData> roadUserAccidents = new ArrayList<>();

    // Get the road user labels and category indices
    JSONObject roadUserDimension = jsonResponse.getJSONObject("dimension").getJSONObject("Tienkäyttäjä");
    JSONObject roadUserLabels = roadUserDimension.getJSONObject("category").getJSONObject("label");
    JSONObject roadUserIndices = roadUserDimension.getJSONObject("category").getJSONObject("index");
    
    // Get the accident values
    JSONArray accidentValues = jsonResponse.getJSONArray("value");

    // Populate the map with road users and corresponding accident counts
    for (String roadUserCode : roadUserLabels.keySet()) {
        String roadUserLabel = roadUserLabels.getString(roadUserCode);
        int index = roadUserIndices.getInt(roadUserCode); // Get the index for the current road user
        int accidentCount = accidentValues.getInt(index); // Fetch the accident count using the index
        AccidentData accidentData = new AccidentData(roadUserLabel, accidentCount);
        roadUserAccidents.add(accidentData);
    }

    return roadUserAccidents;
}
}
