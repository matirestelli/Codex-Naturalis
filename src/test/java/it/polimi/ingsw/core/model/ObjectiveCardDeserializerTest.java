package it.polimi.ingsw.core.model;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveCardDeserializerTest {
    private ObjectiveCardDeserializer objectiveCardDeserializer;
    private Gson gson;

    @BeforeEach
    void setUp() {
        objectiveCardDeserializer = new ObjectiveCardDeserializer();
        gson = new GsonBuilder()
                .setFieldNamingStrategy(new FieldNamingStrategy() {
                    @Override
                    public String translateName(Field field) {
                        if (field.getDeclaringClass() == DxDiagonalObjective.class && field.getName().equals("color")) {
                            return "dxDiagonalObjectiveColor";
                        } else if (field.getDeclaringClass() == CardGame.class && field.getName().equals("color")) {
                            return "cardGameColor";
                        }
                        return field.getName();
                    }
                })
                .registerTypeAdapter(Objective.class, objectiveCardDeserializer)
                .create();
    }

    @Test
    void testDeserialize() {
        JsonElement jsonElement = JsonParser.parseString("{\"pattern\":\"L\"}");
        Objective objective = gson.fromJson(jsonElement, Objective.class);
        assertTrue(objective instanceof LObjective);

        jsonElement = JsonParser.parseString("{\"pattern\":\"reverseL\"}");
        objective = gson.fromJson(jsonElement, Objective.class);
        assertTrue(objective instanceof ReverseLObjective);

        jsonElement = JsonParser.parseString("{\"pattern\":\"downL\"}");
        objective = gson.fromJson(jsonElement, Objective.class);
        assertTrue(objective instanceof DownLObjective);

        jsonElement = JsonParser.parseString("{\"pattern\":\"reverseDownL\"}");
        objective = gson.fromJson(jsonElement, Objective.class);
        assertTrue(objective instanceof DownReverseLObjective);

        jsonElement = JsonParser.parseString("{\"pattern\":\"diagonalDx\"}");
        objective = gson.fromJson(jsonElement, Objective.class);
        assertTrue(objective instanceof DxDiagonalObjective);

        jsonElement = JsonParser.parseString("{\"pattern\":\"diagonalSx\"}");
        objective = gson.fromJson(jsonElement, Objective.class);
        assertTrue(objective instanceof SxDiagonalObjective);

        jsonElement = JsonParser.parseString("{\"pattern\":\"resource\"}");
        objective = gson.fromJson(jsonElement, Objective.class);
        assertTrue(objective instanceof ResourceObjective);

        JsonElement unknownJsonElement = JsonParser.parseString("{\"pattern\":\"unknown\"}");
        assertThrows(JsonParseException.class, () -> {
            gson.fromJson(unknownJsonElement, Objective.class);
        });
    }
}
