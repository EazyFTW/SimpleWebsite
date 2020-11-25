package com.eazyftw.simplewebsite.file;

import com.eazyftw.simplewebsite.web.mapping.Mapping;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private static Config instance;
    private JsonObject root;

    public static Config getInstance() {
        if(instance == null)
            instance = new Config();

        return instance;
    }

    private Config() {
        File file = new File("config.json");

        if(!file.exists()) {
            try {
                InputStream src = Config.class.getResourceAsStream("/config.json");
                Files.copy(src, Paths.get(file.toURI()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            JsonParser jsonParser = new JsonParser();
            root = (JsonObject) jsonParser.parse(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return root.has("port") ? root.get("port").getAsInt() : 3000;
    }

    public List<Mapping> getMappings() {
        List<Mapping> mappings = new ArrayList<>();
        JsonArray mArray = root.getAsJsonArray("mappings");

        mArray.forEach(mElement -> {
            if(mElement instanceof JsonObject) {
                JsonObject mObject = mElement.getAsJsonObject();

                if(mObject.has("method") && mObject.has("url") && mObject.has("fileName")) {
                    mappings.add(new Mapping(Mapping.Method.fromString(mObject.get("method").getAsString()), mObject.get("url").getAsString(), mObject.get("fileName").getAsString()));
                }
            }
        });

        return mappings;
    }
}
