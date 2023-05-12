package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.model.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Функциональность данного контроллера - обработка событий окна, всплывающего при закрытии окна сохранения карты
 * (у каждого окна созддания карты есть свое проиндексированное окно по закрытию с сохранением или без него)
 *
 *  @author Prigodich A.N.
 *  @version 1.0.1.2
 * */
public class SaveMapController extends Controller implements Initializable {
    @FXML
    private Pane saveMapPane;
    private NewMapController newMapController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveMapPane.setStyle("-fx-background-color: orange");
    }

    @FXML
    private void onSaveDataBtnClick() {
        newMapController.saveDataToFile();
        onDoNotSaveDataBtnClick();
    }

    /**
     * данный метод отвечает за нажатие на клавишу не сохранять.
     * По нему происходит закрытие двух последних окон - текущего  и предыдущего
     * */
    @FXML
    private void onDoNotSaveDataBtnClick() {
        List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).toList();
        Stage stage = (Stage) open.get(open.size() - 1).getScene().getWindow();
        stage.close();
        stage = (Stage) open.get(open.size() - 2).getScene().getWindow();
        stage.close();
    }

    @Override
    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }
}
