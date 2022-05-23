package org.engine.Resources.Utils;

import com.google.gson.*;
import org.engine.Rendering.Objects.Components.Component;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Objects.Components.Transform;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray componentArray = jsonObject.getAsJsonArray("components");

        GameObject go = new GameObject(name);
        for(JsonElement e : componentArray){
            Component c = context.deserialize(e, Component.class);
            go.addComponent(c);
        }
        go.transform = go.getComponent(Transform.class);

        return go;
    }
}
