package Scenes;

import CharacterClasses.Player;
import EventManager.EventObserver;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import maps.MapController;
import sample.GlobalVariable;
import sample.Main;
import sample.Utils;

import java.io.IOException;


public class ControllerGameProcess{
    public static GameObserver gameObserver = new GameObserver();
    public static Scene sceneGameProcess;
    public static Pane gameRoot = new Pane();
    public static Player player = new Player();
    public static Camera camera = new PerspectiveCamera();
    public static String keyTrader = "";

    public static void gameInit() throws IOException {
        GlobalVariable.eventManager.registerObserver(gameObserver);


        MapController.loadMapLab1Level1(MapController.Lab1Level1);
        MapController.showMap(MapController.Lab1Level1, gameRoot, player);


        sceneGameProcess = new Scene(gameRoot);
        sceneGameProcess.setCamera(camera);
        Utils.Resource.loadScene(ControllerGameProcess.sceneGameProcess, "Game");

        player.animationInstalled("UP", Duration.millis(500), 3, 3, 0, player.getHeight(), player.getWidth(), player.getHeight());
        player.animationInstalled("LEFT", Duration.millis(500), 3, 3, 0, player.getHeight() * 2, player.getWidth(), player.getHeight());
        player.animationInstalled("DOWN", Duration.millis(500), 3, 3, 0, 0, player.getWidth(), player.getHeight());
        player.animationInstalled("RIGHT", Duration.millis(500), 3, 3, 0, player.getHeight() * 3, player.getWidth(), player.getHeight());

    }

    public static void gameUpdate(){
        GlobalVariable.eventManager.notifyObservers();//обновление формы


        //-------------------------------событие по нажатии клавиш----------------------
        Utils.Resource.scenes.get("Game").setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                if(e.getCode() == KeyCode.W){
                    keyTrader = "UP";
                }

                if(e.getCode() == KeyCode.A){
                    keyTrader = "LEFT";
                }

                if(e.getCode() == KeyCode.S){
                    keyTrader = "DOWN";
                }

                if(e.getCode() == KeyCode.D){
                    keyTrader = "RIGHT";
                }

                if(e.getCode() == KeyCode.ESCAPE){
                    Main.stage.setScene(Utils.Resource.scenes.get("scene_MainMenu"));
                }
            }
        });

        Utils.Resource.scenes.get("Game").setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                if(e.getCode() == KeyCode.W){
                    keyTrader = "";
                    player.animationStop("UP");
                }

                if(e.getCode() == KeyCode.A){
                    keyTrader = "";
                    player.animationStop("LEFT");
                }

                if(e.getCode() == KeyCode.S){
                    keyTrader = "";
                    player.animationStop("DOWN");
                }

                if(e.getCode() == KeyCode.D){
                    keyTrader = "";
                    player.animationStop("RIGHT");
                }

            }
        });

        if(keyTrader.equals("UP")){
            player.moveUp(MapController.Lab1Level1, gameRoot);
        }
        if(keyTrader.equals("DOWN")){
            player.moveDown(MapController.Lab1Level1, gameRoot);
        }
        if(keyTrader.equals("RIGHT")){
            player.moveRight(MapController.Lab1Level1, gameRoot);
        }
        if(keyTrader.equals("LEFT")){
            player.moveLeft(MapController.Lab1Level1, gameRoot);
        }


    }






















    //-------------------------------------------------------------------------------------------------
    public static class GameObserver implements EventObserver {//Наблюдатель за меню

        @Override
        public void update(){
            gameRoot.setPrefWidth(GlobalVariable.Resolution_Width);
            gameRoot.setPrefHeight(GlobalVariable.Resolution_Height);
            camera.translateZProperty().set(GlobalVariable.Resolution_Height-200);
            camera.translateXProperty().set(player.getScaleX() - (int)(GlobalVariable.Resolution_Width/2) + 32);
            camera.translateYProperty().set(player.getScaleY() - (int)(GlobalVariable.Resolution_Height/2) + 64);
            Main.stage.setFullScreen(GlobalVariable.FullScreenOn);
        }
    }
}
