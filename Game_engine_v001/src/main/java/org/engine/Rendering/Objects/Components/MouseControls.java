package org.engine.Rendering.Objects.Components;

import org.engine.GameController.KeyListener;
import org.engine.GameController.MouseListener;
import org.engine.GameElements.Window;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Resources.Utils.Settings;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    //управление мышью для игрового объекта
    GameObject holdingObject = null;

    //значения для сброса
    private float debounceTime = 0.05f;
    private float debounce = debounceTime;
    private transient GameObject lastPickedObj = null;

    public void pickupObject(GameObject go){
        if(this.holdingObject != null){
            this.holdingObject.destroy();
        }
        this.holdingObject = go;
        //небольшая прозрачность при выборе объекта
        holdingObject.getComponent(SpriteRender.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        this.holdingObject.addComponent(new NonPickable());//блочим, чтоб нельзя было выбрать
        Window.getCurrentScene().addGameObjectToScene(go);
    }

    public void place(){
        //размещение множества объектов с зажатой кнопкой
        GameObject newObj = this.holdingObject.copy();
        if(newObj.getComponent(StateMachine.class) != null){//обновление анимации
            newObj.getComponent(StateMachine.class).refreshTextures();
        }

        newObj.getComponent(SpriteRender.class).setColor(new Vector4f(1, 1, 1, 1));
        newObj.removeComponent(NonPickable.class);

        //проверка, чтоб не дублировались объекты
        if(lastPickedObj == null) {
            Window.getCurrentScene().addGameObjectToScene(newObj);
        }
        else{
            if (newObj.transform.position.x == lastPickedObj.transform.position.x && newObj.transform.position.y == lastPickedObj.transform.position.y) {
                //Window.getCurrentScene().addGameObjectToScene(newObj);
            }else{
                Window.getCurrentScene().addGameObjectToScene(newObj);
            }
        }


        lastPickedObj = newObj;
    }

    @Override
    public void editorUpdate(float dt) {
        debounce -= dt;
        if(holdingObject != null && debounce <= 0){
            float x = MouseListener.getWorldX();
            float y = MouseListener.getWorldY();
            holdingObject.transform.position.x = ((int)Math.floor(x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2.0f;
            holdingObject.transform.position.y = ((int)Math.floor(y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2.0f;

        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            place();
            debounce = debounceTime;
        }

        //уничтожение объекта при нажатии на ESC
        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            holdingObject.destroy();
            holdingObject = null;
        }
        }
    }
}
