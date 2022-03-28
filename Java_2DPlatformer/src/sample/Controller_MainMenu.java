package sample;

import EventManager.EventManager;
import EventManager.EventObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller_MainMenu implements Initializable {
    //-------------------------------------------Observer-----------------------------------------
    MainMenuObserver MenuObserver = new MainMenuObserver();//наблюдатель за меню
    //-------------------------------------------main_menu----------------------------------------
    @FXML
    public AnchorPane pane_setting;
    @FXML
    public ImageView Image_FoneMenu;
    @FXML
    private Label Label_version;
    @FXML
    private ImageView Button_NewGame;
    @FXML
    private ImageView Button_Setting;
    @FXML
    private ImageView Button_Exit;
    @FXML
    private VBox VBoxButtons;

    @FXML
    void Button_exit_action_released(MouseEvent event) {
        Platform.exit();
    }
    @FXML
    void Button_exit_action_entered(MouseEvent event) {
        Button_Exit.setImage(GlobalVariable.Button_Exit_pressed);
    }
    @FXML
    void Button_exit_action_exit(MouseEvent event) {
        Button_Exit.setImage(GlobalVariable.Button_Exit);
    }



    @FXML
    void Button_settings_action_released(MouseEvent event) {
        Main.stage.setScene(Utils.Resource.scenes.get("scene_Settings"));
        GlobalVariable.eventManager.notifyObservers();//обновляем настройки
        Main.stage.show();
    }
    @FXML
    void Button_settings_action_entered(MouseEvent event) {
        Button_Setting.setImage(GlobalVariable.Button_Settings_pressed);
    }
    @FXML
    void Button_settings_action_exited(MouseEvent event) {
        Button_Setting.setImage(GlobalVariable.Button_Settings);
    }



    @FXML
    void Button_NewGame_action_entered(MouseEvent event) {
        Button_NewGame.setImage(GlobalVariable.Button_NGTexture_pressed);
    }
    @FXML
    void Button_NewGame_action_exited(MouseEvent event) {
        Button_NewGame.setImage(GlobalVariable.Button_NGTexture);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GlobalVariable.eventManager.registerObserver(MenuObserver);//регистрируем наблюдателя за меню
        Image_FoneMenu.setFitWidth(GlobalVariable.Resolution_Width);
        Image_FoneMenu.setFitHeight(GlobalVariable.Resolution_Height);
        Image_FoneMenu.setImage(GlobalVariable.fone_menu_name);

        VBoxButtons.setPrefWidth(GlobalVariable.Resolution_Width);
        VBoxButtons.setPrefHeight(GlobalVariable.Resolution_Height);

        Label_version.setFont(GlobalVariable.small_main_font);

    }

    //-------------------------------------------------------------------------------------------------
    public class MainMenuObserver implements EventObserver {//Наблюдатель за меню

        @Override
        public void update(){
            VBoxButtons.setPrefWidth(GlobalVariable.Resolution_Width);
            VBoxButtons.setPrefHeight(GlobalVariable.Resolution_Height);

            GlobalVariable.change_resolution(Main.stage, Image_FoneMenu,
                    GlobalVariable.Resolution_Width, GlobalVariable.Resolution_Height,
                    GlobalVariable.Stage_LayoutX, GlobalVariable.Stage_LayoutY);


            Main.stage.setFullScreen(GlobalVariable.FullScreenOn);
        }
    }
}

