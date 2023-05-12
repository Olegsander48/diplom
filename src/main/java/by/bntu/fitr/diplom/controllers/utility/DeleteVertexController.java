package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.model.Controller;
import by.bntu.fitr.diplom.model.Road;
import by.bntu.fitr.diplom.model.Vertex;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DeleteVertexController extends Controller implements Initializable {
    @FXML
    private ComboBox<String> vertexesComboBox;

    @FXML
    private AnchorPane deleteVertexAnchorPane;

    private NewMapController newMapController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteVertexAnchorPane.setStyle("-fx-background-color: rgb(105, 40, 40)");
        Platform.runLater(() -> {
            vertexesComboBox.setItems(convertList(newMapController.getVertexes()));
        });
    }

    @FXML
    private void onDeleteVertexButtonCLick() {
        if (vertexesComboBox.getValue() != null) {
            List<Vertex> vertexes = newMapController.getVertexes();
            Vertex removedVertex = vertexes.remove(vertexesComboBox.getItems().indexOf(vertexesComboBox.getValue()));
            vertexesComboBox.setItems(convertList(vertexes));
            newMapController.setVertexes(vertexes);
            newMapController.setRoads(checkRoadsForContact(removedVertex));
            newMapController.drawFigures();

            newMapController.getVertexes().forEach(System.out::println);
            newMapController.getRoads().forEach(System.out::println);

            newMapController.setCountOfRoads();

            Stage stage = (Stage) vertexesComboBox.getScene().getWindow();
            stage.close();
        }
    }

    private ObservableList<String> convertList(List<Vertex> arrayList) {
        return FXCollections.observableArrayList(arrayList
                .stream()
                .map(vertex -> vertex
                        .getLabel()
                        .getText())
                .collect(Collectors.toList()));
    }

    private List<Road> checkRoadsForContact(Vertex vertex) {
        return newMapController.getRoads()
                .stream()
                .filter(road -> (road.getStartPositionX() != vertex.getMousePositionX())
                        && (road.getStartPositionY() != vertex.getMousePositionY())
                        && (road.getEndPositionX() != vertex.getMousePositionX())
                        && (road.getEndPositionY() != vertex.getMousePositionY()))
                .collect(Collectors.toList());

    }

    @Override
    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }
}
