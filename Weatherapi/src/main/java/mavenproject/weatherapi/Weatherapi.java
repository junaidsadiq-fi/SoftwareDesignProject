package mavenproject.weatherapi;

import mavenproject.weatherapi.WeatherDataModel;
import mavenproject.weatherapi.WeatherDataController;

public class Weatherapi {
    public static void main(String[] args) {
        WeatherDataModel model = new WeatherDataModel();
        WeatherDataController controller = new WeatherDataController(model);

        // Start the program
        controller.run();
    }
}
