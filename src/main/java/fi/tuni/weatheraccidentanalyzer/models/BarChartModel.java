package fi.tuni.weatheraccidentanalyzer.models;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.collections.FXCollections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartModel {
    /*public void updateBarChartForMonth(BarChart<String, Number> barChart) {
        barChart.getData().clear();
        barChart.setTitle("Accidents in October");
        //barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Accidents this month");

        series.getData().add(new XYChart.Data<>("October", 50));

        barChart.getData().add(series);

        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        xAxis.setCategories(FXCollections.observableArrayList("October"));
        xAxis.setTickLabelRotation(0);
    }

    public void updateBarChartForYear(BarChart<String, Number> barChart) {
        barChart.getData().clear();
        barChart.setTitle("Accidents past year");
        //barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Accidents in each month");

        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};

        int[] accidentsPerMonth = {50, 40, 60, 30, 80, 45, 70, 55, 65, 75, 85, 90};

        for (int i = 0; i < 12; i++) {
            series.getData().add(new XYChart.Data<>(months[i], accidentsPerMonth[i]));
        }

        barChart.getData().add(series);

        // Force the labels for the bars, rotate the labels if needed
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        xAxis.setTickLabelRotation(45);
        xAxis.setTickLength(10);
        xAxis.setTickMarkVisible(true);
        xAxis.setCategories(FXCollections.observableArrayList(months));
    }*/

    public void updateBarChart(BarChart<String, Number> barChart, YearlyAccidentData yearlyAccData) {
        System.out.println("UPDATING BAR CHART");
        Map<String, Integer> accidentsByMonth = calculateMonthlyAccidentTotals(yearlyAccData);

        // Clear the current bar chart data
        barChart.getData().clear();
        barChart.setTitle("Accidents by Month");

        // Create a new series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Accidents in each month");

        Map<String, String> monthMappings = new HashMap<>();
        monthMappings.put("M01", "January");
        monthMappings.put("M02", "February");
        monthMappings.put("M03", "March");
        monthMappings.put("M04", "April");
        monthMappings.put("M05", "May");
        monthMappings.put("M06", "June");
        monthMappings.put("M07", "July");
        monthMappings.put("M08", "August");
        monthMappings.put("M09", "September");
        monthMappings.put("M10", "October");
        monthMappings.put("M11", "November");
        monthMappings.put("M12", "December");

        for (Map.Entry<String, Integer> entry : accidentsByMonth.entrySet()) {
            String monthKey = entry.getKey(); // e.g., "2017M01"
            int accidentCount = entry.getValue();

            // Extract the readable month name from the key
            String monthCode = monthKey.substring(4); // Extract "M01", "M02", etc.
            String readableMonth = monthMappings.getOrDefault(monthCode, monthKey);

            // Add the data to the chart
            series.getData().add(new XYChart.Data<>(readableMonth, accidentCount));
        }

        // Add the series to the bar chart
        barChart.getData().add(series);

        // Configure the x-axis labels
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        xAxis.setTickLabelRotation(45); // Rotate the labels for better readability
        xAxis.setTickLength(10);
        xAxis.setTickMarkVisible(true);

        // Set categories based on the readable month names
        xAxis.setCategories(FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));
    }

    public Map<String, Integer> calculateMonthlyAccidentTotals(YearlyAccidentData yearlyAccData) {
        // Create a map to store the total accidents for each month
        Map<String, Integer> monthlyAccidentTotals = new HashMap<>();

        // Get the list of monthly accident data
        List<MonthlyAccidentData> monthlyData = yearlyAccData.getMonthlyData();

        // Iterate over each month's data
        for (MonthlyAccidentData monthData : monthlyData) {
            // Get the month name
            String month = monthData.getMonth();
            System.out.println("MONTH FROM MONTHDATA: " + month);

            // Initialize the total count for this month
            int totalAccidentsForMonth = 0;

            // Get the list of accidents for the month
            List<AccidentData> accidents = monthData.getAccidents();

            // Sum up the accident counts for all road users
            for (AccidentData accident : accidents) {
                totalAccidentsForMonth += accident.getAccidentCount();
            }

            // Store the total count in the map
            monthlyAccidentTotals.put(month, totalAccidentsForMonth);
        }

        return monthlyAccidentTotals;
    }
}
