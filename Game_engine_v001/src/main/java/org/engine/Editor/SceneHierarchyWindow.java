package org.engine.Editor;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import org.engine.EventSystem.Observers.Observer;
import org.engine.GameElements.Window;
import org.engine.Rendering.Objects.GameObject;

import java.util.List;

public class SceneHierarchyWindow {
    private static String payloadDragDropType = "SceneHierarchy";

    //Окно иерархии сцены
    public void imGui() {
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getCurrentScene().getGameObjects();
        int index = 0;
        for (GameObject obj : gameObjects) {//если объект не серреализуется, то нет смысла его отображать на панели
            if (!obj.doSerialization()) {
                continue;
            }

            boolean treeNodeOpen = doTreeNode(obj, index);//древовидный вывод

            if (treeNodeOpen) {
                ImGui.treePop();
            }
            index++;

        }

        ImGui.end();
    }

    private boolean doTreeNode(GameObject object, int index){//древовидный вывод
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(
                object.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.SpanAvailWidth,
                object.name
        );
        ImGui.popID();

        //система перетаскивания узла
        if(ImGui.beginDragDropSource()){//если по объекту кликнули
            ImGui.setDragDropPayload(payloadDragDropType, object);
            //===============отображаемые детали
            ImGui.text(object.name);
            //===============
            ImGui.endDragDropSource();
        }

        if(ImGui.beginDragDropTarget()){//если цель уже выбрана и находится в таргете
            Object payloadObj = ImGui.acceptDragDropPayload(payloadDragDropType);//получаем перетаскиваемый объект
            if(payloadObj != null){
                if(payloadObj.getClass().isAssignableFrom(GameObject.class)){
                    GameObject playerGameObj = (GameObject) payloadObj;
                }
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }

}
