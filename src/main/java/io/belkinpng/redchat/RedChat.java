package io.belkinpng.redchat;

import io.belkinpng.redchat.event.AsyncChatListener;
import io.belkinpng.redchat.event.PlayerAdvancementListener;
import io.belkinpng.redchat.event.PlayerSessionListener;
import io.belkinpng.redchat.util.Commands;
import io.belkinpng.redchat.util.Config;
import io.belkinpng.redchat.util.storage.IgnoredPlayers;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import static org.bukkit.Bukkit.getPluginManager;

public final class RedChat extends JavaPlugin {

    private static JavaPlugin plugin;
    public static Logger LOGGER;
    public static boolean papiAvailable;

    @Override
    public void onEnable() {
        plugin = this;
        LOGGER = getSLF4JLogger();
        papiAvailable = isPapiEnabled();
        Config.load();
        IgnoredPlayers.loadAllFromFile();
        Commands.register();
        getPluginManager().registerEvents(new AsyncChatListener(), plugin);
        getPluginManager().registerEvents(new PlayerSessionListener(), plugin);
        getPluginManager().registerEvents(new PlayerAdvancementListener(), plugin);
    }

    @NotNull
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    private static boolean isPapiEnabled() {
        var plugin = getPluginManager().getPlugin("PlaceholderAPI");
        boolean isEnabled = plugin != null && plugin.isEnabled();
        if (!isEnabled) {
            LOGGER.info("PlaceholderAPI not found, placeholders from this plugin won't work");
        }
        return isEnabled;
    }
}
