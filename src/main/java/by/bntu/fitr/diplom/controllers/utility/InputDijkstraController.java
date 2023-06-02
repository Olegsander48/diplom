package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.HelloApplication;
import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.model.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class InputDijkstraController extends Controller implements Initializable {
    @FXML
    private AnchorPane inputDijkstraAnchorPane;
    @FXML
    private Button showRouteButton, closeWindowButton;
    @FXML
    private ComboBox<String> startComboBox, endComboBox;

    private NewMapController newMapController;
    private List<String> elements;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputDijkstraAnchorPane.setStyle("-fx-background-color: skyblue");

        startComboBox.setOnAction(event -> onComboBoxSelected(endComboBox, startComboBox));

        endComboBox.setOnAction(event -> onComboBoxSelected(startComboBox, endComboBox));

        Platform.runLater(() -> {
            elements = newMapController.getLabelTitles();
            startComboBox.setItems(FXCollections.observableArrayList(elements));
            endComboBox.setItems(FXCollections.observableArrayList(elements));
        });

    }

    @FXML
    private void onShowRouteButtonClick() throws IOException {
        if (startComboBox.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/utility/output-dijkstra-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Алгоритм Дейкстра: Выбор маршрута - "
                    + ((RadioButton) newMapController.getParametersRadioPane()
                        .getSelectedToggle())
                        .getText()
                        .toUpperCase(Locale.ROOT)
                    + " ПУТЬ");
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image("by/bntu/fitr/diplom/images/add_vertex.png"));

            OutputDijkstraController controller = fxmlLoader.getController();
            controller.setNewMapController(newMapController);
            controller.setDeparturePoint(startComboBox.getSelectionModel().getSelectedItem());
            controller.setArrivalPoint(endComboBox.getSelectionModel().getSelectedItem()
                    != null
                    ? endComboBox.getSelectionModel().getSelectedItem()
                    : endComboBox.getItems().get(endComboBox.getItems().size() - 1));


            stage.setScene(scene);
            stage.show();

            newMapController.closeWindow(closeWindowButton);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Укажите данные");
            alert.setHeaderText(null);
            alert.setContentText("Укажите вершины для которых надо построить маршрут!");

            alert.showAndWait();
        }
    }

    @FXML
    private void onClearSelectionButtonClick() {
        startComboBox.getSelectionModel().clearSelection();
        endComboBox.getSelectionModel().clearSelection();
    }

    private void onComboBoxSelected(ComboBox<String> first, ComboBox<String> second) {
        AddRoadController controller = new AddRoadController();
        controller.onComboBoxSelected(first, second, new Label(), elements);
    }

    @FXML
    private void onCloseWindowButtonClick() {
        newMapController.closeWindow(closeWindowButton);
    }

    @Override
    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }
}
