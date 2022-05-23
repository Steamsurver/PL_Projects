package org.engine.Editor;

import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;
import org.joml.Vector2f;

public class SettingImGui {
    //инструментал для настройки графического интерфейса движка
    private static float defaultColumnWidth = 220.0f;


    public static void dragVex2Control(String label, Vector2f values){
    dragVex2Control(label, values, 0.0f, defaultColumnWidth);
    }

    public static void dragVex2Control(String label, Vector2f values, float resetValue){
        dragVex2Control(label, values, resetValue, defaultColumnWidth);
    }

    public static void dragVex2Control(String label, Vector2f values, float resetValue, float columnWidth){
        //настройка столбцов для Vector2 значенй
        ImGui.pushID(label);

        ImGui.columns(2);//один столбец для метки, другой для изменяемого значения
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);

        ImGui.nextColumn();
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3, lineHeight);//размер кнопки
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 8);//ширина каждой настраиваемой строки


        //настройка ползунка х==================================
        ImGui.pushItemWidth(widthEach);//назначаем настройки ширины
        if(ImGui.button("X", buttonSize.x, buttonSize.y)){
            values.x = resetValue;
        }

        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##x", vecValuesX, 0.3f);
        ImGui.popItemWidth();
        //=======================================================


        //настройка ползунка y==================================
        ImGui.pushItemWidth(widthEach);//назначаем настройки ширины
        if(ImGui.button("Y", buttonSize.x, buttonSize.y)){
            values.y = resetValue;
        }

        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##y", vecValuesY, 0.3f);
        ImGui.popItemWidth();
        //=======================================================


        values.x = vecValuesX[0];
        values.y = vecValuesY[0];

        ImGui.popStyleVar();
        ImGui.nextColumn();
        ImGui.columns(1);


        ImGui.popID();
    }



    public static float dragFloatControl(String label, float values){
        //настройка столбцов для float значенй
        ImGui.pushID(label);

        ImGui.columns(2);//один столбец для метки, другой для изменяемого значения
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {values};
        ImGui.dragFloat("##dragFloat", valArr, 0.3f);

        ImGui.nextColumn();
        ImGui.columns(1);


        ImGui.popID();

        return valArr[0];
    }


    public static int dragIntControl(String label, int values){
        //настройка столбцов для int значенй
        ImGui.pushID(label);

        ImGui.columns(2);//один столбец для метки, другой для изменяемого значения
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valArr = {values};
        ImGui.dragInt("##dragInt", valArr, 0.3f);

        ImGui.nextColumn();
        ImGui.columns(1);


        ImGui.popID();

        return valArr[0];
    }


    public static String inputText(String label, String text) {
        //вывод текста через ImGUI
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImString outString = new ImString(text, 256);
        if (ImGui.inputText("##" + label, outString)) {//сам ввод текста
            ImGui.columns(1);
            ImGui.popID();

            return outString.get();//в случае изменения, выкинет то имя, которое установили
        }

        ImGui.columns(1);
        ImGui.popID();

        return text;
    }
}
