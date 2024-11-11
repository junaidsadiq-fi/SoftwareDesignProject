package fi.tuni.weatheraccidentanalyzer.models;

import java.util.List;

public class YearlyAccidentData {
    private String year;
    private String area;
    private String injuryType;
    private List<MonthlyAccidentData> monthlyData;

    public YearlyAccidentData(String year, List<MonthlyAccidentData> monthlyData, String area, String injuryType) {
        this.year = year;
        this.monthlyData = monthlyData;
        this.area = area;
        this.injuryType = injuryType;
    }

    public String getYear() {
        return year;
    }

    public List<MonthlyAccidentData> getMonthlyData() {
        return monthlyData;
    }

    public String getArea() {
        return area;
    }

    public String getInjuryType() {
        return injuryType;
    }
}
