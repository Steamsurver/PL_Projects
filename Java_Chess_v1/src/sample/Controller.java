package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import java.io.IOException;

public class Controller {


    //-------------------------------------------main_menu----------------------------------------
    @FXML
    public static AnchorPane pane_setting;
    @FXML
    private Label Label_settings;
    @FXML
    private Label Label_exit;
    @FXML
    void action_settings(MouseEvent event) throws IOException {
        Parent settings_root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        Scene scene_settings = new Scene(settings_root);
        Main.stage.setScene(scene_settings);
        Main.stage.show();

    }

    @FXML
    void action_exit(MouseEvent event) {
        Platform.exit();
    }


    //-----------------------------------------settings_menu--------------------------------------
    public ToggleGroup RB_Group;
    @FXML
    public static AnchorPane APane_settings;
    @FXML
    private FlowPane Flow_settings_pane;
    @FXML
    private Label Label_resolution, Label_setting_back, Setting_sign;
    @FXML
    private RadioButton Button_1600x900;
    @FXML
    private RadioButton Button_1400x900;
    @FXML
    private RadioButton Button_1280x720;
    @FXML
    private RadioButton Button_800x600;
    @FXML
    private RadioButton Button_640x480;

    public void Resolution_actionRB(ActionEvent actionEvent) {
            if(Button_1600x900.isSelected()){
                Main.stage.setWidth(1600);
                Main.stage.setHeight(900);
                Main.stage.setX(180);
                Main.stage.setY(60);
                Setting_sign.setLayoutX(1600/2 - 85);
                System.out.println(Setting_sign.getLayoutX() + "  " + Setting_sign.getLayoutY());

            }
            if(Button_1400x900.isSelected()){
                Main.stage.setWidth(1400);
                Main.stage.setHeight(900);
                Main.stage.setX(290);
                Main.stage.setY(90);
                Setting_sign.setLayoutX(1400/2 - 85);
                System.out.println(Setting_sign.getLayoutX() + "  " + Setting_sign.getLayoutY());
            }
            if(Button_1280x720.isSelected()){
                Main.stage.setWidth(1280);
                Main.stage.setHeight(720);
                Main.stage.setX(370);
                Main.stage.setY(160);
                Setting_sign.setLayoutX(1280/2 - 85);
                System.out.println(Setting_sign.getLayoutX() + "  " + Setting_sign.getLayoutY());
            }
            if(Button_800x600.isSelected()){
                Main.stage.setWidth(800);
                Main.stage.setHeight(600);
                Main.stage.setX(550);
                Main.stage.setY(220);
                Setting_sign.setLayoutX(800/2 - 85);
                System.out.println(Setting_sign.getLayoutX() + "  " + Setting_sign.getLayoutY());
            }
            if(Button_640x480.isSelected()){
                Main.stage.setWidth(640);
                Main.stage.setHeight(480);
                Main.stage.setX(600);
                Main.stage.setY(270);
                Setting_sign.setLayoutX(640/2 - 85);
                System.out.println(Setting_sign.getLayoutX() + "  " + Setting_sign.getLayoutY());
            }
    }


    @FXML
    void Action_setting_back(MouseEvent event) throws IOException {
        Parent MainMenu_root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene_MainMenu = new Scene(MainMenu_root);
        Main.stage.setScene(scene_MainMenu);
        Main.stage.show();
    }

    @FXML
    void action_resolution(MouseEvent event) {
        Flow_settings_pane.setVisible(true);
    }



    //---------------------------------------------------------------------------------


}

