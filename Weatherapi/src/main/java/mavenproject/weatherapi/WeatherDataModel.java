package mavenproject.weatherapi;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import org.xml.sax.SAXException;

public class WeatherDataModel {

    private static final String BASE_URL = "https://opendata.fmi.fi/wfs/fin?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::observations::weather::timevaluepair";
    private static final DateTimeFormatter API_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // Predefined station data
    private static final Map<String, String> STATION_MAP = new LinkedHashMap<>();

    static {
        STATION_MAP.put("uusimaa Porvoo Kalbådagrund", "101022");
        STATION_MAP.put("varsinais-suomi Turku Rajakari", "100947");
        STATION_MAP.put("satakunta Pori Tahkoluoto satama", "101267");
        STATION_MAP.put("kanta-häme Hämeenlinna Katinen", "101150");
        STATION_MAP.put("Pirkanmaa Virrat Äijänneva", "101310");
        STATION_MAP.put("Päijät-Häme Asikkala Pulkkilanharju", "101185");
        STATION_MAP.put("kymenlaakso Virolahti Koivuniemi", "101231");
        STATION_MAP.put("etelä-karjala Lappeenranta Konnunsuo", "101246");
        STATION_MAP.put("etelä-savo Juva Partala", "101418");
        STATION_MAP.put("pohjois-savo Kuopio Maaninka", "101572");
        STATION_MAP.put("pohjois-karjala Rautavaara Ylä-Luosta", "101603");
        STATION_MAP.put("Keski-Suomi Jyväskylä lentoasema", "101339");
        STATION_MAP.put("etelä-pohjanmaa Kauhajoki Kuja-Kokko", "101289");
        STATION_MAP.put("pohjanmaa Vaasa Klemettilä", "101485");
        STATION_MAP.put("keskipohjanmaa Halsua Purola", "101528");
        STATION_MAP.put("lappi Kemijärvi lentokenttä", "101950");
        STATION_MAP.put("Ahvenanmaa Jomala Jomalaby", "100917");
    }
    
    private final List<String> temperatures = new ArrayList<>();
    private final List<String> windSpeeds = new ArrayList<>();
    private final List<String> windGusts = new ArrayList<>();
    private final List<String> windDirections = new ArrayList<>();
    private final List<String> humidities = new ArrayList<>();
    private final List<String> dewPoints = new ArrayList<>();
    private final List<String> rainAmounts = new ArrayList<>();
    private final List<String> rainIntensities = new ArrayList<>();
    private final List<String> snowDepths = new ArrayList<>();
    private final List<String> pressures = new ArrayList<>();
    private final List<String> visibilities = new ArrayList<>();
    private final List<String> cloudCoverages = new ArrayList<>();
    private final List<String> weatherPhenomena = new ArrayList<>();

    // Retrieve station map for displaying options in the View
    public Map<String, String> getStationMap() {
        return STATION_MAP;
    }

    // Build URL list with fmisid and year split into weekly intervals
    public List<String> buildUrls(String stationId, int year, List<String> selectedParameters) {
        String parameters = String.join(",", selectedParameters);
        LocalDateTime startTime = Year.of(year).atDay(1).atStartOfDay();
        LocalDateTime endTime = YearMonth.of(year, 12).atEndOfMonth().atTime(23, 59, 59);

        List<String> urls = new ArrayList<>();

        while (startTime.isBefore(endTime)) {
            LocalDateTime nextEndTime = startTime.plusWeeks(1).isBefore(endTime) ? startTime.plusWeeks(1) : endTime;

            String finalUrl = BASE_URL + "&fmisid=" + stationId +
                    "&parameters=" + parameters +
                    "&starttime=" + startTime.format(API_FORMATTER) +
                    "&endtime=" + nextEndTime.format(API_FORMATTER);

            urls.add(finalUrl);
            startTime = nextEndTime;
        }

        return urls;
    }

    // Send API request to fetch weather data
    public String sendApiRequest(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        return response.body();
    }

    // Parse XML response and categorize data into separate lists
     public void parseAndCategorizeXML(String xmlContent) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setAttribute("http://xml.org/sax/features/namespaces", true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

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
                                    dewPoints.add("Aika: " + time + ", Kastepistelämpötilat: " + value + " °C");
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
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    // Save results to JSON file
    public void saveResultsToJson() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("Lämpötilat", temperatures);
            resultMap.put("Tuulen nopeudet", windSpeeds);
            resultMap.put("Tuulenpuuskat", windGusts);
            resultMap.put("Tuulen suunnat", windDirections);
            resultMap.put("Suhteellinen kosteus", humidities);
            resultMap.put("Kastepistelämpötilat", dewPoints);
            resultMap.put("Sademäärät", rainAmounts);
            resultMap.put("Sadeintensiteetit", rainIntensities);
            resultMap.put("Lumensyvyydet", snowDepths);
            resultMap.put("Ilmanpaineet", pressures);
            resultMap.put("Näkyvyydet", visibilities);
            resultMap.put("Pilvien peittävyys", cloudCoverages);
            resultMap.put("Säätilat", weatherPhenomena);

            FileWriter writer = new FileWriter("weather_data.json");
            gson.toJson(resultMap, writer);
            writer.close();

            System.out.println("Tiedot tallennettu tiedostoon weather_data.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
