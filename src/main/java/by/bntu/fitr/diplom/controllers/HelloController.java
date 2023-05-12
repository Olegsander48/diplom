package by.bntu.fitr.diplom.controllers;

import by.bntu.fitr.diplom.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Функциональность данного класса - обработка действий, происходящих в первоначальном окне управления открытыми вкладками
 *
 *  @author Prigodich A.N.
 *  @version 1.0.1.2
 * */
public class HelloController implements Initializable {
    @FXML
    private Button fileToolMenuButton, openFileToolMenuButton, helpToolMenuButton;

    @FXML
    private CheckBox toolBarCheckBox, statusBarCheckBox;
    @FXML
    private ToolBar mainToolBar, statusBar;

    @FXML
    private Pane pane;

    private static int mapCounter = 1;
    private NewMapController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileToolMenuButton.setTooltip(new Tooltip("Create new map"));
        fileToolMenuButton.setFocusTraversable(false);
        openFileToolMenuButton.setTooltip(new Tooltip("Open exist map"));
        openFileToolMenuButton.setFocusTraversable(false);
        helpToolMenuButton.setTooltip(new Tooltip("Get help"));
        helpToolMenuButton.setFocusTraversable(false);
        pane.setStyle("-fx-background-color: rgb(217, 217, 217)");
    }

    /**
     * метод по созданию и отображению окна создания новой карты
     * метод так же выполняет обработку закрытия окна
     */
    @FXML
    private void onFileMenuButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/new-map-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Карта " + mapCounter++);
        stage.setResizable(false);
        stage.getIcons().add(new Image(new File("src/main/resources/by/bntu/fitr/diplom/images/earth.png").toURI().toString()));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            controller = fxmlLoader.getController();
            controller.onCloseWindowAction(stage.getTitle());
        });
    }
    @FXML
    private void onAboutMenuItemClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/utility/about-program-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("О программе");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onFileMenuExitButtonCLick() {
        List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).toList();
        Stage stage = (Stage) open.get(open.size() - 1).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onOpenFileToolMenuButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/new-map-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        controller = fxmlLoader.getController();
        if (controller.loadDataFromFile(stage)) {
            stage.setTitle("Карта " + mapCounter++);
            stage.setResizable(false);
            stage.getIcons().add(new Image(new File("src/main/resources/by/bntu/fitr/diplom/images/earth.png").toURI().toString()));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event -> {
                event.consume();
                controller.onCloseWindowAction(stage.getTitle());
            });
        }
    }

    /**
     * метод скрывает верхний тулбар (который с 3 кнопками)
     */
    @FXML
    private void onToolBarCheckBoxClick() {
        mainToolBar.setVisible(toolBarCheckBox.isSelected());
    }

    /**
     * метод скрывает нижний статусбар (который содержит надпись status)
     */
    @FXML
    private void onStatusBarCheckBoxClick() {
        statusBar.setVisible(statusBarCheckBox.isSelected());
    }


}