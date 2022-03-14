package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;

public class Controller {
    int Flag = 1;
    @FXML
    private AnchorPane Pane_First;

    @FXML
    private Button button_1;

    @FXML
    void action_first(MouseEvent event) {
        if(Flag == 1){
            button_1.setText("You are pressed this button!");
            Flag = 0;
        }else
        if(Flag == 0){
            button_1.setText("You are pressed again!");
            Flag = 1;
        }else{
            Flag = 1;
        }

    }

}

