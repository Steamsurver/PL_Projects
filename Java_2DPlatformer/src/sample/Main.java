package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import Scenes.ControllerGameProcess;
import javafx.scene.Parent;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args)
    {
        launch(args);
    }
    public static Stage stage;
    Parent rootSettings;
    Parent rootMainMenu;


    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setResizable(false);//Отключение возможности менять размер окна с помощью мыши
        GlobalVariable.setSetting(primaryStage);//Установка настроек
        stage = primaryStage;//главный узел для изменений
        primaryStage.setScene(Utils.Resource.scenes.get("scene_MainMenu"));
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                ControllerGameProcess.gameUpdate();
            }
        };
        timer.start();

    }



    @Override
    public void init() throws Exception {//не стоит в нем писать графический интерфейс
        GlobalVariable.reader();//Сразу списываем из конфигов глобальные переменные
        Utils.Resource.loadScene(rootMainMenu, "scene_MainMenu", "MainMenu.fxml");
        Utils.Resource.loadScene(rootSettings, "scene_Settings", "Settings.fxml");
        ControllerGameProcess.gameInit();

        super.init();
    }





    @Override
    public void stop() throws Exception {
        GlobalVariable.writer();//выкидываем конфиги
        super.stop();
    }

}
