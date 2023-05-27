package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.model.Controller;
import by.bntu.fitr.diplom.model.Road;
import by.bntu.fitr.diplom.model.Vertex;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DeleteRoadController extends Controller implements Initializable {
    @FXML
    private TitledPane straightPane, backwardPane;
    @FXML
    private Label backwardVertexBLabel, backwardVertexALabel;
    @FXML
    private CheckBox directionCheckBox;
    @FXML
    private ComboBox<String> startComboBox, endComboBox;
    @FXML
    private Button clearDataBtn;


    private NewMapController newMapController;
    private List<String> elements;
    private AddRoadController addRoadController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        straightPane.setCollapsible(false);
        backwardPane.setCollapsible(false);

        backwardVertexALabel.setStyle("-fx-text-fill: blue");
        backwardVertexBLabel.setStyle("-fx-text-fill: blue");

        startComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onComboBoxSelected(endComboBox, startComboBox, backwardVertexBLabel);
            }
        });

        endComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onComboBoxSelected(startComboBox, endComboBox, backwardVertexALabel);
            }
        });

        Platform.runLater(() -> {
            elements = newMapController.getLabelTitles();
            addRoadController = new AddRoadController(newMapController);
            loadElementsToComboBox();
        });

    }

    @FXML
    private void onDeleteRoadButtonClick() {
        if (startComboBox.getValue() != null
                && endComboBox.getValue() != null) {

            Road firstRoad = findElementInRoads(startComboBox, endComboBox);
            Road secondRoad = findElementInRoads(endComboBox, startComboBox);

            if (directionCheckBox.isSelected() && firstRoad != null) {
                deleteRoadFromCollection(firstRoad);
                closeWindow();
            } else if (firstRoad != null && secondRoad != null) {
                deleteRoadFromCollection(firstRoad);
                deleteRoadFromCollection(secondRoad);
                closeWindow();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Отсутствует связь");
                alert.setHeaderText(null);
                alert.setContentText("Такая связь не существует!");

                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Заполните данные");
            alert.setHeaderText(null);
            alert.setContentText("Заполнены не все поля!");

            alert.showAndWait();
        }
    }

    private void deleteRoadFromCollection(Road road) {
        newMapController.removeRoad(road);

    }

    private Road findElementInRoads(ComboBox<String> first, ComboBox<String> second) {
        return newMapController.getRoads()
                .stream()
                .filter(road ->
                        road.getStartPositionX() == findElementInVertexes(first).getMousePositionX()
                                && road.getStartPositionY() == findElementInVertexes(first).getMousePositionY()
                                && road.getEndPositionX() == findElementInVertexes(second).getMousePositionX()
                                && road.getEndPositionY() == findElementInVertexes(second).getMousePositionY())
                .findFirst()
                .orElse(null);
    }

    private Vertex findElementInVertexes(ComboBox<String> comboBox) {
        return addRoadController.findElementInVertexes(comboBox);
    }

    @FXML
    private void onDirectionCheckBoxClick() {
        if (directionCheckBox.isSelected()) {
            backwardPane.setVisible(false);
        } else {
            backwardPane.setVisible(true);
        }
    }

    private void onComboBoxSelected(ComboBox<String> first, ComboBox<String> second, Label label) {
        addRoadController.onComboBoxSelected(first, second, label, elements);
    }

    @FXML
    private void onClearDataButtonClick() {
        startComboBox.setValue(null);
        endComboBox.setValue(null);
    }

    private void loadElementsToComboBox() {
        startComboBox.setItems(FXCollections.observableArrayList(elements));
        endComboBox.setItems(FXCollections.observableArrayList(elements));
    }

    @Override
    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }

    private void closeWindow() {
        newMapController.closeWindow(clearDataBtn);
    }
}
