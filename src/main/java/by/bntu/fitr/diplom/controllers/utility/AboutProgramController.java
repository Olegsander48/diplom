package by.bntu.fitr.diplom.controllers.utility;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.util.List;

public class AboutProgramController {
    @FXML
    private void onOkButtonClick() {
        List<Window> open = Stage.getWindows().stream().filter(Window::isShowing).toList();
        Stage stage = (Stage) open.get(open.size() - 1).getScene().getWindow();
        stage.close();
    }
}
