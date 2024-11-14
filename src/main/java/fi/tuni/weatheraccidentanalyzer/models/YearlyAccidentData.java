package fi.tuni.weatheraccidentanalyzer.models;

import java.util.List;

/**
 * Represents the yearly accident data for a specific area and injury type.
 */
public class YearlyAccidentData {

    /**
     * The year of the accident data.
     */
    private String year;

    /**
     * The area where the accidents occurred.
     */
    private String area;

    /**
     * The type of injury recorded in the accident data.
     */
    private String injuryType;

    /**
     * The list of monthly accident data.
     */
    private List<MonthlyAccidentData> monthlyData;

    private List<String> roadUsers;

    /**
     * Constructs a new YearlyAccidentData object with the specified year, monthly data, area, and injury type.
     *
     * @param year the year of the accident data
     * @param monthlyData the list of monthly accident data
     * @param area the area where the accidents occurred
     * @param injuryType the type of injury recorded in the accident data
     */
    public YearlyAccidentData(String year, List<MonthlyAccidentData> monthlyData, String area, String injuryType, List<String> roadUsers) {
        this.year = year;
        this.monthlyData = monthlyData;
        this.area = area;
        this.injuryType = injuryType;
        this.roadUsers = roadUsers;
    }

    public List<String> getRoadUsers() {
        return this.roadUsers;
    }

    /**
     * Returns the year of the accident data.
     *
     * @return the year of the accident data
     */
    public String getYear() {
        return year;
    }

    /**
     * Returns the list of monthly accident data.
     *
     * @return the list of monthly accident data
     */
    public List<MonthlyAccidentData> getMonthlyData() {
        return monthlyData;
    }

    /**
     * Returns the area where the accidents occurred.
     *
     * @return the area where the accidents occurred
     */
    public String getArea() {
        return area;
    }

    /**
     * Returns the type of injury recorded in the accident data.
     *
     * @return the type of injury recorded in the accident data
     */
    public String getInjuryType() {
        return injuryType;
    }

    /**
     * Calculates and prints the total number of injuries for the year in the specified area.
     */
    public void getTotalInjuries() {
        int totalInjuries = 0;
        for (MonthlyAccidentData monthlyData : monthlyData) {
            totalInjuries += monthlyData.getAccidentsByRoadUser("Total").getAccidentCount();
        }
    }
}
