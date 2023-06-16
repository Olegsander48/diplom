package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.algorithms.DijkstraAlgorithm;
import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.model.Road;
import by.bntu.fitr.diplom.model.Vertex;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OutputDijkstraController implements Initializable {
    @FXML
    private TableView<ObservableList<SimpleStringProperty>> dijkstraTableView;
    @FXML
    private TitledPane routeParamsTitledPane;
    @FXML
    private Label beginnigRouteLabel, endRouteLabel;
    @FXML
    private Label totalDistanceLabel, totalTimeLabel, totalCostsLabel;
    @FXML
    private ProgressBar dijkstraProgressBar;

    private NewMapController newMapController;
    private String departurePoint, arrivalPoint;
    private FloydAlgorithmController floydAlgorithmController;
    private List<Vertex> vertexList;
    private List<Road> roadList;
    private List<String> tableColumns;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        routeParamsTitledPane.setCollapsible(false);
        String styleForLabels = "-fx-text-fill: blue";
        beginnigRouteLabel.setStyle(styleForLabels);
        endRouteLabel.setStyle(styleForLabels);
        totalCostsLabel.setStyle(styleForLabels);
        totalDistanceLabel.setStyle(styleForLabels);
        totalTimeLabel.setStyle(styleForLabels);

        floydAlgorithmController = new FloydAlgorithmController();
        floydAlgorithmController.progressBarAnimation(dijkstraProgressBar);

        tableColumns = List.of("№ пп", "Отправление", "Прибытие", "Расстояние, км", "Время движения, ч");
        createColumnsForTableView(dijkstraTableView, tableColumns);

        Platform.runLater(() -> {
            vertexList = newMapController.getVertexes();
            roadList = newMapController.getRoads();
            calculateRoute();
        });
    }

    public void createColumnsForTableView(TableView<ObservableList<SimpleStringProperty>> tableView, List<String> tableColumns) {
        for (int i = 0; i < tableColumns.size(); i++) {
            int index = i;
            TableColumn<ObservableList<SimpleStringProperty>, String> column = new TableColumn<>(tableColumns.get(i));
            column.setCellValueFactory(str -> str.getValue().get(index));
            tableView.getColumns().add(column);
        }
    }

    public void createColumnsForExcelSheet(Sheet sheet, List<String> tableColumns) {
        Row row = sheet.createRow(0);

        for (int i = 0; i < tableColumns.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(tableColumns.get(i));
        }
    }

    @FXML
    private void saveDataToExcel() {
        File selectedFile = floydAlgorithmController.choosePlaceToSaveExcelFile(dijkstraTableView);
        if (selectedFile != null) {
            Workbook book = new HSSFWorkbook();
            Sheet sheet = book.createSheet("Оптимальный путь");

            createColumnsForExcelSheet(sheet, tableColumns);
            loadDataToCells(sheet, dijkstraTableView);

            // Записываем всё в файл
            floydAlgorithmController.saveDataToExcelFile(selectedFile, book);
        }
    }

    private void calculateRoute() {
        DijkstraAlgorithm algorithm = new DijkstraAlgorithm(vertexList, roadList);
        loadDataToTable(
                algorithm.getMapOfDistances(departurePoint, arrivalPoint),
                algorithm.getMapOfTimes(departurePoint, arrivalPoint));

    }

    private void loadDataToTable(Map<String, Double> mapOfDistances, Map<String, Double> mapOfTimes) {
        List<Double> distances = mapOfDistances.values().stream().toList();
        List<String> vertexes = mapOfDistances.keySet().stream().toList();
        List<Double> times = mapOfTimes.values().stream().toList();

        for (int i = 1; i < vertexes.size(); i++) {
            ObservableList<SimpleStringProperty> list = FXCollections.observableArrayList();
            list.add(new SimpleStringProperty(String.valueOf(i)));
            list.add(new SimpleStringProperty(String.valueOf(vertexes.get(i - 1))));
            list.add(new SimpleStringProperty(String.valueOf(vertexes.get(i))));
            list.add(new SimpleStringProperty(String.format("%.0f",distances.get(i) - distances.get(i - 1))));
            list.add(new SimpleStringProperty(String.format("%.4f",times.get(i) - times.get(i - 1))));
            dijkstraTableView.getItems().add(list);
        }

        totalCostsLabel.setText("0");
        totalDistanceLabel.setText(mapOfDistances.get(arrivalPoint) + " (км)");
        totalTimeLabel.setText(String.format("%.4f",mapOfTimes.get(arrivalPoint)) + " (ч)");

    }

    public void loadDataToCells(Sheet sheet, TableView<ObservableList<SimpleStringProperty>> tableView) {
        int count = 1;

        for (ObservableList<SimpleStringProperty> observableList : tableView.getItems()) {
            Row row = sheet.createRow(count++);

            for (int i = 0; i < observableList.size(); i++) {
                Cell number = row.createCell(i);
                number.setCellValue(observableList.get(i).getValue());
            }
        }
    }

    public void setDeparturePoint(String selectedItem) {
        departurePoint = selectedItem;
        beginnigRouteLabel.setText(departurePoint);
    }

    public void setArrivalPoint(String selectedItem) {
        arrivalPoint = selectedItem;
        endRouteLabel.setText(arrivalPoint);
    }

    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }
}
