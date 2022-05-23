package org.engine.Editor;

import imgui.ImGui;
import org.engine.EventSystem.Events.Event;
import org.engine.EventSystem.Events.EventType;
import org.engine.EventSystem.Observers.EventSystem;

public class MenuBar {
    public void imGui() {
        ImGui.beginMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save", "Ctrl+S")) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Load", "Ctrl+O")) {
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
}

