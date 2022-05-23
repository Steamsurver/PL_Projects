package org.engine.Rendering.Objects.Components;

import org.engine.Editor.SettingImGui;
import org.joml.Vector2f;
//класс для перемещения, увеличения и вращения объектов
public class Transform extends Component{
    public Vector2f position;
    public Vector2f scale;
    public float rotation = 0.0f;//параметр вращения
    public int zIndex = 0; //параметр, отвечающий за порядок отрисовки
    public Transform() {
        init(new Vector2f(), new Vector2f());
    }
    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }
    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
        this.zIndex = 0;
    }

    public Transform copy(){
        Transform t = new Transform(new Vector2f(this.position), new Vector2f(this.scale));
        return t;
    }

    public void copy(Transform to){
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public void imGui(){
        //ручное применение настроек для графическоко интерфейса движка
        gameObject.name = SettingImGui.inputText("Name: ", gameObject.name);//настройка имени объекта, если поменяли, то он сохранится в объекте
        SettingImGui.dragVex2Control("Position", this.position);
        SettingImGui.dragVex2Control("Scale", this.scale, 46);
        this.rotation = SettingImGui.dragFloatControl("Rotation", this.rotation);
        this.zIndex = SettingImGui.dragIntControl("Z-index", this.zIndex);
    }

    @Override
    public boolean equals(Object o){
        if(o == null)return false;
        if(!(o instanceof Transform)) return false;

        Transform t = (Transform)o;
        return t.position.equals(this.position) && t.scale.equals(this.scale) &&
                t.rotation == this.rotation && t.zIndex == this.zIndex;
    }


}

