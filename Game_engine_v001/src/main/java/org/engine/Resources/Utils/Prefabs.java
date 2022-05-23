package org.engine.Resources.Utils;

import org.engine.GameElements.Window;
import org.engine.Rendering.Objects.Components.Sprite;
import org.engine.Rendering.Objects.Components.SpriteRender;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Objects.Components.Transform;
import org.joml.Vector2f;

//быстрый генератор объектов
public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block = Window.getCurrentScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;

        SpriteRender renderer = new SpriteRender();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

}
