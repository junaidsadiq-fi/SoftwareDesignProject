package fi.tuni.weatheraccidentanalyzer.models;

/**
 * The AccidentData class represents data related to accidents involving different road users.
 */
public class AccidentData {

    /**
     * The type of road user involved in the accident.
     */
    private String roadUser;

    /**
     * The number of accidents involving the specified road user.
     */
    private int accidentCount;

    /**
     * Constructs a new AccidentData object with the specified road user and accident count.
     *
     * @param roadUser the type of road user involved in the accident
     * @param accidentCount the number of accidents involving the specified road user
     */
    public AccidentData(String roadUser, int accidentCount) {
        this.roadUser = roadUser;
        this.accidentCount = accidentCount;
    }

    /**
     * Returns the type of road user involved in the accident.
     *
     * @return the type of road user
     */
    public String getRoadUser() {
        return roadUser;
    }

    /**
     * Returns the number of accidents involving the specified road user.
     *
     * @return the number of accidents
     */
    public int getAccidentCount() {
        return accidentCount;
    }
}
