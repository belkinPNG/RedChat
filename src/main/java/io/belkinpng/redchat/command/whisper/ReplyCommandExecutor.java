package io.belkinpng.redchat.command.whisper;

import io.belkinpng.redchat.util.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ReplyCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Config.TOO_FEW_ARGS);
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Config.NON_PLAYER_EXECUTOR);
        } else {
            runReplyCommand(player, args);
        }
        return true;
    }

    private void runReplyCommand(@NotNull Player sender, @NotNull String[] args) {
        Player receiver = getReplyTarget(sender);
        if (receiver == null) {
            sender.sendMessage(Config.NOBODY_REPLY);
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        WhisperCommandExecutor.whisper(sender, receiver, message);
    }

    @Nullable
    public Player getReplyTarget(@NotNull Player sender) {
        if (sender.hasMetadata("replyTarget")) {
            List<MetadataValue> metadata = sender.getMetadata("replyTarget");
            String targetUUID = metadata.getFirst().asString();
            return sender.getServer().getPlayer(UUID.fromString(targetUUID));
        }
        return null;
    }
}
