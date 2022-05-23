package org.engine.EventSystem.Observers;

import org.engine.EventSystem.Events.Event;
import org.engine.Rendering.Objects.GameObject;

public interface Observer {
    void onNotify(GameObject object, Event event);

}
