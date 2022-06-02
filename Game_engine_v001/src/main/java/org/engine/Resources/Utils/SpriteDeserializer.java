package org.engine.Resources.Utils;

import com.google.gson.*;
import org.engine.Rendering.Assets.Sound.Sound;
import org.engine.Rendering.Assets.Texture.Texture;
import org.engine.Rendering.Objects.Components.Component;
import org.engine.Rendering.Objects.Components.Sprite;
import org.engine.Rendering.Objects.Components.Transform;
import org.engine.Rendering.Objects.GameObject;

import java.lang.reflect.Type;

public class SpriteDeserializer implements JsonDeserializer<Sprite>{

    @Override
    public Sprite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        int width = jsonObject.get("width").getAsInt();
        int height = jsonObject.get("height").getAsInt();
        Texture textureJ = null;
        JsonObject eA = jsonObject.getAsJsonObject("texture");

        Texture texture = context.deserialize(eA, Texture.class);
        textureJ = texture;
        textureJ.init(textureJ.getFilepath());

        Sprite sprite = new Sprite();
        sprite.setTexture(textureJ);
        sprite.setWidth(width);
        sprite.setHeight(height);

        return sprite;
    }
}
