package mavenproject.weatherapi;

import java.util.List;

public class WeatherDataController {

    private final WeatherDataModel model;
    private final WeatherDataView view;

    public WeatherDataController(WeatherDataModel model, WeatherDataView view) {
        this.model = model;
        this.view = view;
    }

    public void run() {
        // Kysy käyttäjältä paikkakunta, kuukausi, vuosi ja parametrit
        String place = view.askForPlace();
        int year = view.askForYear();
        int month = view.askForMonth();
        List<String> selectedParameters = view.askForParameters();

        // Rakenna URL:t ja hae tiedot
        List<String> urls = model.buildUrls(place, year, month, selectedParameters);
        for (String url : urls) {
            try {
                String xmlContent = model.sendApiRequest(url);
                model.parseAndCategorizeXML(xmlContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Tallenna tulokset JSON-tiedostoon
        model.saveResultsToJson();
    }
}
