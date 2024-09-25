package io.belkinpng.redchat.util;

import io.belkinpng.redchat.util.component.MiniMessageBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import static io.belkinpng.redchat.RedChat.LOGGER;
import static io.belkinpng.redchat.RedChat.getPlugin;

public class Config {
    private static YamlConfiguration config;
    public static String CHAT_FORMAT;
    public static String PLAYER_JOIN;
    public static String PLAYER_QUIT;
    public static String ADVANCEMENT_TASK;
    public static String ADVANCEMENT_CHALLENGE;
    public static String ADVANCEMENT_GOAL;
    public static String WHISPER_FORMAT_SEND;
    public static String WHISPER_FORMAT_RECEIVE;
    public static String START_IGNORING;
    public static String STOP_IGNORING;
    public static String IGNORED_CHATTING;
    public static Component CANT_IGNORE_YOURSELF;
    public static Component PLAYER_NOT_FOUND;
    public static Component NOBODY_REPLY;
    public static Component RELOAD_MESSAGE;
    public static Component TOO_FEW_ARGS;
    public static Component NON_PLAYER_EXECUTOR;

    public static void load() {
        loadConfigFile();
        CHAT_FORMAT = getString("messages.chat.format");
        PLAYER_JOIN = getString("messages.chat.join");
        PLAYER_QUIT = getString("messages.chat.quit");
        ADVANCEMENT_TASK = getString("messages.chat.advancement.task");
        ADVANCEMENT_GOAL = getString("messages.chat.advancement.goal");
        ADVANCEMENT_CHALLENGE = getString("messages.chat.advancement.challenge");
        WHISPER_FORMAT_SEND = getString("messages.whisper.format.send");
        WHISPER_FORMAT_RECEIVE = getString("messages.whisper.format.receive");
        START_IGNORING = getString("messages.ignore.start");
        STOP_IGNORING = getString("messages.ignore.stop");
        IGNORED_CHATTING = getString("messages.ignore.ignored-chat");
        CANT_IGNORE_YOURSELF = getComponent("messages.ignore.cant-ignore-yourself");
        NOBODY_REPLY = getComponent("messages.whisper.reply-not-found");
        PLAYER_NOT_FOUND = getComponent("messages.player-not-found");
        RELOAD_MESSAGE = getComponent("messages.config-reload");
        TOO_FEW_ARGS = getComponent("messages.too-few-arguments");
        NON_PLAYER_EXECUTOR = getComponent("messages.can-executed-player-only");
    }

    @NotNull
    private static String getString(@NotNull String path) {
        return Objects.requireNonNullElse(config.getString(path), "");
    }

    @NotNull
    private static Component getComponent(@NotNull String path) {
        String string = config.getString(path);
        if (string == null) {
            LOGGER.warn("Config string {} is null, empty component will be returned.", path);
            return Component.empty();
        }
        return new MiniMessageBuilder(string).build();
    }

    private static void loadConfigFile() {
        File file = new File(getPlugin().getDataFolder(), "config.yml");
        if (!file.exists()) {
            getPlugin().saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
}
