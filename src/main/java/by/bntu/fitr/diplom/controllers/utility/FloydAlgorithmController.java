package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.algorithms.FloydAlgorithm;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
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
    private OutputDijkstraController dijkstraController;
    private List<Vertex> vertexList;
    private List<Road> roadList;
    private FloydAlgorithm floydAlgorithm;
    private boolean speedLimit;
    private List<String> tableColumns;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstTableView.setStyle("-fx-background-color: rgb(154, 181, 210)");
        secondTableView.setStyle("-fx-background-color: rgb(171, 171, 171)");

        dijkstraController = new OutputDijkstraController();
        tableColumns = new ArrayList<>(List.of("N пп", "Пункт отправления"));

        Platform.runLater(() -> {
            vertexList = newMapController.getVertexes();
            roadList = newMapController.getRoads();
            floydAlgorithm = new FloydAlgorithm(vertexList, roadList);
            vertexList.stream().map(vertex -> vertex.getLabel().getText()).forEach(vertex -> tableColumns.add(vertex));
            dijkstraController.createColumnsForTableView(firstTableView, tableColumns);
            dijkstraController.createColumnsForTableView(secondTableView, tableColumns);
            updateTables();
        });

        progressBarAnimation(floydProgressBar);
    }

    @FXML
    private void onSaveToExcelButtonClick() {
        File selectedFile = choosePlaceToSaveExcelFile(firstTableView);
        if (selectedFile != null) {
            Workbook book = new HSSFWorkbook();
            Sheet firstSheet = book.createSheet(firstTab.getText());
            Sheet secondSheet = book.createSheet(secondTab.getText().substring(0, 24));

            dijkstraController.createColumnsForExcelSheet(firstSheet, tableColumns);
            dijkstraController.createColumnsForExcelSheet(secondSheet, tableColumns);

            dijkstraController.loadDataToCells(firstSheet, firstTableView);
            dijkstraController.loadDataToCells(secondSheet, secondTableView);

            // Записываем всё в файл
            saveDataToExcelFile(selectedFile, book);
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

    public void saveDataToExcelFile (File file, Workbook workbook) {
        try {
            workbook.write(new FileOutputStream(file));
            workbook.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Данные сохранены");
            alert.setHeaderText(null);
            alert.setContentText("Файл сохранен в " + file.getPath());
            alert.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File choosePlaceToSaveExcelFile(TableView<ObservableList<SimpleStringProperty>> tableView) {
        Stage stage = (Stage) tableView.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить данные в excel-файл");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel book (*.xls)", "*.xls"),
                new FileChooser.ExtensionFilter("All Files (*.*)", "*.*")
        );
        return fileChooser.showSaveDialog(stage);
    }

    @Override
    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }
}
