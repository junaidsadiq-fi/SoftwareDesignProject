package mavenproject.weatherapi;

import java.util.Map;
import java.util.Scanner;

public class WeatherDataView {

    private final Scanner scanner = new Scanner(System.in);

    // Display station options and ask user to input the station ID directly
    public String askForStationId(Map<String, String> stationMap) {
        System.out.println("Valitse sääasema seuraavista syöttämällä aseman numero:");

        // Display available stations and their IDs
        for (Map.Entry<String, String> entry : stationMap.entrySet()) {
            System.out.println(entry.getValue() + " - " + entry.getKey());
        }

        // Prompt for the station ID
        while (true) {
            System.out.print("Syötä aseman numero: ");
            String stationId = scanner.next();

            if (stationMap.containsValue(stationId)) {
                return stationId; // Return valid station ID
            } else {
                System.out.println("Virheellinen valinta. Yritä uudelleen.");
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
}
