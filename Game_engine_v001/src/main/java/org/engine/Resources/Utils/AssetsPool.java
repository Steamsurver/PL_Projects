package org.engine.Resources.Utils;

import org.engine.Rendering.Objects.Components.SpriteSheet;
import org.engine.Rendering.Objects.Components.Textures.Texture;
import org.engine.Rendering.Shaders.Shader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetsPool {
    private static Map<String, Shader> shadersContainer = new HashMap<>();
    private static Map<String, Texture> texturesContainer = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheetContainer = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if(AssetsPool.shadersContainer.containsKey(file.getAbsolutePath())){
            return AssetsPool.shadersContainer.get(file.getAbsolutePath());
        }else{
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetsPool.shadersContainer.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }


    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if(AssetsPool.texturesContainer.containsKey(file.getAbsolutePath())){
            return AssetsPool.texturesContainer.get(file.getAbsolutePath());
        }else{
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetsPool.texturesContainer.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet){
        File file = new File(resourceName);
        if(!AssetsPool.spriteSheetContainer.containsKey(file.getAbsolutePath())){
            AssetsPool.spriteSheetContainer.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName){
        File file =new File(resourceName);
        if(!AssetsPool.spriteSheetContainer.containsKey(file.getAbsolutePath())){
            assert false: "ERROR: tried to access to spriteSheet " + resourceName;
        }
        return AssetsPool.spriteSheetContainer.getOrDefault(file.getAbsolutePath(), null);
    }


}
