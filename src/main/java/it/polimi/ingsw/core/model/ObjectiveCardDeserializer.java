package it.polimi.ingsw.core.model;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class is a custom deserializer for the Objective class.
 * It extends the JsonDeserializer interface and overrides the deserialize method.
 */
class ObjectiveCardDeserializer implements JsonDeserializer<Objective> {
    /**
     * This method is used to deserialize the JSON representation of an Objective.
     * @param json The JsonElement to deserialize.
     * @param typeOfT The type of the Object to deserialize to.
     * @param context The context for deserialization.
     * @return The deserialized Objective.
     * @throws JsonParseException if json is not in the expected format of Objective.
     */
    @Override
    public Objective deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String pattern = jsonObject.get("pattern").getAsString();

        return switch (pattern) {
            case "L" -> context.deserialize(jsonObject, LObjective.class);
            case "reverseL" -> context.deserialize(jsonObject, ReverseLObjective.class);
            case "downL" -> context.deserialize(jsonObject, DownLObjective.class);
            case "reverseDownL" -> context.deserialize(jsonObject, DownReverseLObjective.class);
            case "diagonalDx" -> context.deserialize(jsonObject, DxDiagonalObjective.class);
            case "diagonalSx" -> context.deserialize(jsonObject, SxDiagonalObjective.class);
            case "resource" -> context.deserialize(jsonObject, ResourceObjective.class);
            default -> throw new JsonParseException("Unknown pattern: " + pattern);
        };
    }
}