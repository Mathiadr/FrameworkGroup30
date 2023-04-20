package no.hiof.framework30.brunost.util;

import com.google.gson.*;
import no.hiof.framework30.brunost.components.Transform;
import no.hiof.framework30.brunost.components.Component;
import no.hiof.framework30.brunost.gameObjects.GameObject;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");

        GameObject gameObject = new GameObject(name);
        for (JsonElement e : components){
            Component component = context.deserialize(e, Component.class);
            gameObject.addComponent(component);
        }
        gameObject.transform = gameObject.getComponent(Transform.class);
        return gameObject;
    }
}
