package by.bntu.fitr.diplom.controllers;

import by.bntu.fitr.diplom.HelloApplication;
import by.bntu.fitr.diplom.controllers.utility.AddVertexesController;
import by.bntu.fitr.diplom.model.Controller;
import by.bntu.fitr.diplom.model.Road;
import by.bntu.fitr.diplom.model.SerializableShape;
import by.bntu.fitr.diplom.model.Vertex;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * Функциональность данного класса - обработка действий, происходящих в окне создания карты и отправка нужных данных в другой контроллер
 *
 *  @author Prigodich A.N.
 *  @version 1.0.1.2
 * */
public class NewMapController implements Initializable {
    @FXML
    private Button blockMapBtn, addConnectionBtn, deleteConnectionBtn, deleteVertexBtn, addSubstrateBtn;
    @FXML
    private TitledPane substratePane, internetPane, vertexPane, connectionPane, parametersPane, algorithmsPane;
    @FXML
    private ComboBox<Integer> scalesComboBox;
    @FXML
    private RadioButton addRadioBtn, hideRadioBtn, showRadioBtn, deleteRadioBtn;
    @FXML
    private ComboBox<String> searchersComboBox;
    @FXML
    private RadioButton cityRadioBtn, crossroadsRadioBtn, roadObjectRadioBtn;
    @FXML
    private AnchorPane resultAnchorPane;
    @FXML
    private RadioButton shortestParamRadioBtn, fastestParamRadioBtn, economicalParamRadioBtn;
    @FXML
    private ImageView blockMapStateBarImage, saveMapStateBarImage, printMapStateBarImage, helpStateBarImage;
    @FXML
    private ImageView mainMapImageView, blockMapImage;
    @FXML
    private ScrollPane mainMapImageScrollPane;
    @FXML
    private Label coordinatesLabel, vertexesCountlabel, roadsCountLabel;
    @FXML
    private AnchorPane algorithmsAnchorPane, paramsAnchorPane;
    @FXML
    private ToolBar mainMapInfoToolBar;

    private double height, width; //высота и ширина пространства, содержащего изображение
    private Image loadedImage;
    private List<Vertex> vertexes = new ArrayList<>();  //тут происходит создание пустого множества
    private List<Road> roads = new ArrayList<>(); //коллекция, хранящая все дороги
    private double mousePositionX, mousePositionY; //изначальные координаты мыши при нажатии
    private double scalesStep;
    private ToggleGroup parametersRadioPane;
    private int vertexSize = 1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        substratePane.setCollapsible(false);
        scalesComboBox.setItems(FXCollections.observableArrayList(200, 150, 125, 100, 75, 50));
        scalesComboBox.getSelectionModel().select(3);

        ToggleGroup substrateRadioPane = new ToggleGroup();
        addRadioBtn.setToggleGroup(substrateRadioPane);
        addRadioBtn.setSelected(true);
        addRadioBtn.setOnAction(actionEvent -> addSubstrateBtn.setText(addRadioBtn.getText()));
        hideRadioBtn.setToggleGroup(substrateRadioPane);
        hideRadioBtn.setOnAction(actionEvent -> addSubstrateBtn.setText(hideRadioBtn.getText()));
        showRadioBtn.setToggleGroup(substrateRadioPane);
        showRadioBtn.setOnAction(actionEvent -> addSubstrateBtn.setText(showRadioBtn.getText()));
        deleteRadioBtn.setToggleGroup(substrateRadioPane);
        deleteRadioBtn.setOnAction(actionEvent -> addSubstrateBtn.setText(deleteRadioBtn.getText()));

        internetPane.setCollapsible(false);
        searchersComboBox.setItems(FXCollections.observableArrayList("google.com", "yandex.com"));

        vertexPane.setCollapsible(false);
        ToggleGroup vertexesRadioPane = new ToggleGroup();
        cityRadioBtn.setToggleGroup(vertexesRadioPane);
        cityRadioBtn.setSelected(true);
        crossroadsRadioBtn.setToggleGroup(vertexesRadioPane);
        roadObjectRadioBtn.setToggleGroup(vertexesRadioPane);

        resultAnchorPane.setStyle("-fx-background-color: rgb(242, 238, 179)");
        algorithmsAnchorPane.setStyle("-fx-background-color: rgb(237, 232, 157)");
        paramsAnchorPane.setStyle("-fx-background-color: rgb(237, 232, 157)");

        connectionPane.setCollapsible(false);

