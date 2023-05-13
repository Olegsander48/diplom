package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.controllers.algorithms.FloydAlgorithm;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        routeParamsTitledPane.setCollapsible(false);
        String styleForLabels = "-fx-text-fill: blue";
        beginnigRouteLabel.setStyle(styleForLabels);
        endRouteLabel.setStyle(styleForLabels);
        totalCostsLabel.setStyle(styleForLabels);
        totalDistanceLabel.setStyle(styleForLabels);
        totalTimeLabel.setStyle(styleForLabels);

        createColumnsForTableView();

        floydAlgorithmController = new FloydAlgorithmController();
        floydAlgorithmController.progressBarAnimation(dijkstraProgressBar);
    }

    private void createColumnsForTableView() {
        TableColumn<ObservableList<SimpleStringProperty>, String> column1 = new TableColumn<>("N пп");
        column1.setCellValueFactory(str -> str.getValue().get(0));
        dijkstraTableView.getColumns().add(column1);

        TableColumn<ObservableList<SimpleStringProperty>, String> column2 = new TableColumn<>("Отправление");
        column2.setCellValueFactory(str -> str.getValue().get(1));
        dijkstraTableView.getColumns().add(column2);

        TableColumn<ObservableList<SimpleStringProperty>, String> column3 = new TableColumn<>("Прибытие");
        column3.setCellValueFactory(str -> str.getValue().get(2));
        dijkstraTableView.getColumns().add(column3);

        TableColumn<ObservableList<SimpleStringProperty>, String> column4 = new TableColumn<>("Расстояние, км");
        column4.setCellValueFactory(str -> str.getValue().get(3));
        dijkstraTableView.getColumns().add(column4);

        TableColumn<ObservableList<SimpleStringProperty>, String> column5 = new TableColumn<>("Время движения, ч");
        column5.setCellValueFactory(str -> str.getValue().get(4));
        dijkstraTableView.getColumns().add(column5);
    }

    private int createColumnsForExcelSheet(Sheet sheet) {
        Row row = sheet.createRow(0);

        Cell number = row.createCell(0);
        number.setCellValue("N пп");

        Cell departurePoint = row.createCell(1);
        departurePoint.setCellValue("Отправление");

        Cell arrivalPoint = row.createCell(2);
        arrivalPoint.setCellValue("Прибытие");

        Cell distance = row.createCell(3);
        distance.setCellValue("Расстояние, км");

        Cell time = row.createCell(4);
        time.setCellValue("Время движения, ч");

        return row.getPhysicalNumberOfCells();
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

            //todo тут либо будет загрузка данных через коллекцию или через массив
            //todo может нет смысла оставлять отдельный метод и всю логику перенести сюда?
            //loadDataToCells(sheet, floydAlgorithm.getMatrixOfOptimalConnections());
            createColumnsForExcelSheet(sheet);

            // Записываем всё в файл
            floydAlgorithmController.saveDataToExcelFile(selectedFile, book);
        }
    }

    /*private void loadDataToCells(Sheet sheet, double[][] arr) {
        int count = 1;
        createColumnsForExcelSheet(sheet);

        for (int i = 0; i < vertexList.size(); i++) {
            // Нумерация начинается с нуля
            Row row = sheet.createRow(i + 1);

            Cell number = row.createCell(0);
            number.setCellValue(count++);

            Cell vertexName = row.createCell(1);
            vertexName.setCellValue(vertexList.get(i).getLabel().getText());

            for (int j = 0; j < vertexList.size(); j++) {
                int index = j + 2;
                Cell distance = row.createCell(index);

                CellStyle cellStyle = distance.getCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                distance.setCellStyle(cellStyle);

                double value = arr[i][j];
                if (value == Double.POSITIVE_INFINITY) {
                    distance.setCellValue("∞");
                } else {
                    distance.setCellValue(String.format("%.2f", value));
                }
            }
        }
    }*/
}
