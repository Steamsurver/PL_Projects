package org.engine.Resources.Utils;

import com.google.gson.*;
import org.engine.Rendering.Assets.Texture.Texture;
import org.engine.Rendering.Objects.Components.Component;
import org.engine.Rendering.Objects.Components.Sprite;
import org.engine.Rendering.Objects.Components.SpriteSheet;
import org.engine.Rendering.Objects.GameObject;

import java.lang.reflect.Type;

public class SpriteSheetDeserializer implements JsonDeserializer<SpriteSheet> {


    @Override
    public SpriteSheet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject eA = jsonObject.getAsJsonObject("texture");
        Texture texture = context.deserialize(eA, Texture.class);//
        texture.init(texture.getFilepath());

        int spriteWidth = jsonObject.get("spriteWidth").getAsInt();
        int spriteHeight = jsonObject.get("spriteHeight").getAsInt();
        int numSprites = jsonObject.get("numSprites").getAsInt();
        int spacing = jsonObject.get("spacing").getAsInt();

        return new SpriteSheet(texture, spriteWidth, spriteHeight, numSprites, spacing);
    }
}