        parametersPane.setCollapsible(false);
        parametersRadioPane = new ToggleGroup();
        shortestParamRadioBtn.setToggleGroup(parametersRadioPane);
        shortestParamRadioBtn.setSelected(true);
        fastestParamRadioBtn.setToggleGroup(parametersRadioPane);
        economicalParamRadioBtn.setToggleGroup(parametersRadioPane);

        algorithmsPane.setCollapsible(false);

        mainMapInfoToolBar.setStyle("-fx-background-color: rgb(131, 235, 159)");

        checkCollectionForSize(size -> size >= 2, addConnectionBtn, vertexes);
        checkCollectionForSize(size -> size != 0, deleteVertexBtn, vertexes);
        checkCollectionForSize(size -> size > 0, deleteConnectionBtn, roads);
    }

    /**
     * метод выполянет блокировку карты(отключение нужного и ненужного функционала)
     */
    @FXML
    private void onBlockMapButtonClick() {
        Image open = new Image(new File("src/main/resources/by/bntu/fitr/diplom/images/ci19открыто.png").toURI().toString());
        Image close = new Image(new File("src/main/resources/by/bntu/fitr/diplom/images/ci18закрыто.png").toURI().toString());
        if (blockMapBtn.getText().equals("Блокировать карту")) {
            blockMapBtn.setText("Редактировать карту");
            blockMapImage.setImage(open);
            blockMapStateBarImage.setImage(close);
            cityRadioBtn.setDisable(true);
            crossroadsRadioBtn.setDisable(true);
            roadObjectRadioBtn.setDisable(true);
            deleteVertexBtn.setDisable(false);
        } else {
            blockMapBtn.setText("Блокировать карту");
            blockMapImage.setImage(close);
            blockMapStateBarImage.setImage(open);
            cityRadioBtn.setDisable(false);
            crossroadsRadioBtn.setDisable(false);
            roadObjectRadioBtn.setDisable(false);
            deleteVertexBtn.setDisable(true);
        }
    }

    /**
     * метод выполянет октрытие диалогового окна выбора файла для загруки в качестве подложки карты
     */
    @FXML
    private void onAddSubstrateBtnClick(ActionEvent event) {
        if (addRadioBtn.isSelected()) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Открыть изображение");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("All Images", "*.*")
            );
            File substrate = fileChooser.showOpenDialog(stage);
            if (substrate != null) {
                loadedImage = new Image(substrate.toURI().toString());

                mainMapImageView.setImage(loadedImage);
                mainMapImageScrollPane.setContent(mainMapImageView);
                scalesComboBox.getSelectionModel().select(3);

                width = mainMapImageView.getFitWidth();
                height = mainMapImageView.getFitHeight();
            }
        } else if (hideRadioBtn.isSelected()) {
            mainMapImageView.setImage(null);
        } else if (showRadioBtn.isSelected()) {
            mainMapImageView.setImage(loadedImage);
        } else if (deleteRadioBtn.isSelected()) {
            loadedImage = null;
            mainMapImageView.setImage(null);
            scalesComboBox.getSelectionModel().select(3);
            vertexes = new ArrayList<>();
            mainMapImageScrollPane.setContent(null);
            roads = new ArrayList<>();
            vertexSize = 1;
        }
    }

    /**
     * метод изменения масштаба изображения путем изменнеия выбранного значения в комбобоксе
     */
    @FXML
    private void onScalesComboBoxChanged() {
        scalesStep = (double) scalesComboBox.getSelectionModel().getSelectedItem() / 100;
        mainMapImageView.setFitHeight(height * scalesStep);
        mainMapImageView.setFitWidth(width * scalesStep);

        vertexes.forEach(vertex -> {
            vertex.setMousePositionX(vertex.getCenterX() * scalesStep);
            vertex.setMousePositionY(vertex.getCenterY() * scalesStep);
        });

        roads.forEach(road -> {
            road.setStartPositionX(road.getStartX() * scalesStep);
            road.setStartPositionY(road.getStartY() * scalesStep);
            road.setEndPositionX(road.getEndX() * scalesStep);
            road.setEndPositionY(road.getEndY() * scalesStep);
        });

        drawFigures();
    }

    /**
     * метод, срабатывающий при нажатии на крестик окна с основной картой
     */
    public void onCloseWindowAction(String stageTitle) {
        loadNewWindow("views/utility/save-map-view.fxml",
                stageTitle,
                "src/main/resources/by/bntu/fitr/diplom/images/ci49дискета.png",
                Modality.APPLICATION_MODAL,
                0);
    }

    /**
     * Метод обработки нажатия по ImageView
     * открывается окно создания вершины для ввода данных, которые необходимо будет разместить на карте
     */
    @FXML
    private void onImageViewMouseClick(MouseEvent event) {
        scalesStep = (double) scalesComboBox.getSelectionModel().getSelectedItem() / 100;
        mousePositionX = event.getX();
        mousePositionY = event.getY();
        if (blockMapBtn.getText().equals("Блокировать карту")) {
            if (!crossroadsRadioBtn.isSelected()) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/utility/add-vertexes-view.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                AddVertexesController controller = fxmlLoader.getController();
                controller.setStateOfCityRadioBtn(cityRadioBtn.isSelected());
                controller.setStateOfRoadObjectRadioBtn(roadObjectRadioBtn.isSelected());

                controller.setValueOfMousePositionXCoordinate(mousePositionX);
                controller.setValueOfMousePositionYCoordinate(mousePositionY);
                mousePositionX = mousePositionX / scalesStep; //задается позиция мыши относительно масштабирования
                mousePositionY = mousePositionY / scalesStep; //задается позиция мыши относительно масштабирования
                controller.setScale(scalesStep);
                controller.setNewMapController(this); //ссылка на контроллер

                Stage stage = new Stage();
                stage.initModality(Modality.WINDOW_MODAL);
                if (cityRadioBtn.isSelected()) {
                    stage.setTitle("Добавить населенный пункт");
                } else if (roadObjectRadioBtn.isSelected()) {
                    stage.setTitle("Добавить дорожный объект");
                }

                stage.setResizable(false);
                stage.getIcons().add(new Image(new File("src/main/resources/by/bntu/fitr/diplom/images/add_vertex.png").toURI().toString()));
                stage.setScene(scene);
                stage.show();
            } else {
                Rectangle rectangle = new Rectangle();
                rectangle.setHeight(20);
                rectangle.setWidth(20);
                rectangle.setFill(Color.GREEN);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(2);
                Vertex<Rectangle> vertex = new Vertex<>();
                vertex.setShape(rectangle);
                vertex.setFontSize(40);

                Label label = new Label(" п" + vertexSize++);
                label.setTextFill(Color.color(0.72, 0.05, 0.41));
                label.setFont(new Font("System", 15));
                vertex.setLabel(label);

                vertex.setMousePositionX(mousePositionX - 10);
                vertex.setMousePositionY(mousePositionY - 10);

                mousePositionX = (mousePositionX - 10) / scalesStep; //задается позиция мыши относительно масштабирования
                mousePositionY = (mousePositionY - 10) / scalesStep; //задается позиция мыши относительно масштабирования
                vertex.setCenterX(mousePositionX);
                vertex.setCenterY(mousePositionY);
                vertex.setSerializableShape(new SerializableShape(vertex));
                vertexes.add(vertex);
                drawFigures();
            }
        }
    }

    /**
     * суть в том, что у нас изначально считывается нажатие на экран, только после этого происходят действия с
     * передачей заполненного объекта vertex обратно в окно с расстановкой точек
     * */
    @FXML
    private void onMouseDragDetected(MouseEvent event) {
        coordinatesLabel.setText("Координаты: X=" + Math.round(event.getX()) + " Y=" + Math.round(event.getY()));

        checkCollectionForSize(size -> size >= 2, addConnectionBtn, vertexes);
        checkCollectionForSize(size -> size != 0, deleteVertexBtn, vertexes);
        checkCollectionForSize(size -> size > 0, deleteConnectionBtn, roads);
    }

    /**
     * Метод добавления в коллекцию только-что добавленной вершины
     * @param vertex
     */
    public void setVertex(Vertex vertex) {
        vertexes.add(vertex);
        vertexSize++;
        drawFigures();
    }

    public void setRoad(Road road) {
        roads.add(road);
        drawFigures();
    }

    public void removeRoad(Road road) {
        roads.remove(road);
        drawFigures();
    }

    /**
     * метод, отрисовывающий фигуру
     * */
    @FXML
    public void drawFigures() {
        vertexesCountlabel.setText("Количество вершин: " + vertexes.size());
        setCountOfRoads();

        double vValue = mainMapImageScrollPane.vvalueProperty().get();
        double hValue = mainMapImageScrollPane.hvalueProperty().get();

        ImageView imageView = mainMapImageView;
        Group group = new Group();
        group.getChildren().add(imageView);

        roads.forEach(road -> {
            Line line = new Line();

            line.setStartX(road.getStartPositionX() + road.getStartXOffset());
            line.setStartY(road.getStartPositionY() + road.getStartYOffset());
            line.setEndX(road.getEndPositionX() + road.getEndXOffset());
            line.setEndY(road.getEndPositionY() + road.getEndYOffset());

            line.setStrokeWidth(road.getStrokeWidth());
            group.getChildren().add(line);

            Label label = new Label(road.getDistance());
            label.setStyle("-fx-text-fill: rgb(241,134,0);" + "-fx-font-weight: bold");
            label.setFont(new Font("System", 15));
            label.setLayoutX(Math.abs(line.getEndX() + line.getStartX()) / 2 + road.getLabelOffset());
            label.setLayoutY(Math.abs(line.getEndY() + line.getStartY()) / 2);
            group.getChildren().add(label);

        });

        vertexes.forEach(vertex1 -> {
            BorderPane vertexWithLabel = new BorderPane();
            vertexWithLabel.setRight(vertex1.getLabel());

            vertexWithLabel.setLayoutX(vertex1.getMousePositionX());
            vertexWithLabel.setLayoutY(vertex1.getMousePositionY());

            Shape shape = vertex1.getShape();
            vertexWithLabel.setCenter(shape);

            group.getChildren().add(vertexWithLabel);
        });

        mainMapImageScrollPane.setContent(group);

        mainMapImageScrollPane.setVvalue(vValue);
        mainMapImageScrollPane.setHvalue(hValue);
    }

    @FXML
    private void onDeleteVertexBtnClick() {
        if (vertexes.size() != 0) {
            loadNewWindow("views/utility/delete-vertex-view.fxml",
                    "Удалить вершину?",
                    null,
                    Modality.APPLICATION_MODAL,
                    0);
        }
    }

    @FXML
    private void onAddConnectionBtnClick() {
        loadNewWindow("views/utility/add-road-view.fxml",
                "Добавить маршрут",
                null,
                Modality.WINDOW_MODAL,
                scalesStep);
    }

    @FXML
    private void onDeleteConnectionBtnClick() {
        loadNewWindow("views/utility/delete-road-view.fxml",
                "Удалить маршрут?",
                null,
                Modality.APPLICATION_MODAL,
                0);
    }

    private void checkCollectionForSize(Predicate<Integer> predicate, Button button, List collection) {
        if (predicate.test(collection.size())) {
            button.setDisable(false);
        } else {
            button.setDisable(true);
        }
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    public void setVertexes(List<Vertex> vertexes) {
        this.vertexes = vertexes;
    }

    public List<String> getLabelTitles() {
        return vertexes.stream()
                .map(vertex -> vertex
                        .getLabel()
                        .getText())
                .toList();
    }

    public void setCountOfRoads() {
        roadsCountLabel.setText(String.valueOf(roads.size()));
    }

    public int getVertexSize() { //todo данный метод вызывает сомнения в его необходимости
        return vertexSize;
    }

    @FXML
    private void onShortestNetworkButtonClick() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Данная функция в разработке");
        alert.setHeaderText(null);
        alert.setContentText("Данный алгоритм находится в разработке");
        alert.showAndWait();
    }

    @FXML
    private void onFloydAlgorithmButtonClick() {
        loadNewWindow("views/utility/floyd-algorithm-view.fxml",
                "Алгоритм флойда: " + ((Stage) addSubstrateBtn.getScene().getWindow()).getTitle(),
                "src/main/resources/by/bntu/fitr/diplom/images/add_vertex.png",
                Modality.WINDOW_MODAL,
                0);

        //todo расскоментировать по завершению работы над алгоритмом флойда
        /*if (vertexes.size() >= 2 && roads.size() != 0) {
            loadNewWindow("views/utility/floyd-algorithm-view.fxml",
                    "Алгоритм флойда: " + ((Stage) addSubstrateBtn.getScene().getWindow()).getTitle(),
                    "src/main/resources/by/bntu/fitr/diplom/images/add_vertex.png",
                    Modality.WINDOW_MODAL,
                    0);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Не создана транспортная сеть");
            alert.setHeaderText(null);
            alert.setContentText("Создайте транспортную сеть!");

            alert.showAndWait();
        }*/
    }

    @FXML
    private void onDijkstraAlgorithmButtonClick() {
        loadNewWindow("views/utility/input-dijkstra-view.fxml",
                "Выбор маршрута",
                "src/main/resources/by/bntu/fitr/diplom/images/add_vertex.png",
                Modality.APPLICATION_MODAL,
                0);
    }

    @FXML
    private void onReRouteButtonClick() {
        saveDataToFile(); //todo убрать вызов метода
    }

    public void saveDataToFile() {
        Stage stage = (Stage) addSubstrateBtn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить данные");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DP23Prig Files (*.dpprig)", "*.dpprig"),
                new FileChooser.ExtensionFilter("All Files (*.*)", "*.*")
        );
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedFile)))
            {
                scalesComboBox.getSelectionModel().select(3);
                onScalesComboBoxChanged();

                if (mainMapImageView.getImage() != null) {
                    oos.writeBoolean(true);
                    oos.writeObject(mainMapImageView.getImage().getUrl());
                    oos.writeDouble(width);
                    oos.writeDouble(height);
                } else {
                    oos.writeBoolean(false);
                }

                oos.writeObject(vertexes);
                oos.writeObject(roads);
                oos.writeDouble(scalesStep);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public boolean loadDataFromFile(Stage currentStage) {
        Stage stage = currentStage;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить данные");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DP23Prig Files (*.dpprig)", "*.dpprig"),
                new FileChooser.ExtensionFilter("All Files (*.*)", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile)))
            {
                scalesComboBox.getSelectionModel().select(3);
                onScalesComboBoxChanged();
                if (ois.readBoolean()) {
                    mainMapImageView.setImage(new Image((String) ois.readObject()));

                    width = ois.readDouble();
                    height = ois.readDouble();

                    mainMapImageView.setFitWidth(width);
                    mainMapImageView.setFitHeight(height);

                    mainMapImageScrollPane.setContent(mainMapImageView);
                    scalesComboBox.getSelectionModel().select(3);
                }
                vertexes = (List<Vertex>) ois.readObject();
                setValuesToVertexes();
                vertexSize = vertexes.size() + 1;
                roads = (List<Road>) ois.readObject();
                scalesStep = ois.readDouble();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            drawFigures();
            return true;
        } else {
            return false;
        }
    }

    private void setValuesToVertexes() {
        vertexes.forEach(vertex -> {
            SerializableShape shape = vertex.getSerializableShape();

            Label label = new Label(shape.getLabelText());
            label.setTextFill(Color.web(shape.getLabelColor()));
            label.setFont(new Font(shape.getLabelFontName(), shape.getLabelFontSize()));
            vertex.setLabel(label);

            if (shape.getShapeType().equals("Rectangle")) {
                Rectangle rectangle = new Rectangle();
                rectangle.setHeight(shape.getShapeSideLength());
                rectangle.setWidth(shape.getShapeSideLength());
                rectangle.setFill(Color.web(shape.getShapeFill()));
                rectangle.setStroke(Color.web(shape.getShapeStroke()));
                rectangle.setStrokeWidth(shape.getShapeStrokeWidth() / 4);
                vertex.setShape(rectangle);
            } else if (shape.getShapeType().equals("Circle")) {
                Circle circle = new Circle();
                circle.setFill(Color.web(shape.getShapeFill()));
                circle.setStroke(Color.web(shape.getShapeStroke()));
                circle.setRadius(shape.getShapeSideLength());
                circle.setStrokeWidth(shape.getShapeStrokeWidth());
                vertex.setShape(circle);
            } else if (shape.getShapeType().equals("Polygon")){
                Polygon polygon = new Polygon();
                polygon.setFill(Color.web(shape.getShapeFill()));
                polygon.setStroke(Color.web(shape.getShapeStroke()));
                AddVertexesController.setPolygonSides(polygon, 0, 0, shape.getShapeSideLength(), 3);
                polygon.setStrokeWidth(shape.getShapeStrokeWidth());
                vertex.setShape(polygon);
            }
        });
    }

    public RadioButton getShortestParamRadioBtn() {
        return shortestParamRadioBtn;
    }

    public RadioButton getFastestParamRadioBtn() {
        return fastestParamRadioBtn;
    }

    public RadioButton getEconomicalParamRadioBtn() {
        return economicalParamRadioBtn;
    }

    public void closeWindow(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    public ToggleGroup getParametersRadioPane() {
        return parametersRadioPane;
    }

    private void loadNewWindow(String resourceName, String pageTitle, String iconPathFromContentRoot, Modality modality, double scale) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(resourceName));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle(pageTitle);
        stage.setResizable(false);
        stage.initModality(modality);
        if (iconPathFromContentRoot != null) {
            stage.getIcons().add(new Image(new File(iconPathFromContentRoot).toURI().toString()));
        }

        ((Controller) fxmlLoader.getController()).setNewMapController(this);
        ((Controller) fxmlLoader.getController()).setScale(scale);

        stage.setScene(scene);
        stage.show();
    }
}
