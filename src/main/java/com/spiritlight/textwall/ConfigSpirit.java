package com.spiritlight.textwall;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigSpirit {
    public static void getConfig() throws IOException {
        File config = new File("config/TextWall.json");
        if (config.exists()) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject)parser.parse(new FileReader("config/TextWall.json"));
            MainMod.prefix = String.valueOf(jsonObject.get("prefix")).replace("\"", ""); // Funny bug!
        } else {
            writeConfig();
        }
    }

    public static void writeConfig() throws IOException {
        JsonWriter writer = new JsonWriter(new FileWriter("config/TextWall.json"));
        writer.beginObject();
        writer.name("prefix").value(MainMod.prefix);
        writer.endObject();
        writer.close();
    }
}