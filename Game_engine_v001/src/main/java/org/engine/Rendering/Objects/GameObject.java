package org.engine.Rendering.Objects;



import org.engine.Rendering.Objects.Components.Component;
import org.engine.Rendering.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> components;
    private int zIndex; //ндексация по координате Z

    public Transform transform;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.zIndex = 0;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
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
            c.imGui();
        }
    }


    public void addComponent(Component c) {
        this.components.add(c);
        c.gameObject = this;
    }


    public void update(float dt) {
        for (int i=0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }


    public int zIndex() {
        return this.zIndex;
    }
}

