package Controllers;

import Entities.Reservation_v;
import Service.ReservationVService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsController {

    @FXML
    private BarChart<String, Number> reservationChart;

    @FXML
    private CategoryAxis monthAxis;

    @FXML
    private PieChart pieChart;

    @FXML
    private TableView<Map.Entry<String, Integer>> tableView;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> monthColumn;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> countColumn;

    private ReservationVService reservationService;

    @FXML
    public void initialize() {
        reservationService = new ReservationVService();
        displayReservationStatistics();
    }

    public void displayReservationStatistics() {
        // Fetch reservation data from the service
        List<Reservation_v> reservations = reservationService.readAllreservation();

        // Calculate number of reservations per month
        Map<String, Integer> reservationsPerMonth = new HashMap<>();
        for (Reservation_v reservation : reservations) {
            Month month = reservation.getDate_debut().getMonth();
            String monthString = month.toString();
            reservationsPerMonth.put(monthString, reservationsPerMonth.getOrDefault(monthString, 0) + 1);
        }

        // Populate data for the TableView
        ObservableList<Map.Entry<String, Integer>> data = FXCollections.observableArrayList(reservationsPerMonth.entrySet());
        tableView.setItems(data);

        // Populate data for the BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Month month : Month.values()) {
            series.getData().add(new XYChart.Data<>(month.toString(), reservationsPerMonth.getOrDefault(month.toString(), 0)));
        }
        reservationChart.getData().add(series);

        // Populate data for the PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Month month : Month.values()) {
            String monthString = month.toString();
            if (reservationsPerMonth.containsKey(monthString)) {
                pieChartData.add(new PieChart.Data(monthString, reservationsPerMonth.get(monthString)));
            }
        }
        pieChart.setData(pieChartData);

        // Associate each property of ReservationData with the corresponding column in the TableView
        monthColumn.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getKey()));
        countColumn.setCellValueFactory(entry -> new SimpleIntegerProperty(entry.getValue().getValue()).asObject());

        // Set chart axes labels
        monthAxis.setLabel("Month");

        // Set chart title
        reservationChart.setTitle("Reservation Statistics");
        pieChart.setTitle("Reservation Statistics");
    }

}
