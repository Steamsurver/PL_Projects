package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    @Override
    public void start(Stage stage) throws Exception{
        int FLAG = 0;

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Hello JavaFX");
        stage.setWidth(250);
        stage.setHeight(200);

        stage.show();

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
