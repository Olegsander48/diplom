package by.bntu.fitr.diplom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false); //todo запрет изменять размер окна
        stage.getIcons().add(new Image(new File("src/main/resources/by/bntu/fitr/diplom/images/main-logo.png").toURI().toString()));
        stage.setTitle("Дипломный проект");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}