package org.engine.Resources.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.engine.Rendering.Assets.Sound.Sound;
import org.engine.Rendering.Objects.Components.Component;
import org.engine.Rendering.Objects.Components.Sprite;
import org.engine.Rendering.Objects.Components.SpriteSheet;
import org.engine.Rendering.Assets.Texture.Texture;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Shaders.Shader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AssetsPool {
    private static Map<String, Shader> shadersContainer = new HashMap<>();
    private static Map<String, Texture> texturesContainer = new HashMap<>();

    private static Map<String, Sprite> spriteContainer = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheetContainer = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    private static ArrayList<SpriteSheet> arrayOfSpriteSheet = new ArrayList<>();
    private static ArrayList<Sprite> arrayOfSprite = new ArrayList<>();

    //==================================шейдеры=======================================
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

    //==================================текстуры====================================
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
        File file = new File(resourceName);
        if(!AssetsPool.spriteSheetContainer.containsKey(file.getAbsolutePath())){
            assert false: "ERROR: tried to access to spriteSheet " + resourceName;
        }
        return AssetsPool.spriteSheetContainer.getOrDefault(file.getAbsolutePath(), null);
    }


    //========================звук===================================
    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }

    public static Sound getSound(String soundFile) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            assert false : "ERROR: Sound file not added '" + soundFile + "'";
        }

        return null;
    }

    public static Sound addSound(String soundFile, boolean loops) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            Sound sound = new Sound(file.getAbsolutePath(), loops);
            AssetsPool.sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }


    //=================================================Листы спрайтов===========================================
    public static ArrayList<SpriteSheet> getToArrayOfSpriteSheets(){
        return AssetsPool.arrayOfSpriteSheet;
    }

    public static void reloadArrayOfSpriteSheets(ArrayList<SpriteSheet> spriteSheets){
        AssetsPool.arrayOfSpriteSheet = spriteSheets;
    }

    public static ArrayList<Sprite> getToArrayOfSprite(){
        return AssetsPool.arrayOfSprite;
    }

    public static void reloadArrayOfSprite(ArrayList<Sprite> sprites){
        AssetsPool.arrayOfSprite = sprites;
    }

}
