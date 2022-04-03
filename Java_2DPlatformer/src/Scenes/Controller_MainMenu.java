package Scenes;

import EventManager.EventObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sample.GlobalVariable;
import sample.Main;
import sample.Utils;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller_MainMenu implements Initializable {
    //-------------------------------------------Observer-----------------------------------------
    MainMenuObserver mainMenuObserver = new MainMenuObserver();//наблюдатель за меню
    //-------------------------------------------main_menu----------------------------------------
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ImageView imageFoneMenu;
    @FXML
    private Label labelVersion;
    @FXML
    private ImageView buttonNewGame;
    @FXML
    private ImageView buttonSetting;
    @FXML
    private ImageView buttonExit;
    @FXML
    private VBox vBoxButtons;

    @FXML
    void buttonExitActionReleased(MouseEvent event) {
        Platform.exit();
    }
    @FXML
    void buttonExitActionEntered(MouseEvent event) {
        buttonExit.setImage(GlobalVariable.Button_Exit_pressed);
    }
    @FXML
    void buttonExitActionExit(MouseEvent event) {
        buttonExit.setImage(GlobalVariable.Button_Exit);
    }



    @FXML
    void buttonSettingsActionReleased(MouseEvent event) {
        Main.stage.setScene(Utils.Resource.scenes.get("scene_Settings"));
        GlobalVariable.eventManager.notifyObservers();//обновляем настройки
        Main.stage.show();
    }
    @FXML
    void buttonSettingsActionEntered(MouseEvent event) {
        buttonSetting.setImage(GlobalVariable.Button_Settings_pressed);
    }
    @FXML
    void buttonSettingsActionExited(MouseEvent event) {
        buttonSetting.setImage(GlobalVariable.Button_Settings);
    }



    @FXML
    void buttonNewGameActionEntered(MouseEvent event) {
        buttonNewGame.setImage(GlobalVariable.Button_NGTexture_pressed);
    }
    @FXML
    void buttonNewGameActionExited(MouseEvent event) {
        buttonNewGame.setImage(GlobalVariable.Button_NGTexture);
    }
    @FXML
    void buttonNewGameActionReleased(MouseEvent mouseEvent) {
        Main.stage.setScene(Utils.Resource.scenes.get("Game"));
        Main.stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GlobalVariable.eventManager.registerObserver(mainMenuObserver);//регистрируем наблюдателя за меню
        imageFoneMenu.setFitWidth(GlobalVariable.Resolution_Width);
        imageFoneMenu.setFitHeight(GlobalVariable.Resolution_Height);
        imageFoneMenu.setImage(GlobalVariable.fone_menu_name);

        vBoxButtons.setPrefWidth(GlobalVariable.Resolution_Width);
        vBoxButtons.setPrefHeight(GlobalVariable.Resolution_Height);

        labelVersion.setFont(GlobalVariable.small_main_font);
        System.out.println("Start");
    }


    //-------------------------------------------------------------------------------------------------
    public class MainMenuObserver implements EventObserver {//Наблюдатель за меню

        @Override
        public void update(){
            vBoxButtons.setPrefWidth(GlobalVariable.Resolution_Width);
            vBoxButtons.setPrefHeight(GlobalVariable.Resolution_Height);

            GlobalVariable.change_resolution(Main.stage, imageFoneMenu,
                    GlobalVariable.Resolution_Width, GlobalVariable.Resolution_Height,
                    GlobalVariable.Stage_LayoutX, GlobalVariable.Stage_LayoutY);


            Main.stage.setFullScreen(GlobalVariable.FullScreenOn);
        }
    }
}

