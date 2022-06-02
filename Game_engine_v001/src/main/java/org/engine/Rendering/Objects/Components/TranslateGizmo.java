package org.engine.Rendering.Objects.Components;

import org.engine.Editor.PropertiesWindow;
import org.engine.GameController.KeyListener;
import org.engine.GameController.MouseListener;
import org.engine.GameElements.Window;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Resources.Utils.AssetsPool;
import org.engine.Resources.Utils.Prefabs;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

//компонент для перемещения объектов(x, y стрелки) + реализация поворота объектов
public class TranslateGizmo extends Component {
    private Vector4f xAxisColor = new Vector4f(0, 0.7f, 1f, 1);//цвет оси x
    private Vector4f xAxisColorHover = new Vector4f(0, 0.4f, 1f, 1);//цвет при наведении мышью
    private Vector4f yAxisColor = new Vector4f(0, 0.7f, 1f, 1);//цвет оси y
    private Vector4f yAxisColorHover = new Vector4f(0, 0.4f, 1f, 1);//цвет при наведении мышью

    private GameObject xAxisObject;//объект оси x
    private GameObject yAxisObject;//объект оси y
    private Sprite arrowSprite;//спрайт стрелы
    private Sprite scaleSprite;//спрайт размерного указателя
    private SpriteRender xAxisSprite;//спрайт оси x
    private SpriteRender yAxisSprite;//спрайт оси y
    private Vector2f xAxisOffset = new Vector2f(0f, -0.3f);//смещение x оси
    private Vector2f yAxisOffset = new Vector2f(-0.380f, 0.070f);//смещение y оси

    private float gizmoWidth = 16f / 80f;
    private float gizmoHeight = 48f / 80f;
    private boolean xAxisActive = false; //флаг для блокировки перетаскиваний : true - можно   false - нельзя
    private boolean yAxisActive = false; //флаг для блокировки перетаскиваний

    private boolean xAxisSizeActive = false; //флаг для блокировки размера : true - можно   false - нельзя
    private boolean yAxisSizeActive = false; //флаг для блокировки размера
    boolean xAxisHot;//проверка, находится мышь над gizmo x стреле
    boolean yAxisHot;//проверка, находится мышь над gizmo y стреле
    private GameObject activeGameObject = null;//выбранный объект
    PropertiesWindow propertiesWindow;//экземпляр выбранного объекта

