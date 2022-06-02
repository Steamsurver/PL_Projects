package org.engine.Editor;

import imgui.ImGui;
import org.engine.EventSystem.Events.Event;
import org.engine.EventSystem.Events.EventType;
import org.engine.EventSystem.Observers.EventSystem;

public class MenuBar {
    private boolean isOpenWindowBarTexture;
    private boolean isOpenWindowBarSpriteSheet;
    private boolean isOpenWindowURLImageLoad;


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

        if (ImGui.beginMenu("Load Files")) {
            if (ImGui.menuItem("Load Texture", "----")) {
                this.isOpenWindowBarTexture = true;

            }

            if (ImGui.menuItem("Load SpriteSheet", "----")) {
                this.isOpenWindowBarSpriteSheet = true;

            }

            if (ImGui.menuItem("Load Animation", "----")) {
                //this.isOpenWindowBarTexture = true;
            }

            if(ImGui.menuItem("Load Sound", "----")){
                //this.isOpenWindowBarTexture = true;
            }

            if(ImGui.menuItem("Load URL Image", "----")){
                this.isOpenWindowURLImageLoad = true;
            }

            ImGui.endMenu();
        }
        ImGui.endMenuBar();

        //============================вызов окна загрузки=====================
        if(isOpenWindowBarTexture){
            isOpenWindowBarTexture = FileLoader.loadTexture();
        }

        if(isOpenWindowBarSpriteSheet){
            isOpenWindowBarSpriteSheet = FileLoader.loadSpriteSheet();
        }

        if(isOpenWindowURLImageLoad){
            isOpenWindowURLImageLoad = FileLoader.loadURLImage();
        }

    }


}

