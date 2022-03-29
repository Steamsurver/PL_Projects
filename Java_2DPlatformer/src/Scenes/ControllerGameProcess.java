package Scenes;

import CharacterClasses.Player;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import sample.GlobalVariable;
import sample.Main;
import sample.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class ControllerGameProcess{
    public static Scene scene_GameProcess;
    public static Pane gameRoot = new Pane();
    public static Player player;

    public static void Game_Init() throws IOException {
        player = new Player();
        player.GetRootChildren(gameRoot);


        scene_GameProcess = new Scene(gameRoot);
        Utils.Resource.load_scene(ControllerGameProcess.scene_GameProcess, "Game");
    }

    public static void Game_Update(){
        //-------------------------------событие по нажатии клавиш----------------------
        Utils.Resource.scenes.get("Game").setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                if(e.getCode() == KeyCode.W){
                    player.moveUp();
                }

                if(e.getCode() == KeyCode.A){
                    player.moveLeft();
                }

                if(e.getCode() == KeyCode.S){
                    player.moveDown();
                }

                if(e.getCode() == KeyCode.D){
                    player.moveRight();
                }
            }
        });




    }
}
