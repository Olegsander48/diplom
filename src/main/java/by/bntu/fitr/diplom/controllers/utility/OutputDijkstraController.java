package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.controllers.algorithms.dijkstraAlgorithm.Graph;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

        tableColumns = List.of("Отправление", "Прибытие", "Маршрут", "Расстояние, км", "Время движения, ч");
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
        Graph graph = new Graph(newMapController);

        for (Vertex vertex : vertexList) {
            graph.addVertex(vertex.getLabel().getText());
        }

        for (Road road : roadList) {
            graph.addEdge(
                    indexOfElement(road.getStartPositionX(),
                            road.getStartPositionY(),
                            graph),
                    indexOfElement(road.getEndPositionX(),
                            road.getEndPositionY(),
                            graph),
                    Integer.parseInt(road.getDistance()),
                    road.getSpeed());

        }

        graph.path().forEach(System.out::println); //todo убрать вывод значений из коллекции
        graph.path().forEach(this::loadDataToTable);
        graph.clean();
    }

    private int indexOfElement(double positionX, double positionY, Graph graph) {
        for (Vertex vertex : vertexList) {
            if (vertex.getMousePositionX() == positionX && vertex.getMousePositionY() == positionY) {
                return graph.indexOfElement(vertex.getLabel().getText());
            }
        }
        return 0;
    }

    private String[] transformString(String str) {
        String[] arr = str.split("\\s[-][>]\\s");
        for (int i = 0; i < arr.length; i++) {
            String forReplace =  arr[i].replaceAll("[(\\s)]", "");
            arr[i] = forReplace.replaceAll("^\\d+.\\d+", "");
        }
        return arr;
    }

    private void loadDataToTable(String lastString) {
        String[] vertexes = transformString(lastString);

        ObservableList<SimpleStringProperty> list = FXCollections.observableArrayList();
        list.add(new SimpleStringProperty(vertexes[0]));
        list.add(new SimpleStringProperty(vertexes[vertexes.length - 1]));
        list.add(new SimpleStringProperty(lastString.replaceAll("^\\d+\\s\\d+.\\d+.", "")));
        list.add(new SimpleStringProperty(lastString.replaceAll("\\s.+", "")));
        list.add(new SimpleStringProperty(lastString.replaceAll("^\\d+\\s", "")
                                                    .replaceAll("\\s.+", "")));
        dijkstraTableView.getItems().add(list);
        totalCostsLabel.setText("");
        totalDistanceLabel.setText("distance");
        totalTimeLabel.setText("time");

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
}
