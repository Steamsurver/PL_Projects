package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args)
    {
        launch(args);
    }

    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        Parent first_root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene_main = new Scene(first_root);
        primaryStage.setScene(scene_main);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {//не стоит в нем писать графический интерфейс

        System.out.println("Тестовый вывод в консоль");
        super.init();
    }

    @Override
    public void stop() throws Exception {

        System.out.println("Тест закрытия приложения");
        super.stop();
    }
}
