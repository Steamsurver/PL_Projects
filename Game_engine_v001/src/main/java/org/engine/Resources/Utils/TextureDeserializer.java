package org.engine.Resources.Utils;

import com.google.gson.*;
import org.engine.Rendering.Assets.Texture.Texture;
import org.engine.Rendering.Objects.Components.Sprite;

import java.lang.reflect.Type;

public class TextureDeserializer implements JsonDeserializer<Texture> {

    @Override
    public Texture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Texture texture = null;
        JsonObject jsonObject = json.getAsJsonObject();
        String filepath = jsonObject.get("filepath").getAsString();
        int width = jsonObject.get("width").getAsInt();
        int height = jsonObject.get("height").getAsInt();

        texture = new Texture(width, height);
        texture.init(filepath);

        return texture;
    }

}