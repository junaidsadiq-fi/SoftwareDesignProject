package fi.tuni.weatheraccidentanalyzer.models;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class PieChartModel {
    public void updatePieChartForMonth(PieChart pieChart) {
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
    }
}
