package fi.tuni.weatheraccidentanalyzer.models;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.collections.FXCollections;


public class LineChartModel {
    public void updateLineChartForMonth(LineChart<String, Number> lineChart) {
        lineChart.getData().clear();
        lineChart.setTitle("Daily Temperatures in October");

        // Series for daily temperatures
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Temperatures in October");

        // Demo data: Daily temperatures for October (31 days)
        String[] days = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "27", "28",
                "29", "30", "31"
        };
        int[] dailyTemperatures = {15, 14, 13, 15, 16, 17, 15, 14, 13, 16,
                17, 18, 19, 20, 21, 19, 18, 17, 16,
                15, 16, 17, 18, 19, 20, 21, 22, 23,
                21, 20, 19, 18, 17, 16}; // Example temperatures

        for (int i = 0; i < days.length; i++) {
            series.getData().add(new XYChart.Data<>(days[i], dailyTemperatures[i]));
        }

        lineChart.getData().add(series);

        // Force the labels for the days
        CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
        xAxis.setTickLabelRotation(45);
        xAxis.setTickLength(10);
        xAxis.setCategories(FXCollections.observableArrayList(days));
    }

    public void updateLineChartForYear(LineChart<String, Number> lineChart) {
        lineChart.getData().clear();
        lineChart.setTitle("Average Monthly Temperatures");

        // Series for monthly average temperatures
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Monthly Temperatures");

        // Demo data: Monthly average temperatures
        String[] months = {
                "January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October",
                "November", "December"
        };
        double[] averageMonthlyTemperatures = {-5, -4, 2, 10, 15, 20, 25, 24, 16, 10, 2, -3};

        for (int i = 0; i < months.length; i++) {
            series.getData().add(new XYChart.Data<>(months[i], averageMonthlyTemperatures[i]));
        }

        lineChart.getData().add(series);

        // Force the labels for the months
        CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
        xAxis.setTickLabelRotation(45);
        xAxis.setTickLength(10);
        xAxis.setCategories(FXCollections.observableArrayList(months));
    }
}
