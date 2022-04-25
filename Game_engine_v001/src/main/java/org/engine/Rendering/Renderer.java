package org.engine.Rendering;


import org.engine.Rendering.Objects.Components.SpriteRender;
import org.engine.Rendering.Objects.Components.Textures.Texture;
import org.engine.Rendering.Objects.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go){
        SpriteRender spr = go.getComponent(SpriteRender.class);
        if(spr != null){
            add(spr);
        }
    }

    public void add(SpriteRender sprite){
        boolean added = false;
        for(RenderBatch batch : batches){
            if(batch.hasRoom() && batch.zIndex() == sprite.gameObject.zIndex()){
                Texture tex = sprite.getTexture();
                if(tex == null || (batch.hasTexture(tex)|| batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.zIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void render(float dt){
        for(RenderBatch batch : batches){
            batch.render(dt);
        }
    }
}