    public TranslateGizmo(PropertiesWindow propertiesWindow) {
        //генерируем объекты с готовыми параметрами
        SpriteSheet gizmos = AssetsPool.getSpriteSheet("src/main/java/org/engine/Resources/Textures/Button_textures/gizmos.png");
        this.arrowSprite = gizmos.getSprite(1);
        this.scaleSprite = gizmos.getSprite(2);


        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);

        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRender.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRender.class);

        this.propertiesWindow = propertiesWindow;

        //запрещаем выбирать стрелки редактором
        this.xAxisObject.addComponent(new NonPickable());
        this.yAxisObject.addComponent(new NonPickable());

        Window.getCurrentScene().addGameObjectToScene(this.xAxisObject);
        Window.getCurrentScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;//поворачиваем x стрелу на 90 градусов
        this.yAxisObject.transform.rotation = 180;//поворачиваем y стрелу на 180 градусов
        this.xAxisObject.transform.zIndex = 200;
        this.yAxisObject.transform.zIndex = 200;
        this.xAxisObject.setNoSerialize();
        this.yAxisObject.setNoSerialize();
    }

    @Override
    public void update(float dt){
        setInactive();

    }


    @Override
    public void editorUpdate(float dt) {

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();//подтягиваем выбранный объект
        if (this.activeGameObject != null) {
            this.setActive();
            //возможность дублицировать
            if(KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.keyBeginPress(GLFW_KEY_D)){
                GameObject newObj = this.activeGameObject.copy();
                Window.getCurrentScene().addGameObjectToScene(newObj);
                newObj.transform.position.add(0.1f, 0.1f);
                this.propertiesWindow.setActiveGameObject(newObj);
                return;
            } else if (KeyListener.keyBeginPress(GLFW_KEY_DELETE)) {//удаление объекта
                activeGameObject.destroy();
                this.setInactive();
                this.propertiesWindow.setActiveGameObject(null);
                return;
            }
        } else {
            this.setInactive();
            return;
        }

        this.xAxisHot = checkXHoverState();//проверка, находится мышь над gizmo x стреле
        this.yAxisHot = checkYHoverState();//проверка, находится мышь над gizmo y стреле

        if (this.activeGameObject != null) {
            //установка позиций как у выбранного объекта
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            //назначение смещения
            this.xAxisObject.transform.position.add(this.xAxisOffset);
            this.yAxisObject.transform.position.add(this.yAxisOffset);
        }

        this.flagSetup();//установка флагов перемещения

        //реализация перемещения объекта
        if (activeGameObject != null) {
            //изменяем координаты в зависсимости от пройденного растояния мыши
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
            } else if (yAxisActive && !xAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            }
            MouseListener.endFrame();
        }

        //реализация изменения размера объекта
        if (activeGameObject != null) {

            //изменяем размер в зависсимости от пройденного растояния мыши
            if (xAxisSizeActive && !yAxisSizeActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldDx();
                System.out.println(MouseListener.getWorldDx());
            } else if (yAxisSizeActive && !xAxisSizeActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldDy();
            }
            MouseListener.endFrame();
        }
    }

    private boolean checkXHoverState() {//проверка, находится мышь над gizmo x стреле
        Vector2f mousePos = MouseListener.getWorld();
        if ((mousePos.x <= xAxisObject.transform.position.x + (gizmoHeight / 2f) &&
                mousePos.x >= xAxisObject.transform.position.x - (gizmoWidth / 2f) &&
                mousePos.y >= xAxisObject.transform.position.y - (gizmoHeight / 6f)&&
                mousePos.y <= xAxisObject.transform.position.y + (gizmoWidth / 2f))) {
            xAxisSprite.setColor(xAxisColorHover);
            return true;
        }

        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState() {//проверка, находится мышь над gizmo y стреле
        Vector2f mousePos = MouseListener.getWorld();
        if (mousePos.x <= yAxisObject.transform.position.x + (gizmoWidth / 2f) &&
                mousePos.x >= yAxisObject.transform.position.x - (gizmoWidth / 2f) &&
                mousePos.y <= yAxisObject.transform.position.y + (gizmoHeight / 2f)&&
                mousePos.y >= yAxisObject.transform.position.y - (gizmoHeight / 2f)) {
            yAxisSprite.setColor(yAxisColorHover);
            return true;
        }

        yAxisSprite.setColor(yAxisColor);
        return false;
    }

    private void setActive() {//установка активного объекта
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive() {//отвязка от объекта и становление невидимыми
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }


    private void flagSetup() {//установка флагов изменения размера и перетаскивания
        //установка флагов для возможности перемещения объекта
        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && !yAxisActive
                && !yAxisSizeActive && !xAxisSizeActive) {
            xAxisSprite.setSprite(arrowSprite);
            yAxisSprite.setSprite(arrowSprite);
            xAxisActive = true;
            yAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && !xAxisActive
                && !yAxisSizeActive && !xAxisSizeActive) {
            xAxisSprite.setSprite(arrowSprite);
            yAxisSprite.setSprite(arrowSprite);
            yAxisActive = true;
            xAxisActive = false;
        } else {
            xAxisActive = false;
            yAxisActive = false;
        }

        //установка флагов для возможности изменения размера объекта
        if ((xAxisHot || xAxisSizeActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && !yAxisSizeActive
                && !yAxisActive && !xAxisActive) {
            xAxisSprite.setSprite(scaleSprite);
            yAxisSprite.setSprite(scaleSprite);
            xAxisSizeActive = true;
            yAxisSizeActive = false;
        } else if ((yAxisHot || yAxisSizeActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && !xAxisSizeActive
                && !yAxisActive && !xAxisActive) {
            xAxisSprite.setSprite(scaleSprite);
            yAxisSprite.setSprite(scaleSprite);
            xAxisSizeActive = false;
            yAxisSizeActive = true;
        } else {
            xAxisSprite.setSprite(arrowSprite);
            yAxisSprite.setSprite(arrowSprite);
            xAxisSizeActive = false;
            yAxisSizeActive = false;
        }
    }
}
