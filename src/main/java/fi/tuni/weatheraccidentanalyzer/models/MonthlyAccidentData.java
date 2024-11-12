package fi.tuni.weatheraccidentanalyzer.models;

import java.util.List;

public class MonthlyAccidentData {
    private String month;
    private List<AccidentData> accidents;

    public MonthlyAccidentData(String month, List<AccidentData> accidents) {
        this.month = month;
        this.accidents = accidents;
    }

    public String getMonth() {
        return month;
    }

    public AccidentData getAccidentByRoadUser(String roadUser) {
        for (AccidentData accident : accidents) {
            if (accident.getRoadUser().equalsIgnoreCase(roadUser)) {
                return accident;
            }
        }
        return null;
    }
}
