package fi.tuni.weatheraccidentanalyzer.models;

import java.util.List;

/**
 * Represents the accident data for a specific month.
 */
public class MonthlyAccidentData {

    /**
     * The month for which the accident data is recorded.
     */
    private String month;

    /**
     * The list of accident data for the specified month.
     */
    private List<AccidentData> accidents;

    /**
     * Constructs a new MonthlyAccidentData object with the specified month and list of accidents.
     *
     * @param month the month for which the accident data is recorded
     * @param accidents the list of accident data for the specified month
     */
    public MonthlyAccidentData(String month, List<AccidentData> accidents) {
        this.month = month;
        this.accidents = accidents;
    }

    /**
     * Returns the month for which the accident data is recorded.
     *
     * @return the month for which the accident data is recorded
     */
    public String getMonth() {
        return month;
    }

    /**
     * Returns the accident data for a specific road user.
     *
     * @param roadUser the type of road user (e.g., pedestrian, cyclist, driver)
     * @return the accident data for the specified road user, or null if no data is found
     */
    public AccidentData getAccidentsByRoadUser(String roadUser) {
        for (AccidentData accident : accidents) {
            System.out.println("accident.getRoadUser():" + accident.getRoadUser());
            System.out.println("user got as parameter:" + roadUser);
            if (accident.getRoadUser().equalsIgnoreCase(roadUser)) {
                return accident;
            }
        }
        return null;
    }
}
