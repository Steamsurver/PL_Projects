package org.engine.Rendering.Objects;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.engine.Rendering.Objects.Components.Component;
import org.engine.Rendering.Objects.Components.Sprite;
import org.engine.Rendering.Objects.Components.SpriteRender;
import org.engine.Rendering.Objects.Components.Transform;
import org.engine.Resources.Utils.AssetsPool;
import org.engine.Resources.Utils.Deserializer;
import org.engine.Resources.Utils.GameObjectDeserializer;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    public String name;
    private static int ID_COUNTER = 0;
    private int uid = -1;//id объекта
    private List<Component> components;//контейнер с компонентами
    public transient Transform transform;//параметры для перемещения и изменения размера
    private boolean doSerialization = true;//можно дессириализовывать или нет
    private boolean isDead = false;//флаг для уничтожения

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.uid = ID_COUNTER++;
    }

    public void destroy() {
        this.isDead = true;
        for(int i = 0; i < components.size(); i++){
            components.get(i).destroy();
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }

        return null;
    }


    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }


    public void imGui(){
        for(Component c : components){
            if(ImGui.collapsingHeader(c.getClass().getSimpleName()))//варианты свойств зависящие от компонента
                c.imGui();
        }
    }


    public void addComponent(Component c) {
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }


    public void update(float dt) {
        for (int i=0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void editorUpdate(float dt) {
        for (int i=0; i < components.size(); i++) {
            components.get(i).editorUpdate(dt);
        }
    }


    public void start() {
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public int getUid(){
        return this.uid;
    }

    public List<Component> getAllComponents() {
        return components;
    }

    public void setNoSerialize() {
         this.doSerialization = false;
    }

    public boolean doSerialization() {
        return this.doSerialization;
    }

    public boolean isDead() {
        return this.isDead;
    }

    public GameObject copy() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new Deserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);

        obj.generateUid();
        for(Component c : obj.getAllComponents()){
            c.generateId();
        }

         //гарантированное переприсваивание текстуры
        SpriteRender spriteRender = obj.getComponent(SpriteRender.class);
        if(spriteRender != null && spriteRender.getTexture() != null){
            spriteRender.setTexture(AssetsPool.getTexture(spriteRender.getTexture().getFilepath()));
        }

        return obj;
    }

    public void generateUid(){
        this.uid = ID_COUNTER++;
    }
}

