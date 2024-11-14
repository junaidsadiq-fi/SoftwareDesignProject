package fi.tuni.weatheraccidentanalyzer.models;

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
import java.nio.charset.StandardCharsets;
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
import org.xml.sax.SAXException;

public class WeatherDataModel {

    private static final String BASE_URL = "https://opendata.fmi.fi/wfs/fin?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::observations::weather::timevaluepair";
    private static final DateTimeFormatter API_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

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
    
    // Each parameter is now stored as a Map with the timestamp as key
    private final Map<String, Double> temperatures = new LinkedHashMap<>();
    private final Map<String, Double> windSpeeds = new LinkedHashMap<>();
    private final Map<String, Double> windGusts = new LinkedHashMap<>();
    private final Map<String, Double> windDirections = new LinkedHashMap<>();
    private final Map<String, Double> humidities = new LinkedHashMap<>();
    private final Map<String, Double> dewPoints = new LinkedHashMap<>();
    private final Map<String, Double> rainAmounts = new LinkedHashMap<>();
    private final Map<String, Double> rainIntensities = new LinkedHashMap<>();
    private final Map<String, Double> snowDepths = new LinkedHashMap<>();
    private final Map<String, Double> pressures = new LinkedHashMap<>();
    private final Map<String, Double> visibilities = new LinkedHashMap<>();
    private final Map<String, Double> cloudCoverages = new LinkedHashMap<>();
    private final Map<String, Double> weatherPhenomena = new LinkedHashMap<>();

    public Map<String, String> getStationMap() {
        return STATION_MAP;
    }

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

    public String sendApiRequest(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        return response.body();
    }

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
                            String valueStr = measurementElement.getElementsByTagName("wml2:value").item(0).getTextContent();
                            
                            double value = valueStr.equals("NaN") ? Double.NaN : Double.parseDouble(valueStr);

                            // Store value in the corresponding map with the timestamp as key
                            switch (paramType) {
                                case "obs-obs-1-1-t2m":
                                    temperatures.put(time, value);
                                    break;
                                case "obs-obs-1-1-ws_10min":
                                    windSpeeds.put(time, value);
                                    break;
                                case "obs-obs-1-1-wg_10min":
                                    windGusts.put(time, value);
                                    break;
                                case "obs-obs-1-1-wd_10min":
                                    windDirections.put(time, value);
                                    break;
                                case "obs-obs-1-1-rh":
                                    humidities.put(time, value);
                                    break;
                                case "obs-obs-1-1-td":
                                    dewPoints.put(time, value);
                                    break;
                                case "obs-obs-1-1-r_1h":
                                    rainAmounts.put(time, value);
                                    break;
                                case "obs-obs-1-1-ri_10min":
                                    rainIntensities.put(time, value);
                                    break;
                                case "obs-obs-1-1-snow_aws":
                                    snowDepths.put(time, value);
                                    break;
                                case "obs-obs-1-1-p_sea":
                                    pressures.put(time, value);
                                    break;
                                case "obs-obs-1-1-vis":
                                    visibilities.put(time, value);
                                    break;
                                case "obs-obs-1-1-n_man":
                                    cloudCoverages.put(time, value);
                                    break;
                                case "obs-obs-1-1-wawa":
                                    weatherPhenomena.put(time, value);
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
        }
    }

    public void saveResultsToJson() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeSpecialFloatingPointValues().create();

            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("Temperatures", temperatures);
            resultMap.put("windSpeeds", windSpeeds);
            resultMap.put("windGusts", windGusts);
            resultMap.put("windDirections", windDirections);
            resultMap.put("humidities", humidities);
            resultMap.put("dewPoints", dewPoints);
            resultMap.put("rainAmounts", rainAmounts);
            resultMap.put("rainIntensities", rainIntensities);
            resultMap.put("snowDepths", snowDepths);
            resultMap.put("pressures", pressures);
            resultMap.put("visibilities", visibilities);
            resultMap.put("cloudCoverages", cloudCoverages);
            resultMap.put("weatherPhenomena", weatherPhenomena);

            try (FileWriter writer = new FileWriter("weather_data.json", StandardCharsets.UTF_8)) {
                gson.toJson(resultMap, writer);
            }

            System.out.println("Data saved to weather_data.json");

        } catch (IOException e) {
        }
    }
}
