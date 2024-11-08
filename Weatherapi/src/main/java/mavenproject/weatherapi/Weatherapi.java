package mavenproject.weatherapi;

import mavenproject.weatherapi.WeatherDataModel;
import mavenproject.weatherapi.WeatherDataView;
import mavenproject.weatherapi.WeatherDataController;

public class Weatherapi {
    public static void main(String[] args) {
        WeatherDataModel model = new WeatherDataModel();
        WeatherDataView view = new WeatherDataView();
        WeatherDataController controller = new WeatherDataController(model, view);

        // Start the program
        controller.run();
    }
}
