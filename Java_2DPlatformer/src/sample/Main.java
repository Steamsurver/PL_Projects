package sample;

import javafx.application.Application;
import Scenes.ControllerGameProcess;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
        ControllerGameProcess.Game_Update();//Апдейтер игровой сцены

    }



    @Override
    public void init() throws Exception {//не стоит в нем писать графический интерфейс
        GlobalVariable.reader();//Сразу списываем из конфигов глобальные переменные
        Utils.Resource.load_scene(root_MainMenu, "scene_MainMenu", "MainMenu.fxml");
        Utils.Resource.load_scene(root_settings, "scene_Settings", "Settings.fxml");
        ControllerGameProcess.Game_Init();

        super.init();
    }





    @Override
    public void stop() throws Exception {
        GlobalVariable.writer();//выкидываем конфиги
        super.stop();
    }

}
