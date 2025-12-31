package com.lifecal.controller;

import com.lifecal.common.SceneManager;
import com.lifecal.common.Session;
import com.lifecal.model.WeightLog;
import com.lifecal.service.ReportService;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class WeightReportController {
    @FXML
    private LineChart<String, Number> weightChart;
    @FXML
    private Label startWeightLabel;
    @FXML
    private Label currentWeightLabel;
    @FXML
    private Label weightChangeLabel;
    @FXML
    private Label totalEntriesLabel;

    private ReportService reportService;
    private int currentUserId;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    @FXML
    private void initialize() {
        reportService = new ReportService();
        currentUserId = Session.getInstance().getCurrentUser().getId();
        loadReport();
    }

    private void loadReport() {
        List<WeightLog> weightHistory = reportService.getWeightTrend(currentUserId);
        Map<String, Object> stats = reportService.getWeightStatistics(currentUserId);

        if (!stats.isEmpty()) {
            double startWeight = (double) stats.get("startWeight");
            double currentWeight = (double) stats.get("currentWeight");
            double change = (double) stats.get("weightChange");

            startWeightLabel.setText(String.format("%.1f กก.", startWeight));
            currentWeightLabel.setText(String.format("%.1f กก.", currentWeight));

            String changeText = String.format("%.1f กก.", Math.abs(change));
            if (change > 0) {
                weightChangeLabel.setText("+" + changeText);
                weightChangeLabel.setStyle("-fx-text-fill: #EF4444;");
            } else if (change < 0) {
                weightChangeLabel.setText(changeText + " ↓");
                weightChangeLabel.setStyle("-fx-text-fill: #10B981;");
            } else {
                weightChangeLabel.setText(changeText);
                weightChangeLabel.setStyle("-fx-text-fill: #6B7280;");
            }
        } else {
            startWeightLabel.setText("N/A");
            currentWeightLabel.setText("N/A");
            weightChangeLabel.setText("N/A");
        }

        totalEntriesLabel.setText(String.valueOf(weightHistory.size()));
        updateChart(weightHistory);
    }

    private void updateChart(List<WeightLog> weightHistory) {
        XYChart.Series<String, Number> weightSeries = new XYChart.Series<>();
        weightSeries.setName("น้ำหนัก (กก.)");

        for (WeightLog log : weightHistory) {
            String dateLabel = log.getDate().format(dateFormatter);
            weightSeries.getData().add(new XYChart.Data<>(dateLabel, log.getWeight()));
        }

        weightChart.getData().clear();
        if (!weightSeries.getData().isEmpty()) {
            weightChart.getData().add(weightSeries);
        }

        weightChart.setCreateSymbols(true);
        weightChart.setLegendVisible(false);
    }

    @FXML
    private void handleRefresh() {
        loadReport();
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("dashboard.fxml", 1200, 800);
    }
}
