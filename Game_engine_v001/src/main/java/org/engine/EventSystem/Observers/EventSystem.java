package org.engine.EventSystem.Observers;

import org.engine.EventSystem.Events.Event;
import org.engine.Rendering.Objects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class EventSystem {
    private static List<Observer> observers = new ArrayList<>();//список наблюдателей

    public static void addObserver(Observer observer){//добавление наблюдателя
        observers.add(observer);
    }

    public static void notify(GameObject object, Event event){//вызов обновления на наблюдателе
        for(Observer observer : observers){
            observer.onNotify(object, event);
        }
    }


}
