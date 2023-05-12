module by.bntu.fitr.diplom {
    requires javafx.controls;
    requires javafx.fxml;
    requires poi;

    opens by.bntu.fitr.diplom to javafx.fxml;
    exports by.bntu.fitr.diplom;
    exports by.bntu.fitr.diplom.controllers;
    opens by.bntu.fitr.diplom.controllers to javafx.fxml;
    exports by.bntu.fitr.diplom.controllers.utility;
    opens by.bntu.fitr.diplom.controllers.utility to javafx.fxml;
    exports by.bntu.fitr.diplom.controllers.algorithms;
    opens by.bntu.fitr.diplom.controllers.algorithms to javafx.fxml;
    exports by.bntu.fitr.diplom.model;
    opens by.bntu.fitr.diplom.model to javafx.base;
}