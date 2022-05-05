package org.engine.Resources.Utils;

import org.engine.Rendering.Objects.Components.Sprite;
import org.engine.Rendering.Objects.Components.SpriteRender;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Transform;
import org.joml.Vector2f;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block = new GameObject("Sprite_Object_Gen",
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRender renderer = new SpriteRender();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

}
