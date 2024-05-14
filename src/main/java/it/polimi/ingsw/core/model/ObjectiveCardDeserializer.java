package it.polimi.ingsw.core.model;

import com.google.gson.*;

import java.lang.reflect.Type;

class ObjectiveCardDeserializer implements JsonDeserializer<Objective> {
    @Override
    public Objective deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String pattern = jsonObject.get("pattern").getAsString();

        return switch (pattern) {
            case "L" -> context.deserialize(jsonObject, LObjective.class);
            case "diagonalDx" -> context.deserialize(jsonObject, DxDiagonalObjective.class);
            case "resource" -> context.deserialize(jsonObject, ResourceObjective.class);
            default -> throw new JsonParseException("Unknown pattern: " + pattern);
        };
    }
}
