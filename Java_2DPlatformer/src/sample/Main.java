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
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    public static void main(String[] args)
    {
        launch(args);
    }
    public static Stage stage;
    Parent root_settings;
    Parent root_MainMenu;


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);//Отключение возможности менять размер окна с помощью мыши
        GlobalVariable.Set_Setting(primaryStage);//Установка настроек
        stage = primaryStage;//главный узел для изменений

        primaryStage.setScene(Utils.Resource.scenes.get("scene_MainMenu"));
        primaryStage.show();
    }



    @Override
    public void init() throws Exception {//не стоит в нем писать графический интерфейс
        GlobalVariable.reader();//Сразу списываем из конфигов глобальные переменные
        Utils.Resource.load_scene(root_MainMenu, "scene_MainMenu", "MainMenu.fxml");
        Utils.Resource.load_scene(root_settings, "scene_Settings", "Settings.fxml");

        super.init();
    }





    @Override
    public void stop() throws Exception {
        GlobalVariable.writer();
        super.stop();
    }

}
