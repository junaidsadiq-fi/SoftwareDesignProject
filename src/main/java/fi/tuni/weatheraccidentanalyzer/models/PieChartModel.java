package fi.tuni.weatheraccidentanalyzer.models;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartModel {
    /*public void updatePieChartForMonth(PieChart pieChart) {
        pieChart.getData().clear();

        pieChart.setTitle("Accident types in October");

        PieChart.Data slice1 = new PieChart.Data("Car Accidents", 55);
        PieChart.Data slice2 = new PieChart.Data("Bike Accidents", 25);
        PieChart.Data slice3 = new PieChart.Data("Pedestrian Accidents", 20);

        pieChart.getData().addAll(slice1, slice2, slice3);
    }

    public void updatePieChartForYear(PieChart pieChart) {
        pieChart.getData().clear();

        pieChart.setTitle("Accident types past year");

        PieChart.Data slice1 = new PieChart.Data("Car Accidents", 70);
        PieChart.Data slice2 = new PieChart.Data("Bike Accidents", 20);
        PieChart.Data slice3 = new PieChart.Data("Pedestrian Accidents", 10);

        pieChart.getData().addAll(slice1, slice2, slice3);
    }*/

    public void updatePieChart(PieChart pieChart, YearlyAccidentData yearlyAccData) {
        // Halutaan päivittää onnettomuudet tienkäyttäjien mukaan.
        // getMonthlyData() palauttaa List<MonthlyAccidentData>
        // public AccidentData getAccidentsByRoadUser(String roadUser)
        // AccidentDatalla count kenttä, jossa onnettumuuksien määrä tienkäyttäjän mukaan.
        //yearlyAccData.getMonthlyData()

        pieChart.getData().clear();
        pieChart.setTitle("Accidents by the selected road users");

        Map<String, String> roadUserNames = new HashMap<>();
        roadUserNames.put("SSS", "Total");
        roadUserNames.put("JK_SS", "Pedestrian");
        roadUserNames.put("PP_SS", "Cyclists");
        roadUserNames.put("MO_SS", "Moped");
        roadUserNames.put("MP_SS", "Motorcyclists");
        roadUserNames.put("HA_SS", "Passenger car");
        roadUserNames.put("LA_SS", "Van");
        roadUserNames.put("PA_SS", "Truck");
        roadUserNames.put("KA_SS", "Lorry"); // roadUserNames.put("KA_SS", "Bus");
        roadUserNames.put("MU_SS", "Other road users");

        List<String> selectedRoadUsers = yearlyAccData.getRoadUsers();
        List<MonthlyAccidentData> monthlyAccidents = yearlyAccData.getMonthlyData();

        System.out.println("ROAD USERS:" + selectedRoadUsers);

        // Hashmap to store the accidents of the different roadusers.
        Map<String, Integer> roadUserAccidentCounts = new HashMap<>();

        // Init the hashmap with zeros for each user.
        for (String roadUser : selectedRoadUsers) {
            roadUserAccidentCounts.put(roadUser, 0);
        }

        // Get the accidents counts for each road user
        System.out.println("GETTING ACCIDENTS BY USRES:");
        for (MonthlyAccidentData monthlyData : monthlyAccidents) {
            for (String roadUserCode : selectedRoadUsers) {
                String roadUser = roadUserNames.get(roadUserCode);
                AccidentData accidentData = monthlyData.getAccidentsByRoadUser(roadUser);
                if (accidentData != null) {
                    // Add accident count to the current total for the road user
                    int currentCount = roadUserAccidentCounts.getOrDefault(roadUser, 0);
                    System.out.println("CURRENT COUNT:" + currentCount);
                    roadUserAccidentCounts.put(roadUser, currentCount + accidentData.getAccidentCount());
                } else {
                    System.out.println("AccidentData is NULL");
                }
            }
        }
        System.out.println("GOT ALL THE ACCIDENTS.");

        // Create PieChart slices based on accumulated accident data
        for (Map.Entry<String, Integer> entry : roadUserAccidentCounts.entrySet()) {
            String roadUserCode = entry.getKey();
            int totalAccidents = entry.getValue();

            String roadUserLabel = roadUserNames.getOrDefault(roadUserCode, roadUserCode);

            // Only add to chart if there were any accidents
            if (totalAccidents > 0) {
                PieChart.Data slice = new PieChart.Data(roadUserLabel, totalAccidents);
                pieChart.getData().add(slice);
            }
        }
    }
}
