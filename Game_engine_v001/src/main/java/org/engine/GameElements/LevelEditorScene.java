package org.engine.GameElements;


import imgui.ImGui;
import imgui.ImVec2;
import org.engine.GameController.KeyListener;
import org.engine.Rendering.Objects.Components.*;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Transform;
import org.engine.Resources.Utils.AssetsPool;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene{
    private GameObject player;
    private GameObject playerSheets;
    private SpriteSheet spriteSheetPlayer;

    public LevelEditorScene() {
        System.out.println("LevelEditorScene");
        System.out.println("Loading shaders");

    }


    @Override
    public void init(){
        loadResources();//загружаем ресурсы из AssetsPool(текстуры и шейдеры)

        if(levelLoaded){//подгружаем сразу первый объект в активные
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        spriteSheetPlayer = AssetsPool.getSpriteSheet("src/main/java/org/engine/Resources/Textures/Animations/Player_Animation_v2.png");
        this.camera = new Camera(new Vector2f(0, 0));



        player = new GameObject("player", new Transform(new Vector2f(2 * 100, 0), new Vector2f(64, 128)), 3);
        SpriteRender spriteRenderPlayer = new SpriteRender();
        Sprite spritePlayer = new Sprite();
        spritePlayer.setTexture(AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Entity/Player_Idle.png"));
        spriteRenderPlayer.setSprite(spritePlayer);
        player.addComponent(spriteRenderPlayer);
        player.addComponent(new RigidBody());
        this.addGameObjectToScene(player);
        this.activeGameObject = player;


    }

    @Override
    public void update(float dt) {
        this.renderer.render(dt);//метод рендеринга из объекта RenderButch

        camera.position.y = -200;
        camera.position.x = -200;

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            player.transform.position.x += 11 ;

        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            player.transform.position.x -= 11;

        }
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            player.transform.position.y += 11;

        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            player.transform.position.y -= 11;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }

    private void loadResources(){
        AssetsPool.getShader("src/main/java/org/engine/Rendering/Shaders/default.glsl");
        AssetsPool.addSpriteSheet("src/main/java/org/engine/Resources/Textures/Animations/Player_Animation_v2.png",
                new SpriteSheet(AssetsPool.getTexture("src/main/java/org/engine/Resources/Textures/Animations/Player_Animation_v2.png"),
                        27,45, 12, 1));
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
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                System.out.println("Button " + i + "clicked");
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
