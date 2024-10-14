package fi.tuni.weatheraccidentanalyzer.models;

public class TrafficAccidents {
    private String period;
    private Integer accidentCount;

    public TrafficAccidents(String period, Integer accidentCount) {
        this.period = period;
        this.accidentCount = accidentCount;
    }

    public String getPeriod() {
        return period;
    }

    public Integer getAccidentCount() {
        return accidentCount;
    }
}
