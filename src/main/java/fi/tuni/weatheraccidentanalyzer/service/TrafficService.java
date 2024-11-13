package fi.tuni.weatheraccidentanalyzer.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for fetching traffic data from an external API.
 */
public class TrafficService {

    /**
     * Fetches traffic data for the specified parameters.
     *
     * @param yearMonth the year and month for which the traffic data is requested, in the format "YYYYMM"
     * @param injuryType the type of injury data to fetch
     * @param roadUserCodes the list of road user codes to include in the request
     * @param area the area code for which the traffic data is requested
     * @return the traffic data as a JSON string, or null if an error occurs
     */
    public String getTrafficData(String yearMonth, String injuryType, List<String> roadUserCodes, String area) {
        try {
            // Convert roadUserCodes list to a JSON array string
            String roadUserValues = roadUserCodes.stream()
                .map(code -> "\"" + code + "\"")
                .collect(Collectors.joining(", "));

            String requestBody = String.format("""
              {
                "query": [
                  {
                    "code": "Alue",
                    "selection": {
                      "filter": "item",
                      "values": [
                        "%s"
                      ]
                    }
                  },
                  {
                    "code": "Tienkäyttäjä",
                    "selection": {
                      "filter": "item",
                      "values": [%s]
                    }
                  },
                  {
                    "code": "Sukupuoli",
                    "selection": {
                      "filter": "item",
                      "values": [
                        "SSS"
                      ]
                    }
                  },
                  {
                    "code": "Ikä",
                    "selection": {
                      "filter": "item",
                      "values": [
                        "SSS"
                      ]
                    }
                  },
                  {
                    "code": "Kuukausi",
                    "selection": {
                      "filter": "item",
                      "values": [
                        "%s"
                      ]
                    }
                  },
                  {
                    "code": "Tiedot",
                    "selection": {
                      "filter": "item",
                      "values": [
                        "%s"
                      ]
                    }
                  }
                ],
                "response": {
                  "format": "json-stat2"
                }
              }
            """, area, roadUserValues, yearMonth, injuryType);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/ton/statfin_ton_pxt_112w.px"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
