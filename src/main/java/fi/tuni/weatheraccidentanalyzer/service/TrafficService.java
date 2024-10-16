package fi.tuni.weatheraccidentanalyzer.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class TrafficService {

  public String getTrafficData(String period) {
    try {
      HttpClient client = HttpClient.newHttpClient();
      String requestBody = """
        {"query": [
            {
                "code":"Kuukausi",
                "selection":{
                    "filter":"item",
                    "values": [
                        "%s"
                    ]
                }
            }
        ]}
        """.formatted(period);
        
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://pxdata.stat.fi/PXWeb/api/v1/en/StatFin/ton/statfin_ton_pxt_111e.px"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

      HttpResponse<String> response = client.send(
        request, HttpResponse.BodyHandlers.ofString());

      return response.body();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
