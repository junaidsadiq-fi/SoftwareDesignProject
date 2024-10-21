package mavenproject.weatherapi;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;

public class Weatherapi {

    private static final String BASE_URL = "https://opendata.fmi.fi/wfs/fin?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::observations::weather::timevaluepair";
    private static final DateTimeFormatter API_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // Listat, joihin yhdistetään kaikki tulokset
    private static List<String> temperatures = new ArrayList<>();
    private static List<String> windSpeeds = new ArrayList<>();
    private static List<String> windGusts = new ArrayList<>();
    private static List<String> windDirections = new ArrayList<>();
    private static List<String> humidities = new ArrayList<>();
    private static List<String> dewPoints = new ArrayList<>();
    private static List<String> rainAmounts = new ArrayList<>();
    private static List<String> rainIntensities = new ArrayList<>();
    private static List<String> snowDepths = new ArrayList<>();
    private static List<String> pressures = new ArrayList<>();
    private static List<String> visibilities = new ArrayList<>();
    private static List<String> cloudCoverages = new ArrayList<>();
    private static List<String> weatherPhenomena = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // 1. Rakenna URL käyttäjän syötteiden perusteella ja pilko viikkoihin
            List<String> urls = buildUrls();

            // 2. Tee API-kutsut jokaiselle viikolle ja tallenna tulokset
            for (String url : urls) {
                System.out.println("Haetaan dataa URL:sta: " + url);
                String xmlContent = sendApiRequest(url);
                parseAndCategorizeXML(xmlContent);
            }

            // 3. Tallenna tulokset JSON-tiedostoon
            saveResultsToJson();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Rakenna dynaamiset URL:t pilkkomalla aikaväli viikkoihin
    private static List<String> buildUrls() {
        Scanner scanner = new Scanner(System.in);

        // Kysy paikkakunta
        System.out.print("Anna paikka (esim. Tampere): ");
        String place = scanner.nextLine();

        // Kysy kuukauden ja vuoden syöte
        int year = askForYear();
        int month = askForMonth();

        // Laske valitun kuukauden ensimmäinen ja viimeinen päivä
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDateTime startTime = startDate.atStartOfDay(); // 1. päivä klo 00:00
        LocalDateTime endTime = YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59); // Kuukauden viimeinen päivä klo 23:59:59

        // Parametrit
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

        String parameters = String.join(",", selectedParameters);

        // Lista urleille
        List<String> urls = new ArrayList<>();

        // Pilko aikaväli viikkoihin
        while (startTime.isBefore(endTime)) {
            LocalDateTime nextEndTime = startTime.plusWeeks(1).isBefore(endTime) ? startTime.plusWeeks(1) : endTime;

            // Rakenna yksittäinen URL yhden viikon ajalle
            String finalUrl = BASE_URL + "&place=" + place +
                    "&parameters=" + parameters +
                    "&starttime=" + startTime.format(API_FORMATTER) +
                    "&endtime=" + nextEndTime.format(API_FORMATTER);

            urls.add(finalUrl);

            // Päivitä aloitusaika seuraavan viikon alkuun
            startTime = nextEndTime;
        }

