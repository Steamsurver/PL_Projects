package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    public static AnchorPane pane_mm;
    @FXML
    public static AnchorPane pane_setting;
    @FXML
    public static Button button_1;
    @FXML
    public static Button button_2;

    //---------------------------------------------------------------------------------
    @FXML
    void action_first(MouseEvent event) throws IOException {
        Parent settings_root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene_settings = new Scene(settings_root);
        Main.stage.setScene(scene_settings);
        Main.stage.show();

    }

    @FXML
    void action_second(MouseEvent event) throws IOException {
        Parent menu_root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene_menu = new Scene(menu_root);
        Main.stage.setScene(scene_menu);
        Main.stage.show();
    }

}

