package com.spiritlight.textwall;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigSpirit {
    public static void getConfig() {
        File config = new File("config/TextWall.json");
        try {
            if (config.exists()) {
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(new FileReader("config/TextWall.json"));
                MainMod.prefix = String.valueOf(jsonObject.get("prefix")).replace("\"", ""); // Funny bug!
                CommandHandler.fileDestination = String.valueOf(jsonObject.get("dir")).replace("\"", "");
            } else {
                writeConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
            writeConfig();
        }
    }

    public static void writeConfig() {
        try {
            JsonWriter writer = new JsonWriter(new FileWriter("config/TextWall.json"));
            writer.beginObject();
            writer.name("prefix").value(MainMod.prefix);
            writer.name("dir").value(CommandHandler.fileDestination);
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}