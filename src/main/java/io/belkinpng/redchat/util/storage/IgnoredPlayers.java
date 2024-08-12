package io.belkinpng.redchat.util.storage;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static io.belkinpng.redchat.RedChat.LOGGER;
import static io.belkinpng.redchat.RedChat.getPlugin;
import static io.belkinpng.redchat.util.storage.JsonStorage.gson;

public class IgnoredPlayers {
    private static final Map<UUID, List<UUID>> ignoreMap = new HashMap<>();

    public static boolean isIgnoring(@NotNull Player player, @NotNull Player target) {
        return isIgnoring(player.getUniqueId(), target.getUniqueId());
    }

    public static boolean isIgnoring(@NotNull UUID playerId, @NotNull UUID targetId) {
        var list = IgnoredPlayers.by(playerId);
        return list.contains(targetId);
    }

    @NotNull
    public static List<UUID> by(@NotNull Player player) {
        return by(player.getUniqueId());
    }

    @NotNull
    public static List<UUID> by(@NotNull UUID uuid) {
        return ignoreMap.getOrDefault(uuid, List.of());
    }

    public static void add(@NotNull Player key, @NotNull Player value) {
        add(key.getUniqueId(), value.getUniqueId());
    }

    public static void add(@NotNull UUID key, @NotNull UUID value) {
        ignoreMap.computeIfAbsent(key, k -> new ArrayList<>())
                .add(value);
        writeChangesToFile(key);
    }

    public static void remove(@NotNull Player key, @NotNull Player value) {
        remove(key.getUniqueId(), value.getUniqueId());
    }

    public static void remove(@NotNull UUID key, @NotNull UUID value) {
        var uuid = ignoreMap.get(key);
        if (uuid == null) return;
        uuid.remove(value);

        if (uuid.isEmpty()) {
            ignoreMap.remove(key);
        }
        writeChangesToFile(key);
    }

    public static void writeChangesToFile(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            File file = JsonStorage.getFile(uuid);
            JsonObject jsonObject = Optional.ofNullable(JsonStorage.readJsonFromFile(file)).orElseGet(JsonObject::new);
            jsonObject.add("ignores", gson.toJsonTree(ignoreMap.get(uuid)));

            try (FileWriter fileWriter = new FileWriter(file)) {
                gson.toJson(jsonObject, fileWriter);
            } catch (IOException exception) {
                LOGGER.error("An error occurred while writing to file: {}", file.getPath(), exception);
            }
        });
    }

    public static void loadAllFromFile() {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Map<UUID, JsonObject> jsonMap = JsonStorage.readDataFolder();
            if (jsonMap.isEmpty()) return;

            for (var entry : jsonMap.entrySet()) {
                List<UUID> ignoredPlayers = JsonStorage.getUUIDList(entry.getValue(), "ignores");
                ignoreMap.put(entry.getKey(), ignoredPlayers);
            }
        });
    }
}