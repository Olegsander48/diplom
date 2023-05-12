package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.controllers.algorithms.FloydAlgorithm;
import by.bntu.fitr.diplom.model.Controller;
import by.bntu.fitr.diplom.model.Road;
import by.bntu.fitr.diplom.model.Vertex;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FloydAlgorithmController extends Controller implements Initializable {
    @FXML
    private Tab firstTab, secondTab;
    @FXML
    private TableView<ObservableList<SimpleStringProperty>> firstTableView, secondTableView;
    @FXML
    private ProgressBar floydProgressBar;
    private NewMapController newMapController;
    private List<Vertex> vertexList;
    private List<Road> roadList;
    private FloydAlgorithm floydAlgorithm;
    private boolean speedLimit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstTableView.setStyle("-fx-background-color: rgb(154, 181, 210)");
        secondTableView.setStyle("-fx-background-color: rgb(171, 171, 171)");

        Platform.runLater(() -> {
            vertexList = newMapController.getVertexes();
            roadList = newMapController.getRoads();
            floydAlgorithm = new FloydAlgorithm(vertexList, roadList);
            createColumnsForTable(firstTableView);
            createColumnsForTable(secondTableView);
            updateTables();
        });

        progressBarAnimation(floydProgressBar);
    }

    @FXML
    private void onSaveToExcelButtonClick() {
        Stage stage = (Stage) firstTableView.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить данные в excel-файл");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel book (*.xls)", "*.xls"),
                new FileChooser.ExtensionFilter("All Files (*.*)", "*.*")
        );
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            Workbook book = new HSSFWorkbook();
            Sheet firstSheet = book.createSheet(firstTab.getText());
            loadDataToCells(firstSheet, floydAlgorithm.getMatrixOfOptimalConnections(speedLimit));

            Sheet secondSheet = book.createSheet(secondTab.getText().substring(0, 24));
            loadDataToCells(secondSheet, floydAlgorithm.getTransportNetworkModel(speedLimit));

            // Записываем всё в файл
            try {
                book.write(new FileOutputStream(selectedFile));
                book.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Данные сохранены");
                alert.setHeaderText(null);
                alert.setContentText("Файл сохранен в " + selectedFile.getPath());
                alert.showAndWait();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadDataToCells(Sheet sheet, double[][] arr) {
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
    }

    private void createColumnsForExcelSheet(Sheet sheet) {
        Row row = sheet.createRow(0);

        Cell number = row.createCell(0);
        number.setCellValue("N пп");

        Cell vertexName = row.createCell(1);
        vertexName.setCellValue("Пункт отправления");

        for (int j = 0; j < vertexList.size(); j++) {
            int index = j + 2;
            Cell distance = row.createCell(index);

            CellStyle cellStyle = distance.getCellStyle();
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            distance.setCellStyle(cellStyle);

            distance.setCellValue(vertexList.get(j).getLabel().getText());
        }
    }

    private void createColumnsForTable(TableView<ObservableList<SimpleStringProperty>> tableView) {
        TableColumn<ObservableList<SimpleStringProperty>, String> column1 = new TableColumn<>("N пп");
        column1.setCellValueFactory(str -> str.getValue().get(0));
        tableView.getColumns().add(column1);

        TableColumn<ObservableList<SimpleStringProperty>, String> column2 = new TableColumn<>("Пункт отправления");
        column2.setCellValueFactory(str -> str.getValue().get(1));
        tableView.getColumns().add(column2);

        for (int i = 0; i < vertexList.size(); i++) {
            int index = i + 2;
            TableColumn<ObservableList<SimpleStringProperty>, String> column
                    = new TableColumn<>(vertexList.get(i).getLabel().getText());
            column.setCellValueFactory(str -> str.getValue().get(index));
            tableView.getColumns().add(column);
        }
    }

    private void loadDataToTable(double[][] values, TableView<ObservableList<SimpleStringProperty>> table) {
        int count = 1;
        for (int i = 0; i < vertexList.size(); i++) {
            ObservableList<SimpleStringProperty> list = FXCollections.observableArrayList();
            list.add(new SimpleStringProperty(String.valueOf(count++)));
            list.add(new SimpleStringProperty(vertexList.get(i).getLabel().getText()));
            for (int j = 0; j < vertexList.size(); j++) {
                list.add(new SimpleStringProperty(String.format("%.2f", values[i][j])));
            }
            table.getItems().add(list);
        }
    }

    private void updateTables() {
        speedLimit = !newMapController.getShortestParamRadioBtn().isSelected();
        loadDataToTable(floydAlgorithm.getMatrixOfOptimalConnections(speedLimit), firstTableView);
        loadDataToTable(floydAlgorithm.getTransportNetworkModel(speedLimit), secondTableView);
    }

    public void progressBarAnimation(ProgressBar progressBar) {
        for (double i = 0; i <= 1; i+= 0.1) {
            progressBar.setProgress(i);
        }
        progressBar.setStyle("-fx-accent: green");
    }

    @Override
    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }
}
