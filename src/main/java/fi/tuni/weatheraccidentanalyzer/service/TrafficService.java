package fi.tuni.weatheraccidentanalyzer.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class TrafficService {
    public String getTrafficData(String yearMonth, String injuryType, List<String> roadUserCodes) {
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
                        "MK01"
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
            """, roadUserValues, yearMonth, injuryType);

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
