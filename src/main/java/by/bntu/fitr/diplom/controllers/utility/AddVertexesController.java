package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.model.SerializableShape;
import by.bntu.fitr.diplom.model.Vertex;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Функциональность данного класса - обработка действий, происходящих в окне создания вершины(населенный пункт, перекресток, дорожный объект)
 *
 * @author Prigodich A.N.
 * @version 1.0.1.2
 */
public class AddVertexesController implements Initializable {
    @FXML
    private TextField vertexNameTextField;
    @FXML
    private ComboBox<Integer> fontSizesComboBox;
    @FXML
    private Label mainTextLabel;
    @FXML
    private ColorPicker borderColorPicker, backgroundColorPicker;
    @FXML
    private BorderPane figureBorderPane;
    @FXML
    private Button suggestButton;

    private Boolean stateOfCityRadioBtn, stateOfRoadObjectRadioBtn;
    private Label vertexNameLabel;
    final private Vertex vertex = new Vertex();
    private double scale;

    private NewMapController newMapController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontSizesComboBox.setItems(FXCollections.observableArrayList(12, 16, 20, 24, 28, 32, 36, 52));

        Platform.runLater(() -> {
            if (stateOfCityRadioBtn) {
                mainTextLabel.setText("Введите название населенного пункта:");
                fontSizesComboBox.getSelectionModel().select(2);
                backgroundColorPicker.setValue(Color.YELLOW);
                borderColorPicker.setValue(Color.RED);

                onComboBoxSelected();

            } else if (stateOfRoadObjectRadioBtn) {
                mainTextLabel.setText("Введите название дорожного объекта:");
                fontSizesComboBox.getSelectionModel().select(3);
                backgroundColorPicker.setValue(Color.BLUE);
                borderColorPicker.setValue(Color.PURPLE);

                onComboBoxSelected();
            }
        });
    }

    /**
     * метод, отрисовывающий иконки создаваемых вершин в ImageView (окружность или треугольник с текстом)
     */
    @FXML
    private void onComboBoxSelected() {
        if (stateOfCityRadioBtn) {
            Circle circle = new Circle();
            figureBorderPane.setCenter(circle);
            circle.setFill(backgroundColorPicker.getValue());
            circle.setStroke(borderColorPicker.getValue());
            circle.setRadius((double) fontSizesComboBox.getValue() / 2);
            circle.setStrokeWidth((double) fontSizesComboBox.getValue() / 5);

            vertex.setShape(circle);

            vertexNameLabel = new Label("\tName");
            vertexNameLabel.setFont(new Font("System", 15));
            vertexNameLabel.setTextFill(Color.color(0, 0.5, 0));
            figureBorderPane.setRight(vertexNameLabel);

            vertex.setLabel(vertexNameLabel);

        } else if (stateOfRoadObjectRadioBtn) {
            Polygon polygon = new Polygon();
            figureBorderPane.setCenter(polygon);
            polygon.setFill(backgroundColorPicker.getValue());
            polygon.setStroke(borderColorPicker.getValue());
            setPolygonSides(polygon, 0, 0, (double) fontSizesComboBox.getValue() / 2, 3);
            polygon.setStrokeWidth((double) fontSizesComboBox.getValue() / 5);

            vertex.setShape(polygon);

            vertexNameLabel = new Label("\tName");
            vertexNameLabel.setFont(new Font("System", 15));
            vertexNameLabel.setTextFill(Color.color(0.4, 0, 0));
            figureBorderPane.setRight(vertexNameLabel);

            vertex.setLabel(vertexNameLabel);
        }
    }

    /**
     * метод считывает введенный текст в поле TextField по нажатию Enter и сохраняет его в поле vertex
     */
    @FXML
    private void onTextFieldTextEntered() {
        vertexNameLabel.setText(vertexNameTextField.getText());
    }

    /**
     * метод обрабатывает действие нажатия на кнопку OK и отправку данных
     */
    @FXML
    private void onSuggestButtonClick() {
        vertex.setFontSize(fontSizesComboBox.getValue());
        vertex.setMousePositionX(vertex.getMousePositionX() - (vertex.getFontSize() / 2));
        vertex.setMousePositionY(vertex.getMousePositionY() - (vertex.getFontSize() / 2));
        if (vertexNameTextField.getText().equals("") && vertexNameLabel.getText().equals("\tName")) {
            String vertexName = stateOfCityRadioBtn ? "нп" : "до";
            vertex.getLabel().setText(vertexName + (newMapController.getVertexSize()));
        } else {
            vertex.getLabel().setText(vertexNameTextField.getText());
        }

        vertex.setCenterX(vertex.getMousePositionX() / scale);
        vertex.setCenterY(vertex.getMousePositionY() / scale);
        vertex.setSerializableShape(new SerializableShape(vertex));

        Stage stage = (Stage) mainTextLabel.getScene().getWindow();
        stage.close();

        newMapController.setVertex(vertex);
    }

    /**
     * метод рассчитывает треугольник по заданным точкам центра и количеству вершин
     */
    public static void setPolygonSides(Polygon polygon, double centerX, double centerY, double radius, int sides) {
        polygon.getPoints().clear();
        final double angleStep = Math.PI * 2 / sides;
        double angle = 0; // assumes one point is located directly beneat the center point
        for (int i = 0; i < sides; i++, angle += angleStep) {
            polygon.getPoints().addAll(
                    Math.sin(angle) * radius + centerX, // x coordinate of the corner
                    Math.cos(angle) * radius + centerY // y coordinate of the corner
            );
        }
    }

    /**
     * сеттер на получение состояние выбранностви cityRadioBtn (выбран ли нужный тип вершины)
     */
    public void setStateOfCityRadioBtn(Boolean stateOfCityRadioBtn) {
        this.stateOfCityRadioBtn = stateOfCityRadioBtn;
    }

    /**
     * сеттер на получение состояние выбранности roadObjectRadioBtn (выбран ли нужный тип вершины)
     */
    public void setStateOfRoadObjectRadioBtn(Boolean stateOfRoadObjectRadioBtn) {
        this.stateOfRoadObjectRadioBtn = stateOfRoadObjectRadioBtn;
    }

      /**
     * сеттер на установку вершине значений позиций мыши по Х
     */
    public void setValueOfMousePositionXCoordinate(double mousePositionX) {
        vertex.setMousePositionX(mousePositionX);
    }

    /**
     * сеттер на установку вершине значений позиций мыши по У
     */
    public void setValueOfMousePositionYCoordinate(double mousePositionY) {
        vertex.setMousePositionY(mousePositionY);
        vertex.setCenterY(mousePositionY);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }
}
