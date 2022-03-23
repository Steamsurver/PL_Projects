package sample;

import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.w3c.dom.events.Event;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_MainMenu implements Initializable {


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
    private VBox VBboxButtons;


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
        Button_Setting.setImage(GlobalVariable.Button_NGTexture);
        Main.stage.setScene(Utils.Resource.scenes.get("scene_Settings"));
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
        Image_FoneMenu.setFitWidth(GlobalVariable.Resolution_Width);
        Image_FoneMenu.setFitHeight(GlobalVariable.Resolution_Height);
        Image_FoneMenu.setImage(GlobalVariable.fone_menu_name);

        VBboxButtons.setPrefWidth(GlobalVariable.Resolution_Width);
        VBboxButtons.setPrefHeight(GlobalVariable.Resolution_Height);

        Label_version.setFont(GlobalVariable.small_main_font);
    }



    //---------------------------------------------------------------------------------


}

