package io.belkinpng.redchat.event;

import io.belkinpng.redchat.util.Config;
import io.belkinpng.redchat.util.component.MessageBuilder;
import io.belkinpng.redchat.util.storage.IgnoredPlayers;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import static io.belkinpng.redchat.util.component.MessagePlaceholder.*;

public class AsyncChatListener implements Listener {
    private static final ChatRenderer renderer = new ChatRenderer() {
        @NotNull
        @Override
        public Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
            boolean isIgnoring = (viewer instanceof Player player) && IgnoredPlayers.isIgnoring(player, source); //cringe twice check ((((
            String format = isIgnoring ? Config.IGNORED_CHATTING : Config.CHAT_FORMAT;

            return new MessageBuilder(format)
                    .setPapiPlaceholders(source)
                    .setPlaceholder(PLAYER_NAME.key(), source.getName())
                    .setPlaceholder(MESSAGE.key(), message)
                    .build();
        }
    };

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncChat(AsyncChatEvent event) {
        event.viewers().removeIf(viewer -> {
            if (viewer instanceof Player receiver) {
                boolean isIgnoring = IgnoredPlayers.isIgnoring(receiver, event.getPlayer());
                return isIgnoring && Config.IGNORED_CHATTING.isBlank();
            }
            return false;
        });

        event.renderer(renderer);
    }
}
