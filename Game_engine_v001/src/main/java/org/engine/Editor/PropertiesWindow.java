package org.engine.Editor;

import imgui.ImGui;
import org.engine.GameController.MouseListener;
import org.engine.Physics2D.components.Box2DCollider;
import org.engine.Physics2D.components.Circle2DCollider;
import org.engine.Physics2D.components.RigidBody2D;
import org.engine.Rendering.Objects.Components.NonPickable;
import org.engine.Rendering.Objects.Components.Textures.PickingTexture;
import org.engine.Rendering.Objects.GameObject;
import org.engine.scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
//окно со свойствами выбранного объекта
public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;
    private float debounce = 0.2f;//небольшая задержка

    public PropertiesWindow(PickingTexture pickingTexture){
        this.pickingTexture = pickingTexture;
    }


    public void update(float dt, Scene currentScene){
        debounce -= dt;

        if(!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0){
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);


            GameObject pickedOgj = currentScene.getGameObject(gameObjectId);
            //если игровой объект содержит класс NonPickable, то не выбираем его
            if(pickedOgj != null && pickedOgj.getComponent(NonPickable.class) == null) {
                activeGameObject = pickedOgj;
            }else if(pickedOgj == null && !MouseListener.isDragging()){//очищаем место активного объекта
                activeGameObject = null;
            }
            this.debounce = 0.2f;
        }
    }

    public void imGui(){
        if(activeGameObject != null){
            ImGui.begin("Properties");

            //добавление объектов через правый клик
            if(ImGui.beginPopupContextWindow("Component Adder")){//всплывающее окно через правую кнопку мыши
                if(ImGui.menuItem("Add RigidBody")){//один из вариантов контекстного меню
                    if(activeGameObject.getComponent(RigidBody2D.class) == null){
                        activeGameObject.addComponent(new RigidBody2D());
                    }
                }

                if(ImGui.menuItem("Add BoxCollider")){
                    if(activeGameObject.getComponent(Box2DCollider.class) == null && activeGameObject.getComponent(Circle2DCollider.class) == null){
                        activeGameObject.addComponent(new Box2DCollider());
                    }
                }

                if(ImGui.menuItem("Add CircleCollider")){
                    if(activeGameObject.getComponent(Circle2DCollider.class) == null && activeGameObject.getComponent(Box2DCollider.class) == null){
                        activeGameObject.addComponent(new Circle2DCollider());
                    }
                }

                ImGui.endPopup();//закрытие всплывающего контекстного меню
            }


            activeGameObject.imGui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {
        return activeGameObject;
    }

    public void setActiveGameObject(Object o) {
        activeGameObject = null;
    }
}
