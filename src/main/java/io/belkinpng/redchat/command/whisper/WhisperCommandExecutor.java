package io.belkinpng.redchat.command.whisper;

import io.belkinpng.redchat.util.Config;
import io.belkinpng.redchat.util.component.MiniMessageBuilder;
import io.belkinpng.redchat.util.storage.IgnoredPlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static io.belkinpng.redchat.RedChat.getPlugin;
import static io.belkinpng.redchat.util.component.MessagePlaceholder.*;

public class WhisperCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Config.NON_PLAYER_EXECUTOR);
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Config.TOO_FEW_ARGS);
        } else {
            runWhisperCommand(player, args);
        }
        return true;
    }

    private void runWhisperCommand(@NotNull Player sender, @NotNull String[] args) {
        Player receiver = Bukkit.getPlayer(args[0]);
        if (receiver == null) {
            sender.sendMessage(Config.PLAYER_NOT_FOUND);
            return;
        }
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        whisper(sender, receiver, message);
    }

    public static void whisper(@NotNull Player sender, @NotNull Player receiver, @NotNull String message) {
        var builder = new MiniMessageBuilder()
                .setPapiRelationalPlaceholders(sender, receiver)
                .setPlaceholder(SENDER.key(), sender.getName())
                .setPlaceholder(RECEIVER.key(), receiver.getName())
                .setPlaceholder(MESSAGE.key(), message);

        sender.sendMessage(builder
                .setFormat(Config.WHISPER_FORMAT_SEND)
                .build());
        setReplyTarget(sender, receiver);

        if (IgnoredPlayers.isIgnoring(receiver, sender)) return;

        receiver.sendMessage(builder
                .setFormat(Config.WHISPER_FORMAT_RECEIVE)
                .build());
        setReplyTarget(receiver, sender);
    }

    public static void setReplyTarget(@NotNull Player sender, @NotNull Player target) {
        String uuid = target.getUniqueId().toString();
        sender.setMetadata("replyTarget", new FixedMetadataValue(getPlugin(), uuid));
    }
}
