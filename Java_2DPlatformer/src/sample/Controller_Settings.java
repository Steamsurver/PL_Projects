package sample;

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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_Settings implements Initializable {
    public ToggleGroup RB_Group;
    @FXML
    public AnchorPane APane_settings;
    @FXML
    public ImageView Image_FoneSettings;
    @FXML
    private FlowPane Flow_settings_pane;
    @FXML
    private Label Label_resolution, Label_setting_back, Label_setting_sign;
    @FXML
    private RadioButton Button_1920x1080;
    @FXML
    private RadioButton Button_1600x900;
    @FXML
    private RadioButton Button_1280x720;
    @FXML
    private RadioButton Button_960х540;
    @FXML
    private RadioButton Button_640х360;

    public void Resolution_actionRB(ActionEvent actionEvent) {
        if(Button_1920x1080.isSelected()){
            GlobalVariable.change_resolution(Main.stage, Image_FoneSettings, 1920, 1080, 1, 1);
            GlobalVariable.change_text_resolution(Label_setting_sign);
        }
        if(Button_1600x900.isSelected()){
            GlobalVariable.change_resolution(Main.stage, Image_FoneSettings, 1600, 900, 290, 90);
            GlobalVariable.change_text_resolution(Label_setting_sign);
        }
        if(Button_1280x720.isSelected()){
            GlobalVariable.change_resolution(Main.stage, Image_FoneSettings, 1280, 720, 370, 160);
            GlobalVariable.change_text_resolution(Label_setting_sign);
        }
        if(Button_960х540.isSelected()){
            GlobalVariable.change_resolution(Main.stage, Image_FoneSettings, 960, 540, 550, 220);
            GlobalVariable.change_text_resolution(Label_setting_sign);
        }
        if(Button_640х360.isSelected()){
            GlobalVariable.change_resolution(Main.stage, Image_FoneSettings, 640, 360, 600, 270);
            GlobalVariable.change_text_resolution(Label_setting_sign);
        }
    }

    @FXML
    void Action_setting_back(MouseEvent event) throws IOException {
        Utils.Resource.scenes.remove("scene_MainMenu");
        Utils.Resource.load_scene(null, "scene_MainMenu", "MainMenu.fxml");

        Main.stage.setScene(Utils.Resource.scenes.get("scene_MainMenu"));
        Main.stage.show();
    }

    @FXML
    void action_resolution(MouseEvent event) {
        Flow_settings_pane.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image_FoneSettings.setFitWidth(GlobalVariable.Resolution_Width);
        Image_FoneSettings.setFitHeight(GlobalVariable.Resolution_Height);
        Image_FoneSettings.setImage(GlobalVariable.fone_menu_name);

        Label_setting_sign.setLayoutX(GlobalVariable.Resolution_Width/2 - 190/2);
        Label_resolution.setFont(GlobalVariable.main_font);
        Label_setting_back.setFont(GlobalVariable.main_font);
        Label_setting_sign.setFont(GlobalVariable.main_font);

        Button_1920x1080.setFont(GlobalVariable.main_font);
        Button_1920x1080.setScaleX(0.5);
        Button_1920x1080.setScaleY(0.5);


        Button_1600x900.setFont(GlobalVariable.main_font);
        Button_1600x900.setScaleX(0.5);
        Button_1600x900.setScaleY(0.5);
        Button_1600x900.setLayoutX(1);

        Button_1280x720.setFont(GlobalVariable.main_font);
        Button_1280x720.setScaleX(0.5);
        Button_1280x720.setScaleY(0.5);

        Button_960х540.setFont(GlobalVariable.main_font);
        Button_960х540.setScaleX(0.5);
        Button_960х540.setScaleY(0.5);

        Button_640х360.setFont(GlobalVariable.main_font);
        Button_640х360.setScaleX(0.5);
        Button_640х360.setScaleY(0.5);


    }
}
