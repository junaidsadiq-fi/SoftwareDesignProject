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

// Year parameter between 2015-2024
// Injury type parameter: "louk" for injuries, "kuol" for deaths and "loukv" for serious injuries
// Area parameter: 
    // SSS = Whole country
    // MK01 = Uusimaa
    // MK02 = Varsinais-Suomi
    // MK04 = Satakunta
    // MK05 = Kanta-Häme
    // MK06 = Pirkanmaa
    // MK07 = Päijät-Häme
    // MK08 = Kymenlaakso
    // MK09 = Etelä-Karjala
    // MK10 = Etelä-Savo
    // MK11 = Pohjois-Savo
    // MK12 = Pohjois-Karjala
    // MK13 = Keski-Suomi
    // MK14 = Etelä-Pohjanmaa
    // MK15 = Pohjanmaa
    // MK16 = Keski-Pohjanmaa
    // MK17 = Pohjois-Pohjanmaa
    // MK18 = Kainuu
    // MK19 = Lappi
    // MK21 = Ahvenanmaa

// Road user codes for different road user types
  // SSS = Total
  // JK_SS = Pedestrians
  // PP_SS = Cyclists
  // MO_SS = Moped
  // MP_SS = Motorcyclists
  // HA_SS = Passenger car
  // LA_SS = Van
  // PA_SS = Truck
  // KA_SS = Bus
  // MU_SS = Other road users

/**
 * Controller class for managing traffic accident data.
 */
public class TrafficAccidentsController {
    private YearlyAccidentData yearlyAccidentData;
    private TrafficService trafficService;

    /**
     * Constructs a new TrafficAccidentsController object.
     *
     * @param year the year for which the traffic data is requested
     * @param injuryType the type of injury data to fetch
     * @param roadUserCodes the list of road user codes to include in the request
     * @param area the area code for which the traffic data is requested
     */
    public TrafficAccidentsController(Integer year, String injuryType, List<String> roadUserCodes, String area) {
        trafficService = new TrafficService();
        List<AccidentData> roadUserAccidents;
        List<MonthlyAccidentData> monthlyAccidentDataList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            String formattedMonth = yearMonth.getYear() + "M" + String.format("%02d", yearMonth.getMonthValue());

            JSONObject jsonObject = new JSONObject(trafficService.getTrafficData(formattedMonth, injuryType, roadUserCodes, area));

            roadUserAccidents = getRoadUserAccidents(jsonObject);

            MonthlyAccidentData monthlyAccidentData = new MonthlyAccidentData(formattedMonth, roadUserAccidents);

            monthlyAccidentDataList.add(monthlyAccidentData);
        }

        yearlyAccidentData = new YearlyAccidentData(String.valueOf(year), monthlyAccidentDataList, area, injuryType, roadUserCodes);
    }

    /**
     * Returns the yearly accident data.
     *
     * @return the yearly accident data
     */
    public YearlyAccidentData getTrafficAccidents() {
        return yearlyAccidentData;
    }

    /**
     * Extracts road user accident data from the JSON response.
     *
     * @param jsonResponse the JSON response containing the accident data
     * @return a list of AccidentData objects
     */
    public List<AccidentData> getRoadUserAccidents(JSONObject jsonResponse) {
        List<AccidentData> roadUserAccidents = new ArrayList<>();

        // Get the road user labels and category indices
        JSONObject roadUserDimension = jsonResponse.getJSONObject("dimension").getJSONObject("Tienkäyttäjä");
        JSONObject roadUserLabels = roadUserDimension.getJSONObject("category").getJSONObject("label");
        JSONObject roadUserIndices = roadUserDimension.getJSONObject("category").getJSONObject("index");

        // Get the accident values
        JSONArray accidentValues = jsonResponse.getJSONArray("value");

        // Populate the list with road users and corresponding accident counts
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
