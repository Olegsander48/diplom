package by.bntu.fitr.diplom.controllers.utility;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutProgramController {
    @FXML
    private Button suggestButton;
    @FXML
    private void onOkButtonClick() {
        Stage stage = (Stage) suggestButton.getScene().getWindow();
        stage.close();
    }
}
