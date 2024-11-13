package fi.tuni.weatheraccidentanalyzer.models;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.collections.FXCollections;

public class BarChartModel {
    public void updateBarChartForMonth(BarChart<String, Number> barChart) {
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
    }
}
