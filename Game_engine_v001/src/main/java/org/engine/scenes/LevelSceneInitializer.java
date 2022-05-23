package org.engine.scenes;


import imgui.ImGui;
import imgui.ImVec2;
import org.engine.GameController.MouseListener;
import org.engine.GameElements.Window;
import org.engine.Rendering.Objects.Components.*;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Objects.Components.GridLines;
import org.engine.Resources.Utils.AssetsPool;
import org.engine.Resources.Utils.Prefabs;
import org.engine.Resources.Utils.Settings;
import org.joml.Vector2f;

public class LevelSceneInitializer extends SceneInitializer {
    private SpriteSheet spriteSheetPlayer;
    private GameObject levelEditor;

    public LevelSceneInitializer() {

    }

    @Override
    public void init(Scene scene){
        spriteSheetPlayer = AssetsPool.getSpriteSheet("src/main/java/org/engine/Resources/Textures/Props/Fruits.png");

        levelEditor = scene.createGameObject("LevelEditor");
        levelEditor.setNoSerialize();
        levelEditor.addComponent(new MouseControls());//подключаем управление мышью к сцене
        levelEditor.addComponent(new GridLines());//подключаем рисовку линий к сцене
        levelEditor.addComponent(new EditorCamera(scene.getCamera()));//подключаем управление камерой к сцене
        levelEditor.addComponent(new TranslateGizmo(Window.getImGuiLayer().getPropertiesWindow()));//подключаем стрелки управления объектом
        scene.addGameObjectToScene(levelEditor);
    }

    @Override
    public void loadResources(Scene scene){//загружаем ресурсы из AssetsPool(текстуры и шейдеры)
        AssetsPool.getShader("src/main/java/org/engine/Rendering/Shaders/default.glsl");


        AssetsPool.addSpriteSheet("src/main/java/org/engine/Resources/Textures/Props/Fruits.png",
                new SpriteSheet(AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Props/Fruits.png"),
                        16,16, 6, 0));

        AssetsPool.addSpriteSheet("src/main/java/org/engine/Resources/Textures/Button_textures/gizmos.png",
                new SpriteSheet(AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Button_textures/gizmos.png"),
                        24,48, 3, 0));

        AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Props/Fruits.png");

        //исправление проблем с дессериализацией
        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRender.class) != null) {
                SpriteRender spr = g.getComponent(SpriteRender.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetsPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
        }
    }


    @Override
    public void imGui(){

        //отладочное управление компонентами
        ImGui.begin("LevelEditorStuff");
        levelEditor.imGui();
        ImGui.end();

        ImGui.begin("ConstructWindow");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;

        for(int i=0; i < spriteSheetPlayer.size(); i++){
            Sprite sprite = spriteSheetPlayer.getSprite(i);
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                levelEditor.getComponent(MouseControls.class).pickupObject(object);//цепляем наш обект к мыши.
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < spriteSheetPlayer.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }


        ImGui.end();
    }


}
