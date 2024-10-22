package mavenproject.weatherapi;


public class Weatherapi {
    public static void main(String[] args) {
        WeatherDataModel model = new WeatherDataModel();
        WeatherDataView view = new WeatherDataView();
        WeatherDataController controller = new WeatherDataController(model, view);

        // Käynnistä ohjelma
        controller.run();
    }
}
