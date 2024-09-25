package io.belkinpng.redchat.event;

import io.belkinpng.redchat.util.Config;
import io.belkinpng.redchat.util.component.MiniMessageBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.belkinpng.redchat.util.component.MessagePlaceholder.PLAYER_NAME;

public class PlayerSessionListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (Config.PLAYER_JOIN.isEmpty()) {
            event.joinMessage(null);
            return;
        }
        event.joinMessage(new MiniMessageBuilder(Config.PLAYER_JOIN)
                .setPapiPlaceholders(player)
                .setPlaceholder(PLAYER_NAME.key(), player.getName())
                .build()
        );
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        if (Config.PLAYER_QUIT.isEmpty()) {
            event.quitMessage(null);
            return;
        }
        event.quitMessage(new MiniMessageBuilder(Config.PLAYER_QUIT)
                .setPapiPlaceholders(player)
                .setPlaceholder(PLAYER_NAME.key(), player.getName())
                .build()
        );
    }
}
