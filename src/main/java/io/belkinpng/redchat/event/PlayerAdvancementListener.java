package io.belkinpng.redchat.event;

import io.belkinpng.redchat.util.Config;
import io.belkinpng.redchat.util.component.MiniMessageBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import static io.belkinpng.redchat.util.component.Placeholders.ADVANCEMENT;
import static io.belkinpng.redchat.util.component.Placeholders.PLAYER_NAME;

public class PlayerAdvancementListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        var display = event.getAdvancement().getDisplay();
        var player = event.getPlayer();
        if (display != null && display.doesAnnounceToChat()) {
            var builder = new MiniMessageBuilder()
                    .serializeLegacy()
                    .setPapiPlaceholders(player)
                    .setPlaceholder(PLAYER_NAME.key(), player.getName())
                    .setPlaceholder(ADVANCEMENT.key(), display.displayName());

            switch (display.frame()) {
                case GOAL -> builder.setFormat(Config.ADVANCEMENT_GOAL);
                case TASK -> builder.setFormat(Config.ADVANCEMENT_TASK);
                case CHALLENGE -> builder.setFormat(Config.ADVANCEMENT_CHALLENGE);
                default -> {/*ignored*/}
            }
            if (builder.hasEmptyFormat()) {
                event.message(null);
                return;
            }
            event.message(builder.build());
        }
    }
}
