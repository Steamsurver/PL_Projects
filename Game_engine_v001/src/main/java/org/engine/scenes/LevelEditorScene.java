package org.engine.scenes;


import imgui.ImGui;
import imgui.ImVec2;
import org.engine.GameElements.Camera;
import org.engine.Rendering.DebugDraw;
import org.engine.Rendering.Objects.Components.*;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Objects.Components.GridLines;
import org.engine.Rendering.Transform;
import org.engine.Resources.Utils.AssetsPool;
import org.engine.Resources.Utils.Prefabs;
import org.engine.scenes.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class LevelEditorScene extends Scene {
    private GameObject player;
    private GameObject playerSheets;
    private SpriteSheet spriteSheetPlayer;
    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

    public LevelEditorScene() {
        System.out.println("LevelEditorScene");
        System.out.println("Loading shaders");

    }


    @Override
    public void init(){
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        this.camera = new Camera(new Vector2f(0, 0));
        camera.position.y = -200;
        camera.position.x = -200;


        loadResources();//загружаем ресурсы из AssetsPool(текстуры и шейдеры)
        spriteSheetPlayer = AssetsPool.getSpriteSheet("src/main/java/org/engine/Resources/Textures/Animations/Player_Animation_v2.png");


        if(levelLoaded){//подгружаем сразу первый объект в активные
            if(gameObjects.size() > 0) {
                this.activeGameObject = gameObjects.get(0);
            }return;
        }


    }
    @Override
    public void update(float dt) {

        levelEditorStuff.update(dt);
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }


        this.renderer.render(dt);//метод рендеринга из объекта RenderButch
    }

    private void loadResources(){
        AssetsPool.getShader("src/main/java/org/engine/Rendering/Shaders/default.glsl");


        AssetsPool.addSpriteSheet("src/main/java/org/engine/Resources/Textures/Animations/Player_Animation_v2.png",
                new SpriteSheet(AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Animations/Player_Animation_v2.png"),
                        27,45, 12, 1));

        AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Animations/Player_Animation_v2.png");

        //исправление проблем с дессериализацией
        for (GameObject g : gameObjects) {
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
                GameObject object = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight);
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);//цепляем наш обект к мыши.
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