        return urls;
    }

    // Apumetodi käyttäjän vahvistuksen kysymiseen
    private static boolean confirm(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(question + " (Y/n): ");
        String answer = scanner.nextLine();
        return answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("y") || answer.isEmpty();
    }

    // Apumetodi käyttäjän kuukauden kysymiseen
    private static int askForMonth() {
        Scanner scanner = new Scanner(System.in);
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

    // Apumetodi käyttäjän vuoden kysymiseen
    private static int askForYear() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Anna vuosi (esim. 2019): ");
            int year = scanner.nextInt();
            if (year > 1900 && year <= LocalDate.now().getYear()) {
                return year;
            } else {
                System.out.println("Virheellinen vuosi. Syötä arvo välillä 1900 ja nykyinen vuosi.");
            }
        }
    }

    // Lähetä HTTP GET -kutsu API:lle ja palauta vastaus XML-muodossa
    private static String sendApiRequest(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();  // Palauta XML-vastaus merkkijonona
    }

    // Jäsennä XML-vastaus ja yhdistä tulokset
    private static void parseAndCategorizeXML(String xmlContent) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(xmlContent.getBytes()));

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("wml2:MeasurementTimeseries");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String paramType = element.getAttribute("gml:id");
                    NodeList measurementPoints = element.getElementsByTagName("wml2:MeasurementTVP");

                    for (int j = 0; j < measurementPoints.getLength(); j++) {
                        Node measurementNode = measurementPoints.item(j);
                        if (measurementNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element measurementElement = (Element) measurementNode;
                            String time = measurementElement.getElementsByTagName("wml2:time").item(0).getTextContent();
                            String value = measurementElement.getElementsByTagName("wml2:value").item(0).getTextContent();

                            switch (paramType) {
                                case "obs-obs-1-1-t2m":
                                    temperatures.add("Aika: " + time + ", Lämpötila: " + value + " °C");
                                    break;
                                case "obs-obs-1-1-ws_10min":
                                    windSpeeds.add("Aika: " + time + ", Tuulen nopeus: " + value + " m/s");
                                    break;
                                case "obs-obs-1-1-wg_10min":
                                    windGusts.add("Aika: " + time + ", Tuulenpuuskat: " + value + " m/s");
                                    break;
                                case "obs-obs-1-1-wd_10min":
                                    windDirections.add("Aika: " + time + ", Tuulen suunta: " + value + " astetta");
                                    break;
                                case "obs-obs-1-1-rh":
                                    humidities.add("Aika: " + time + ", Suhteellinen kosteus: " + value + " %");
                                    break;
                                case "obs-obs-1-1-td":
                                    dewPoints.add("Aika: " + time + ", Kastepistelämpötila: " + value + " °C");
                                    break;
                                case "obs-obs-1-1-r_1h":
                                    rainAmounts.add("Aika: " + time + ", Sademäärä: " + value + " mm");
                                    break;
                                case "obs-obs-1-1-ri_10min":
                                    rainIntensities.add("Aika: " + time + ", Sadeintensiteetti: " + value + " mm/h");
                                    break;
                                case "obs-obs-1-1-snow_aws":
                                    snowDepths.add("Aika: " + time + ", Lumensyvyys: " + value + " cm");
                                    break;
                                case "obs-obs-1-1-p_sea":
                                    pressures.add("Aika: " + time + ", Ilmanpaine: " + value + " hPa");
                                    break;
                                case "obs-obs-1-1-vis":
                                    visibilities.add("Aika: " + time + ", Näkyvyys: " + value + " m");
                                    break;
                                case "obs-obs-1-1-n_man":
                                    cloudCoverages.add("Aika: " + time + ", Pilvien peittävyys: " + value + " oktasia");
                                    break;
                                case "obs-obs-1-1-wawa":
                                    weatherPhenomena.add("Aika: " + time + ", Säätila: " + value);
                                    break;
                            }
                        }
                    }
                }
            }

        } catch (IOException | ParserConfigurationException | DOMException | SAXException e) {
            e.printStackTrace();
        }
    }

    // Tallenna tulokset JSON-tiedostoon
    private static void saveResultsToJson() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Kootaan kaikki tulokset yhteen Map-rakenteeseen
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("Lämpötilat", temperatures);
            resultMap.put("Tuulen nopeudet", windSpeeds);
            resultMap.put("Tuulenpuuskat", windGusts);
            resultMap.put("Tuulen suunnat", windDirections);
            resultMap.put("Suhteellinen kosteus", humidities);
            resultMap.put("Kastepistelämpötila", dewPoints);
            resultMap.put("Sademäärät", rainAmounts);
            resultMap.put("Sadeintensiteetit", rainIntensities);
            resultMap.put("Lumensyvyydet", snowDepths);
            resultMap.put("Ilmanpaineet", pressures);
            resultMap.put("Näkyvyydet", visibilities);
            resultMap.put("Pilvien peittävyys", cloudCoverages);
            resultMap.put("Säätilat", weatherPhenomena);

            // Kirjoita tiedot JSON-tiedostoon
            FileWriter writer = new FileWriter("weather_data.json");
            gson.toJson(resultMap, writer);
            writer.close();

            System.out.println("Tiedot tallennettu tiedostoon weather_data.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
