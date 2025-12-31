package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.service.ReportService;
import com.lifecal.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class CalorieReportController {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private LineChart<String, Number> calorieChart;
    @FXML
    private Label totalIntakeLabel;
    @FXML
    private Label totalBurnedLabel;
    @FXML
    private Label netCaloriesLabel;
    @FXML
    private Label avgDailyLabel;

    private ReportService reportService;
    private int currentUserId;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd");

    @FXML
    private void initialize() {
        reportService = new ReportService();
        currentUserId = Session.getInstance().getCurrentUser().getId();

        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(6));
        loadReport();
    }

    @FXML
    private void handleLast7Days() {
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(6));
        loadReport();
    }

    @FXML
    private void handleLast30Days() {
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(29));
        loadReport();
    }

    @FXML
    private void handleThisMonth() {
        LocalDate now = LocalDate.now();
        endDatePicker.setValue(now);
        startDatePicker.setValue(now.withDayOfMonth(1));
        loadReport();
    }

    @FXML
    private void handleRefresh() {
        loadReport();
    }

    private void loadReport() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (start == null || end == null) {
            AlertUtil.showWarning("กรุณาเลือกทั้งวันที่เริ่มต้นและวันที่สิ้นสุด");
            return;
        }

        if (start.isAfter(end)) {
            AlertUtil.showWarning("วันที่เริ่มต้นต้องก่อนหรือเท่ากับวันที่สิ้นสุด");
            return;
        }

        Map<String, Object> stats = reportService.getCalorieStatistics(currentUserId, start, end);

        double totalIntake = (double) stats.get("totalIntake");
        double totalBurned = (double) stats.get("totalBurned");
        double totalNet = (double) stats.get("totalNet");

        totalIntakeLabel.setText(String.format("%.0f kcal", totalIntake));
        totalBurnedLabel.setText(String.format("%.0f kcal", totalBurned));
        netCaloriesLabel.setText(String.format("%.0f kcal", totalNet));

        long days = end.toEpochDay() - start.toEpochDay() + 1;
        double avgDaily = totalIntake / days;
        avgDailyLabel.setText(String.format("%.0f kcal", avgDaily));

        updateChart(stats);
    }

    @SuppressWarnings("unchecked")
    private void updateChart(Map<String, Object> stats) {
        Map<LocalDate, Double> intakeByDate = (Map<LocalDate, Double>) stats.get("intake");
        Map<LocalDate, Double> burnedByDate = (Map<LocalDate, Double>) stats.get("burned");

        TreeMap<LocalDate, Double> sortedIntake = new TreeMap<>(intakeByDate);
        TreeMap<LocalDate, Double> sortedBurned = new TreeMap<>(burnedByDate);

        XYChart.Series<String, Number> intakeSeries = new XYChart.Series<>();
        intakeSeries.setName("แคลอรี่ที่ได้รับ");

        XYChart.Series<String, Number> burnedSeries = new XYChart.Series<>();
        burnedSeries.setName("แคลอรี่ที่เผาผลาญ");

        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        LocalDate current = start;

        while (!current.isAfter(end)) {
            String dateLabel = current.format(dateFormatter);
            double intake = sortedIntake.getOrDefault(current, 0.0);
            double burned = sortedBurned.getOrDefault(current, 0.0);

            intakeSeries.getData().add(new XYChart.Data<>(dateLabel, intake));
            burnedSeries.getData().add(new XYChart.Data<>(dateLabel, burned));

            current = current.plusDays(1);
        }

        calorieChart.getData().clear();
        calorieChart.getData().addAll(intakeSeries, burnedSeries);
        calorieChart.setCreateSymbols(true);
        calorieChart.setLegendVisible(true);
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("dashboard.fxml", 1200, 800);
    }
}
