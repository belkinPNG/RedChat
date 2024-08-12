package io.belkinpng.redchat.util.storage;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.belkinpng.redchat.RedChat.LOGGER;
import static io.belkinpng.redchat.RedChat.getPlugin;

public class JsonStorage {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final File dataPath = new File(getPlugin().getDataFolder(), "data");

    @NotNull
    public static File getFile(@NotNull UUID uuid) {
        makeDataDir();
        File file = new File(dataPath, uuid + ".json");

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    LOGGER.error("Failed to create file: {}", file.getPath());
                }
            } catch (IOException exception) {
                LOGGER.error("Error creating file: {}", file.getPath(), exception);
            }
        }
        return file;
    }

    @Nullable
    public static JsonObject readJsonFromFile(@NotNull File file) {
        try (FileReader reader = new FileReader(file)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement != null && jsonElement.isJsonObject()) {
                return jsonElement.getAsJsonObject();
            }
        } catch (IOException exception) {
            LOGGER.error("Error reading file: {}", file.getPath(), exception);
        }
        return null;
    }

    public static List<UUID> getUUIDList(JsonObject jsonObject, String key) {
        return Optional.ofNullable(jsonObject.getAsJsonArray(key))
                .map(jsonArray -> StreamSupport.stream(jsonArray.spliterator(), false)
                        .map(JsonElement::getAsString)
                        .map(uuidString -> {
                            try {
                                return UUID.fromString(uuidString);
                            } catch (IllegalArgumentException e) {
                                LOGGER.error("Invalid UUID format: {}", uuidString);
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    @NotNull
    public static Map<UUID, JsonObject> readDataFolder() {
        makeDataDir();
        Map<UUID, JsonObject> objects = new HashMap<>();
        try (Stream<Path> paths = Files.walk(dataPath.toPath())) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        UUID fileName = formatId(path.getFileName().toString());
                        if (fileName == null) return;
                        objects.put(fileName, readJsonFromFile(path.toFile()));
                    });
        } catch (IOException exception) {
            LOGGER.error("Error reading from data directory: {}", dataPath.getPath(), exception);
        }
        return objects;
    }

    @Nullable
    private static UUID formatId(@NotNull String fileName) {
        try {
            return UUID.fromString(fileName.substring(0, fileName.length() - 5));
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid file name format for UUID: {}", fileName);
            return null;
        }
    }

    private static void makeDataDir() {
        if (!dataPath.exists() && !dataPath.mkdirs()) {
            LOGGER.error("Failed to create directories for path: {}", dataPath.getPath());
        }
    }
}
