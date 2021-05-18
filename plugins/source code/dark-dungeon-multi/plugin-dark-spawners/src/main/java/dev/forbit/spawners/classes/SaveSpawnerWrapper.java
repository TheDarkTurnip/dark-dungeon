package dev.forbit.spawners.classes;

import com.google.gson.*;
import lombok.Data;

public @Data class SaveSpawnerWrapper {
    Spawner spawner;
    String className;
    public SaveSpawnerWrapper(Spawner spawner) {
        setSpawner(spawner);
        className = spawner.getClass().getName();
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }

    public static Spawner fromJSON(String json) throws ClassNotFoundException {
        System.out.println("Parsing: "+json);
        JsonParser parser = new JsonParser();

        JsonElement element = parser.parse(json);
        JsonObject object = element.getAsJsonObject();
        String className = object.get("className").getAsString();
        Class<?> clazz = Class.forName(className);
        Gson gson = (new GsonBuilder()).serializeNulls().create();
        return (Spawner) gson.fromJson(object.get("spawner"), clazz);

    }
}
