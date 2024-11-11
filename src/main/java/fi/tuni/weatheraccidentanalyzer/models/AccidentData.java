package fi.tuni.weatheraccidentanalyzer.models;

import java.util.Map;

public class AccidentData {
    private String roadUserType;
    private int accidentCount;

    public AccidentData(String roadUserType, int accidentCount) {
        this.roadUserType = roadUserType;
        this.accidentCount = accidentCount;
    }

    public String getRoadUserType() {
        return roadUserType;
    }

    public int getAccidentCount() {
        return accidentCount;
    }

}
