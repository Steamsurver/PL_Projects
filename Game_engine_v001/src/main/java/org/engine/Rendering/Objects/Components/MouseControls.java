package org.engine.Rendering.Objects.Components;

import org.engine.GameController.MouseListener;
import org.engine.GameElements.Window;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Resources.Utils.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;

    public void pickupObject(GameObject go){
        this.holdingObject = go;
        Window.getCurrentScene().addGameObjectToScene(go);
    }

    public void place(){
        this.holdingObject = null;
    }

    @Override
    public void update(float dt) {
        if(holdingObject != null){
            holdingObject.transform.position.x = (float)MouseListener.getOrthoX();
            holdingObject.transform.position.y = (float)MouseListener.getOrthoY();
            holdingObject.transform.position.x = (int)(holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            holdingObject.transform.position.y = (int)(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;
        }

        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            place();
        }


    }
}
