package org.engine.Resources.Utils;

import org.engine.GameElements.Window;
import org.engine.Rendering.Objects.Components.*;
import org.engine.Rendering.Objects.GameObject;

import java.lang.annotation.Annotation;

//быстрый генератор объектов
public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){//генерация спрайта
        GameObject block = Window.getCurrentScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;

        SpriteRender renderer = new SpriteRender();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

    public static GameObject generateCharacterAnimation(int from, int to, String title) {//создание анимации персонажа
        SpriteSheet animationSprite = AssetsPool.getSpriteSheet("src/main/java/org/engine/Resources/Textures/Animations/playerA.png");
        GameObject character = generateSpriteObject(animationSprite.getSprite(0), 0.25f, 0.5f);

        AnimationState run = new AnimationState();
        run.title = "title";
        float defaultFrameTime = 0.23f;
        for(int i = from; i != to + 1; i++) {
            run.addFrame(animationSprite.getSprite(i), defaultFrameTime);
        }
        run.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(run.title);

        character.addComponent(stateMachine);
        return character;
    }

}
