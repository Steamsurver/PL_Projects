package org.engine.scenes;


import imgui.ImGui;
import imgui.ImVec2;
import org.engine.GameElements.Window;
import org.engine.Rendering.Assets.Sound.Sound;
import org.engine.Rendering.Objects.Components.*;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Objects.Components.GridLines;
import org.engine.Resources.Utils.AssetsPool;
import org.engine.Resources.Utils.Prefabs;
import org.joml.Vector2f;

import java.io.File;
import java.util.Collection;

public class LevelSceneInitializer extends SceneInitializer {
    private SpriteSheet spriteSheet;
    private SpriteSheet spriteSheet2;
    private GameObject levelEditor;
    private Scene pScene;
    public LevelSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        spriteSheet = AssetsPool.getSpriteSheet("src/main/java/org/engine/Resources/Textures/Props/Fruits.png");
        levelEditor = scene.createGameObject("LevelEditor");
        levelEditor.setNoSerialize();
        levelEditor.addComponent(new MouseControls());//подключаем управление мышью к сцене
        levelEditor.addComponent(new GridLines());//подключаем рисовку линий к сцене
        levelEditor.addComponent(new EditorCamera(scene.getCamera()));//подключаем управление камерой к сцене
        levelEditor.addComponent(new TranslateGizmo(Window.getImGuiLayer().getPropertiesWindow()));//подключаем стрелки управления объектом
        scene.addGameObjectToScene(levelEditor);
        this.pScene = scene;
    }

    @Override
    public void loadResources(Scene scene) {//загружаем ресурсы из AssetsPool(текстуры и шейдеры)
        AssetsPool.getShader("src/main/java/org/engine/Rendering/Shaders/default.glsl");

        AssetsPool.addSpriteSheet("src/main/java/org/engine/Resources/Textures/Props/Fruits.png",
                new SpriteSheet(AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Props/Fruits.png"),
                        16, 16, 6, 0));

        AssetsPool.addSpriteSheet("src/main/java/org/engine/Resources/Textures/Button_textures/gizmos.png",
                new SpriteSheet(AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Button_textures/gizmos.png"),
                        24, 48, 3, 0));

        AssetsPool.addSpriteSheet("src/main/java/org/engine/Resources/Textures/Animations/playerA.png",
                new SpriteSheet(AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Animations/playerA.png"),
                        32, 64, 32, 0));

        AssetsPool.addSpriteSheet("src/main/java/org/engine/Resources/Textures/Props/flower.png",
                new SpriteSheet(AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Props/flower.png"),
                        32, 32, 4, 0));


        AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Props/Fruits.png");

        AssetsPool.addSound("src/main/java/org/engine/Resources/Sounds/Blip_Select.ogg", false);
        AssetsPool.addSound("src/main/java/org/engine/Resources/Sounds/Blip_Select2.ogg", false);
        AssetsPool.addSound("src/main/java/org/engine/Resources/Sounds/Blip_Select3.ogg", false);
        AssetsPool.addSound("src/main/java/org/engine/Resources/Sounds/Explosion2.ogg", false);
        AssetsPool.addSound("src/main/java/org/engine/Resources/Sounds/Laser_Shoot4.ogg", false);

        //исправление проблем с дессериализацией
        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRender.class) != null) {
                SpriteRender spr = g.getComponent(SpriteRender.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetsPool.getTexture(spr.getTexture().getFilepath()));
                }
            }

            if (g.getComponent(StateMachine.class) != null) {
                StateMachine sm = g.getComponent(StateMachine.class);
                sm.refreshTextures();
            }
        }


    }


    @Override
    public void imGui() {
        //отладочное управление компонентами
        ImGui.begin("LevelEditorStuff");
        levelEditor.imGui();
        ImGui.end();

        ImGui.begin("ConstructWindow");
        if (ImGui.beginTabBar("Elements")) {
            if (ImGui.beginTabItem("Single sprite")) {
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                int i = 0;

                //===================================================================================================================
                for (i = 0; i < spriteSheet.size(); i++) {//подгрузка на GUI листов со спрайтами
                    Sprite sprite = spriteSheet.getSprite(i);
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
                    if (i + 1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }

                ImGui.sameLine();
                //===================================================================================================================
               for (int j = 0; j < AssetsPool.getToArrayOfSprite().size(); j++) {//подгрузка на GUI одиночных спрайтов

                    Sprite sprite = AssetsPool.getToArrayOfSprite().get(j);
                    float spriteWidth = sprite.getWidth() * 2;
                    float spriteHeight = sprite.getHeight() * 2;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    i++;

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        levelEditor.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (j + 1 < AssetsPool.getToArrayOfSprite().size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }

                ImGui.sameLine();
                //===================================================================================================================
                for(int j = 0; j < AssetsPool.getToArrayOfSpriteSheets().size(); j++){
                    for(int t = 0; t < AssetsPool.getToArrayOfSpriteSheets().get(j).size(); t++) {
                        Sprite sprite = AssetsPool.getToArrayOfSpriteSheets().get(j).getSprite(t);
                        float spriteWidth = sprite.getWidth() * 2;
                        float spriteHeight = sprite.getHeight() * 2;
                        int id = sprite.getTexId();
                        Vector2f[] texCoords = sprite.getTexCoords();

                        i++;
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
                        if (t + 1 < AssetsPool.getToArrayOfSpriteSheets().get(j).size() && nextButtonX2 < windowX2) {
                            ImGui.sameLine();
                        }
                    }
                }


                //===================================================================================================================

                ImGui.endTabItem();
            }

            //====================================================================================
            if (ImGui.beginTabItem("Prefabs")) {
                SpriteSheet animationSprite = AssetsPool.getSpriteSheet("src/main/java/org/engine/Resources/Textures/Animations/playerA.png");
                SpriteSheet flowerSprite = AssetsPool.getSpriteSheet("src/main/java/org/engine/Resources/Textures/Props/flower.png");
                Vector2f[] texCoords;
                Sprite sprite;

                //========================
                sprite = animationSprite.getSprite(0);
                texCoords = sprite.getTexCoords();
                ImGui.pushID(0);
                if (ImGui.imageButton(sprite.getTexId(), sprite.getWidth(), sprite.getHeight(), texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateCharacterAnimation(0, 3, "run down", "src/main/java/org/engine/Resources/Textures/Animations/playerA.png");
                    levelEditor.getComponent(MouseControls.class).pickupObject(object);//цепляем наш обект к мыши.
                }
                ImGui.popID();
                ImGui.sameLine();
                //========================
                sprite = animationSprite.getSprite(8);
                texCoords = sprite.getTexCoords();
                ImGui.pushID(1);
                if (ImGui.imageButton(sprite.getTexId(), sprite.getWidth(), sprite.getHeight(), texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateCharacterAnimation(8, 11, "run up", "src/main/java/org/engine/Resources/Textures/Animations/playerA.png");
                    levelEditor.getComponent(MouseControls.class).pickupObject(object);//цепляем наш обект к мыши.
                }
                ImGui.popID();
                ImGui.sameLine();
                //========================
                sprite = animationSprite.getSprite(16);
                texCoords = sprite.getTexCoords();
                ImGui.pushID(2);
                if (ImGui.imageButton(sprite.getTexId(), sprite.getWidth(), sprite.getHeight(), texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateCharacterAnimation(16, 19, "run left", "src/main/java/org/engine/Resources/Textures/Animations/playerA.png");
                    levelEditor.getComponent(MouseControls.class).pickupObject(object);//цепляем наш обект к мыши.
                }
                ImGui.popID();
                ImGui.sameLine();
                //========================
                sprite = animationSprite.getSprite(24);
                texCoords = sprite.getTexCoords();
                ImGui.pushID(3);
                if (ImGui.imageButton(sprite.getTexId(), sprite.getWidth(), sprite.getHeight(), texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateCharacterAnimation(24, 27, "run right", "src/main/java/org/engine/Resources/Textures/Animations/playerA.png");
                    levelEditor.getComponent(MouseControls.class).pickupObject(object);//цепляем наш обект к мыши.
                }
                ImGui.popID();
                ImGui.sameLine();
                //========================

                //========================
                sprite = flowerSprite.getSprite(0);
                texCoords = sprite.getTexCoords();
                ImGui.pushID(4);
                if (ImGui.imageButton(sprite.getTexId(), sprite.getWidth(), sprite.getHeight(), texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateCharacterAnimation(0, 3, "flower", "src/main/java/org/engine/Resources/Textures/Props/flower.png");
                    levelEditor.getComponent(MouseControls.class).pickupObject(object);//цепляем наш обект к мыши.
                }
                ImGui.popID();
                ImGui.sameLine();
                //========================

                ImGui.endTabItem();
            }
            //====================================================================================
            if (ImGui.beginTabItem("Sounds")) {
                Collection<Sound> sounds = AssetsPool.getAllSounds();
                for (Sound sound : sounds) {
                    File tmp = new File(sound.getFilepath());
                    if (ImGui.button(tmp.getName())) {
                        if (!sound.isPlaying()) {
                            sound.play();
                        } else {
                            sound.stop();
                        }
                    }

                    if (ImGui.getContentRegionAvailX() > 100) {
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();
            }



            //====================================================================================

            ImGui.endTabBar();
        }

        ImGui.end();
    }


}



