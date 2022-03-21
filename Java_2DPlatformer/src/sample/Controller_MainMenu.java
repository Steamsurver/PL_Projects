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
    Label Label_settings;
    @FXML
    Label Label_exit;

    @FXML
    void action_settings(MouseEvent event) throws IOException {
        Main.stage.setScene(Utils.Resource.scenes.get("scene_Settings"));
        Main.stage.show();

    }
    @FXML
    void action_exit(MouseEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image_FoneMenu.setFitWidth(GlobalVariable.Resolution_Width);
        Image_FoneMenu.setFitHeight(GlobalVariable.Resolution_Height);
        Image_FoneMenu.setImage(GlobalVariable.fone_menu_name);

        Label_settings.setLayoutX(GlobalVariable.Resolution_Width/2 - 191/2);
        Label_settings.setLayoutY(GlobalVariable.Resolution_Height/2 - 50);

        Label_exit.setLayoutX(GlobalVariable.Resolution_Width/2 - 117/2 + 20);
        Label_exit.setLayoutY(GlobalVariable.Resolution_Height/2);

        Label_settings.setFont(GlobalVariable.main_font);
        Label_exit.setFont(GlobalVariable.main_font);
    }



    //---------------------------------------------------------------------------------


}

