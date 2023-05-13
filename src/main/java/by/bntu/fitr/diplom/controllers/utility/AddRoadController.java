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

public class AddRoadController extends Controller implements Initializable {
    @FXML
    private TitledPane straightPane, backwardPane;
    @FXML
    private Label backwardRoadClassLabel, backwardVertexBLabel, backwardVertexALabel;
    @FXML
    private CheckBox directionCheckBox;
    @FXML
    private ComboBox<String> startComboBox, endComboBox;
    @FXML
    private ComboBox<Integer> roadClassComboBox;
    @FXML
    private Label backwardSpeedLabel, straightSpeedLabel;
    @FXML
    private TextField straightTextField, backwardTextField;

    private NewMapController newMapController;
    private List<String> elements;
    private double scale;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        straightPane.setCollapsible(false);
        backwardPane.setCollapsible(false);

        backwardVertexALabel.setStyle("-fx-text-fill: blue");
        backwardVertexBLabel.setStyle("-fx-text-fill: blue");
        backwardRoadClassLabel.setStyle("-fx-text-fill: blue");
        backwardTextField.setStyle("-fx-text-fill: blue");

        roadClassComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        startComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onComboBoxSelected(endComboBox, startComboBox, backwardVertexBLabel, elements);
            }
        });

        endComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onComboBoxSelected(startComboBox, endComboBox, backwardVertexALabel, elements);
            }
        });

        Platform.runLater(() -> {
            elements = newMapController.getLabelTitles();
            loadElementsToComboBox();
        });
    }

    private void setSpeedLimit(int roadClass, Label label) {
        String invariable = "Разрешенная скорость:";
        switch (roadClass) {
            case 1 -> invariable += " 120 км/ч";
            case 2 -> invariable += " 90 км/ч";
            case 3 -> invariable += " 80 км/ч";
            case 4 -> invariable += " 60 км/ч";
            case 5 -> invariable += " 40 км/ч";
        }
        label.setText(invariable);
    }

    @FXML
    private void onStraightTextFieldTextEntered() {
        String text = straightTextField.getText();
        if (text.matches("\\d+")) {
            backwardTextField.setText(text);
            straightTextField.setStyle("-fx-text-fill: black");
        } else {
            straightTextField.setStyle("-fx-text-fill: red");
            backwardTextField.setText(null);
        }
    }

    @FXML
    private void onDirectionCheckBoxClick() {
        if (directionCheckBox.isSelected()) {
            backwardPane.setVisible(false);
        } else {
            backwardPane.setVisible(true);
        }
    }

    @FXML
    private void onRoadClassComboBoxSelected() {
        int selectedElement = roadClassComboBox.getSelectionModel().getSelectedItem();
        backwardRoadClassLabel.setText("" + selectedElement);
        setSpeedLimit(selectedElement, straightSpeedLabel);
        setSpeedLimit(selectedElement, backwardSpeedLabel);
    }

    public void onComboBoxSelected(ComboBox<String> first, ComboBox<String> second, Label label, List<String> elements) {
        String selected = first.getSelectionModel().getSelectedItem();
        first.setItems(FXCollections.observableArrayList(elements));
        String value = second.getSelectionModel().getSelectedItem();
        label.setText(value);
        second.getItems().remove(selected);
        first.getItems().remove(value);
        first.getSelectionModel().select(selected);
    }

    @FXML
    private void onClearDataButtonClick() {
        startComboBox.setValue(null);
        endComboBox.setValue(null);
        roadClassComboBox.setValue(0);
        straightTextField.setText(null);
        backwardRoadClassLabel.setText(null);
        backwardTextField.setText(null);
    }

    @FXML
    private void onAddRoadButtonClick() {
        if (startComboBox.getValue() != null
                && endComboBox.getValue() != null
                && roadClassComboBox.getValue() != null
                && !straightTextField.getText().isEmpty()
                && straightTextField.getText().matches("\\d+")) {

            Vertex startVertex = findElementInVertexes(startComboBox);
            Vertex endVertex = findElementInVertexes(endComboBox);

            if (directionCheckBox.isSelected()) {
                saveRoadToCollection(startVertex, endVertex, 10, 10, 10, 10, 20);
            } else {
                //todo финальная версия, но хотелось бы чтобы при угле +- 90 градусов ровнее отображалось
                if (endVertex.getMousePositionX() > startVertex.getMousePositionX() && endVertex.getMousePositionY() > startVertex.getMousePositionY()) {
                    saveRoadToCollection(startVertex, endVertex, 15, 10, 10, 5, 20);
                    saveRoadToCollection(endVertex, startVertex, 5, 10, 10, 15, -20);
                } else if (endVertex.getMousePositionX() < startVertex.getMousePositionX() && endVertex.getMousePositionY() < startVertex.getMousePositionY()) {
                    saveRoadToCollection(startVertex, endVertex, 5, 10, 10, 15, 20);
                    saveRoadToCollection(endVertex, startVertex, 15, 10, 10, 5, -20);
                } else if (endVertex.getMousePositionX() > startVertex.getMousePositionX() && endVertex.getMousePositionY() < startVertex.getMousePositionY()) {
                    saveRoadToCollection(startVertex, endVertex, 10, 5, 5, 10, 20);
                    saveRoadToCollection(endVertex, startVertex, 10, 15, 15, 10, -20);
                } else if (endVertex.getMousePositionX() < startVertex.getMousePositionX() && endVertex.getMousePositionY() > startVertex.getMousePositionY()) {
                    saveRoadToCollection(startVertex, endVertex, 10, 15, 15, 10, 20);
                    saveRoadToCollection(endVertex, startVertex, 10, 5, 5, 10, -20);
                }
            }

            Stage stage = (Stage) startComboBox.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Заполните данные");
            alert.setHeaderText(null);
            alert.setContentText("Заполнены не все поля или заполнены неверно!");

            alert.showAndWait();
        }

    }

    private void saveRoadToCollection(Vertex first, Vertex second, int startXOffset, int startYOffset, int endXOffset, int endYOffset, int labelOffset) {
        newMapController.setRoad(new Road(
                straightTextField.getText(),
                Double.parseDouble(straightSpeedLabel.getText().replaceAll("[^0-9]", "")),
                first.getMousePositionX(),
                first.getMousePositionY(),
                second.getMousePositionX(),
                second.getMousePositionY(),
                roadClassComboBox.getItems().get(roadClassComboBox.getItems().size() - roadClassComboBox.getValue()),
                scale,
                startXOffset,
                startYOffset,
                endXOffset,
                endYOffset,
                labelOffset
        ));
    }

    public Vertex findElementInVertexes(ComboBox<String> comboBox) {
        return newMapController
                .getVertexes()
                .stream()
                .filter(vertex -> vertex
                        .getLabel()
                        .getText()
                        .equals(comboBox.getSelectionModel().getSelectedItem()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }

    private void loadElementsToComboBox() {
        startComboBox.setItems(FXCollections.observableArrayList(elements));
        endComboBox.setItems(FXCollections.observableArrayList(elements));
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
    }
}
