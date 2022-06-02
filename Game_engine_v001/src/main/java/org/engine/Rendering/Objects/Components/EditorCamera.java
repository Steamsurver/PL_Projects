package org.engine.Rendering.Objects.Components;

import org.engine.GameController.KeyListener;
import org.engine.GameController.MouseListener;
import org.engine.GameElements.Camera;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

//Камера редактора уровней
public class EditorCamera extends Component {

    private float dragDebounce = 0.032f;//примерная задержка перетаскивания
    private float dragSensitivity = 30.0f;//чувствительность перетаскивания
    private float lerpTime = 0.0f;//значение, необходимое для ускорение плавного перехода со временем
    private Camera levelEditorCamera;//экземпляр камеры редактора
    private Vector2f clickOrigin; //место, где мы первоначально кликнули
    private float scrollSensitivity = 0.1f;//чувствительность скроллинга
    private boolean reset = false;//флаг для сброса камеры в начальное положение

    public EditorCamera(Camera levelEditorCamera) {
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void editorUpdate(float dt) {
        //передвижение мыши
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0) {//по сути наш первый клик
            this.clickOrigin = new Vector2f(MouseListener.getWorldX(), MouseListener.getWorldY());//координаты первого нажатия
            dragDebounce -= dt;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2f mousePos = new Vector2f(MouseListener.getWorldX(), MouseListener.getWorldY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);//расстояние между первым кликом и текущим кликом
            levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitivity));//установка новых координат для камеры
            this.clickOrigin.lerp(mousePos, dt);//попытка реализовать плавное движение камеры со скоростью dt(пока ни на что не влияет)
        }

        if (dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {//сброс задержки
            dragDebounce = 0.1f;
        }

        if (MouseListener.getScrollY() != 0.0f) {//скроллинг камеры
            float addValue = (float) Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity),
                    1 / levelEditorCamera.getZoom());//экспоненциально изменяем значение zoom
            addValue *= -Math.signum(MouseListener.getScrollY());//если уменьшаем значение, то убеждаемся, что оно отрицательно и уменьшаем и наоборот
            levelEditorCamera.addZoom(addValue);
            MouseListener.endFrame();
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            reset = true;
        }

        if (reset) {//плавное перемещение к нулевым координатам
            levelEditorCamera.position.lerp(new Vector2f(), lerpTime);
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() +
                    ((1.0f - levelEditorCamera.getZoom()) * lerpTime));//плавное приближение зумма к начальному уровню

            this.lerpTime += 0.1f * dt;//ускорение с каждым кадром

            //установка нулевых координат при приближении к значениям, близким к 0, для более быстрого перехода
            if (Math.abs(levelEditorCamera.position.x) <= 5.0f &&
                    Math.abs(levelEditorCamera.position.y) <= 5.0f) {
                this.lerpTime = 0.0f;
                levelEditorCamera.position.set(0f, 0f);
                this.levelEditorCamera.setZoom(1.0f);
                reset = false;
            }
            MouseListener.endFrame();
        }
    }
}

