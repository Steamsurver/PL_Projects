package Scenes;

import EventManager.EventObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import sample.GlobalVariable;
import sample.Main;
import sample.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_Settings implements Initializable {
    SettingObserver settingObserver = new SettingObserver();
    Boolean resolutionFlag = true;
    public ToggleGroup RBGroup;
    @FXML
    public AnchorPane aPaneSettings;
    @FXML
    public ImageView imageFoneSettings;
    @FXML
    private FlowPane flowSettingsPane;
    @FXML
    private Label labelResolution, labelSettingSign, fullScreenOnLabel, fullScreenOffLabel;
    @FXML
    private ImageView buttonBack;
    @FXML
    private RadioButton button1920X1080;
    @FXML
    private RadioButton button1600X900;
    @FXML
    private RadioButton button1280X720;
    @FXML
    private RadioButton button960Х540;
    @FXML
    private RadioButton button640Х360;

    public void resolutionActionRB(ActionEvent actionEvent) {
        if(button1920X1080.isSelected()){
            GlobalVariable.change_resolution(Main.stage, imageFoneSettings, 1920, 1080, 1, 1);
            GlobalVariable.change_text_resolution(labelSettingSign);
        }
        if(button1600X900.isSelected()){
            GlobalVariable.change_resolution(Main.stage, imageFoneSettings, 1600, 900, 290, 90);
            GlobalVariable.change_text_resolution(labelSettingSign);
        }
        if(button1280X720.isSelected()){
            GlobalVariable.change_resolution(Main.stage, imageFoneSettings, 1280, 720, 370, 160);
            GlobalVariable.change_text_resolution(labelSettingSign);
        }
        if(button960Х540.isSelected()){
            GlobalVariable.change_resolution(Main.stage, imageFoneSettings, 960, 540, 550, 220);
            GlobalVariable.change_text_resolution(labelSettingSign);
        }
        if(button640Х360.isSelected()){
            GlobalVariable.change_resolution(Main.stage, imageFoneSettings, 640, 360, 600, 270);
            GlobalVariable.change_text_resolution(labelSettingSign);
        }
    }
    @FXML
    void fullScreeOFF(MouseEvent event) {
        GlobalVariable.FullScreenOn = false;
        fullScreenOffLabel.setVisible(false);
        fullScreenOnLabel.setVisible(true);

        GlobalVariable.eventManager.notifyObservers();
    }

    @FXML
    void fullScreeON(MouseEvent event) {
        GlobalVariable.FullScreenOn = true;
        fullScreenOffLabel.setVisible(true);
        fullScreenOnLabel.setVisible(false);

        GlobalVariable.eventManager.notifyObservers();
    }


    @FXML
    void actionResolution(MouseEvent event) {
        if(resolutionFlag){
        flowSettingsPane.setVisible(true);
        resolutionFlag = false;
        }else{
            flowSettingsPane.setVisible(false);
            resolutionFlag = true;
        }
    }


    @FXML
    void buttonSettingsActionReleased(MouseEvent event)throws IOException {
        Utils.Resource.scenes.remove("scene_MainMenu");
        Utils.Resource.loadScene(null, "scene_MainMenu", "MainMenu.fxml");

        Main.stage.setScene(Utils.Resource.scenes.get("scene_MainMenu"));
        GlobalVariable.eventManager.notifyObservers();//обновляем настройки
        Main.stage.show();
    }
    @FXML
    void buttonBackActionEntered(MouseEvent event) {
        buttonBack.setImage(GlobalVariable.Button_Back_pressed);
    }

    @FXML
    void buttonBackActionExit(MouseEvent event) {
        buttonBack.setImage(GlobalVariable.Button_Back);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GlobalVariable.eventManager.registerObserver(settingObserver);//регистрируем наблюдателя за меню

        imageFoneSettings.setFitWidth(GlobalVariable.Resolution_Width);
        imageFoneSettings.setFitHeight(GlobalVariable.Resolution_Height);
        imageFoneSettings.setImage(GlobalVariable.fone_menu_name);

        labelSettingSign.setLayoutX(GlobalVariable.Resolution_Width/2 - 190/2);
        labelResolution.setFont(GlobalVariable.main_font);
        labelSettingSign.setFont(GlobalVariable.main_font);
        fullScreenOnLabel.setFont(GlobalVariable.main_font);
        fullScreenOffLabel.setFont(GlobalVariable.main_font);

        button1920X1080.setFont(GlobalVariable.main_font);
        button1920X1080.setScaleX(0.5);
        button1920X1080.setScaleY(0.5);


        button1600X900.setFont(GlobalVariable.main_font);
        button1600X900.setScaleX(0.5);
        button1600X900.setScaleY(0.5);
        button1600X900.setLayoutX(1);

        button1280X720.setFont(GlobalVariable.main_font);
        button1280X720.setScaleX(0.5);
        button1280X720.setScaleY(0.5);

        button960Х540.setFont(GlobalVariable.main_font);
        button960Х540.setScaleX(0.5);
        button960Х540.setScaleY(0.5);

        button640Х360.setFont(GlobalVariable.main_font);
        button640Х360.setScaleX(0.5);
        button640Х360.setScaleY(0.5);

        if(GlobalVariable.FullScreenOn){//надпись полноэкранный режим
            fullScreenOffLabel.setVisible(true);
        }else
            fullScreenOnLabel.setVisible(true);

    }

    //-----------------------------------------------Observer-----------------------------------------------

    public class SettingObserver implements EventObserver{
        @Override
        public void update(){
            Main.stage.setFullScreen(GlobalVariable.FullScreenOn);

            GlobalVariable.change_resolution(Main.stage, imageFoneSettings,
                    GlobalVariable.Resolution_Width, GlobalVariable.Resolution_Height,
                    GlobalVariable.Stage_LayoutX, GlobalVariable.Stage_LayoutY);
            GlobalVariable.change_text_resolution(labelSettingSign);
        }
    }

}
