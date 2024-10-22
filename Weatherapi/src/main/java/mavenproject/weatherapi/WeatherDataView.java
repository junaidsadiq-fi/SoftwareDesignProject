package mavenproject.weatherapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WeatherDataView {

    private final Scanner scanner = new Scanner(System.in);

    public String askForPlace() {
        System.out.print("Anna paikka (esim. Tampere): ");
        return scanner.nextLine();
    }

    public int askForMonth() {
        while (true) {
            System.out.print("Anna kuukausi (1-12): ");
            int month = scanner.nextInt();
            if (month >= 1 && month <= 12) {
                return month;
            } else {
                System.out.println("Virheellinen kuukausi. Syötä arvo välillä 1-12.");
            }
        }
    }

    public int askForYear() {
        while (true) {
            System.out.print("Anna vuosi (esim. 2019): ");
            int year = scanner.nextInt();
            if (year > 1900 && year <= java.time.LocalDate.now().getYear()) {
                return year;
            } else {
                System.out.println("Virheellinen vuosi. Syötä arvo välillä 1900 ja nykyinen vuosi.");
            }
        }
    }

    public List<String> askForParameters() {
        List<String> selectedParameters = new ArrayList<>();
        System.out.println("Valitse parametrit (Y/n): ");
        if (confirm("Haluatko lämpötilan (t2m)?")) selectedParameters.add("t2m");
        if (confirm("Haluatko tuulen nopeuden (ws_10min)?")) selectedParameters.add("ws_10min");
        if (confirm("Haluatko tuulen puuskat (wg_10min)?")) selectedParameters.add("wg_10min");
        if (confirm("Haluatko tuulen suunnan (wd_10min)?")) selectedParameters.add("wd_10min");
        if (confirm("Haluatko suhteellisen kosteuden (rh)?")) selectedParameters.add("rh");
        if (confirm("Haluatko kastepistelämpötilan (td)?")) selectedParameters.add("td");
        if (confirm("Haluatko sademäärän (r_1h)?")) selectedParameters.add("r_1h");
        if (confirm("Haluatko sadeintensiteetin (ri_10min)?")) selectedParameters.add("ri_10min");
        if (confirm("Haluatko lumensyvyyden (snow_aws)?")) selectedParameters.add("snow_aws");
        if (confirm("Haluatko ilmanpaineen (p_sea)?")) selectedParameters.add("p_sea");
        if (confirm("Haluatko näkyvyyden (vis)?")) selectedParameters.add("vis");
        if (confirm("Haluatko pilvien peittävyyden (n_man)?")) selectedParameters.add("n_man");
        if (confirm("Haluatko säätilan (wawa)?")) selectedParameters.add("wawa");

        return selectedParameters;
    }

    private boolean confirm(String question) {
        System.out.print(question + " (Y/n): ");
        String answer = scanner.next();
        return answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("y") || answer.isEmpty();
    }
}
