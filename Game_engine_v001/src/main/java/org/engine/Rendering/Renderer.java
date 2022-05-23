package org.engine.Rendering;


import org.engine.Rendering.Objects.Components.SpriteRender;
import org.engine.Rendering.Objects.Components.Textures.Texture;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Shaders.Shader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;
    private static Shader currentShader;
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
            if(batch.hasRoom() && batch.zIndex() == sprite.gameObject.transform.zIndex){
                Texture tex = sprite.getTexture();
                if(tex == null || (batch.hasTexture(tex)|| batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.transform.zIndex, this);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public static void bindShader(Shader shader){
        currentShader = shader;
    }

    public static Shader getBoundShader(){
        return currentShader;
    }

    public void render(){
        currentShader.use();
        for(int i = 0; i < batches.size(); i++){
            RenderBatch batch = batches.get(i);
            batch.render();
        }
    }

    public void destroyGameObject(GameObject go) {
        if(go.getComponent(SpriteRender.class) == null) return;//если объект и так чист

        for(RenderBatch batch : batches){
            if(batch.destroyIfExists(go)){
                return;
            }
        }

    }
}
