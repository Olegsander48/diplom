package by.bntu.fitr.diplom.controllers.utility;

import by.bntu.fitr.diplom.controllers.NewMapController;
import by.bntu.fitr.diplom.model.Road;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditRoadController implements Initializable {
    @FXML
    private Label oldDirectlyClassLabel, oldDirectlySpeedLabel, oldDirectlyLengthLabel;
    @FXML
    private Label newDirectlyClassLabel, newDirectlySpeedLabel, newDirectlyLengthLabel;
    @FXML
    private Label newReverseClassLabel, newReverseSpeedLabel, newReverseLengthLabel;
    @FXML
    private Label oldReverseClassLabel, oldReverseSpeedLabel, oldReverseLengthLabel;
    @FXML
    private TitledPane newDirectlyTitledPane, oldDirectlyTitledPane, newReverseTitledPane, oldReverseTitledPane;
    @FXML
    private Label directlyDirectionLabel, reverseDirectionLabel;
    @FXML
    private Button replaceBtn;

    private NewMapController newMapController;
    private List values;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newDirectlyTitledPane.setCollapsible(false);
        oldDirectlyTitledPane.setCollapsible(false);
        newReverseTitledPane.setCollapsible(false);
        oldReverseTitledPane.setCollapsible(false);

        Platform.runLater(() -> {
            directlyDirectionLabel.setText(values.get(0) + " -" + values.get(1));
            oldDirectlyClassLabel.setText(values.get(2).toString());
            oldDirectlySpeedLabel.setText(values.get(4).toString());
            oldDirectlyLengthLabel.setText(values.get(6).toString());

            newDirectlyClassLabel.setText(values.get(3).toString());
            newDirectlySpeedLabel.setText(values.get(5).toString());
            newDirectlyLengthLabel.setText(values.get(7).toString());

            reverseDirectionLabel.setText(values.get(1) + " -" + values.get(0));
            newReverseClassLabel.setText(values.get(3).toString());
            newReverseSpeedLabel.setText(values.get(5).toString());
            newReverseLengthLabel.setText(values.get(7).toString());

            oldReverseClassLabel.setText(values.get(2).toString());
            oldReverseSpeedLabel.setText(values.get(4).toString());
            oldReverseLengthLabel.setText(values.get(6).toString());
        });
    }

    @FXML
    private void onReplaceButtonClick() {
        newMapController.getRoads().remove(values.get(8));
        newMapController.setRoad((Road) values.get(9));
        if (values.get(10) != null) {
            newMapController.getRoads().remove(values.get(10));
            newMapController.setRoad((Road) values.get(11));
        }
        onLeaveBtnClick();
    }

    @FXML
    private void onLeaveBtnClick() {
        newMapController.closeWindow(replaceBtn);
    }

    public void setNewMapController(NewMapController newMapController) {
        this.newMapController = newMapController;
    }

    public void setNewValues(List value) {
        this.values = value;
    }
}
