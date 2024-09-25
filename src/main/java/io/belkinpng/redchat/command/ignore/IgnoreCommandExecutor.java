package io.belkinpng.redchat.command.ignore;

import io.belkinpng.redchat.util.component.MiniMessageBuilder;
import io.belkinpng.redchat.util.Config;
import io.belkinpng.redchat.util.storage.IgnoredPlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static io.belkinpng.redchat.util.component.Placeholders.PLAYER_NAME;

public class IgnoreCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Config.TOO_FEW_ARGS);
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Config.NON_PLAYER_EXECUTOR);
        } else {
            runIgnoreCommand(player, args);
        }
        return true;
    }

    public void runIgnoreCommand(@NotNull Player player, @NotNull String[] args) {
        Player ignoredPlayer = Bukkit.getPlayer(args[0]);
        if (ignoredPlayer == null) {
            player.sendMessage(Config.PLAYER_NOT_FOUND);
            return;
        }
        if (ignoredPlayer.equals(player)) {
            player.sendMessage(Config.CANT_IGNORE_YOURSELF);
            return;
        }

        var builder = new MiniMessageBuilder()
                .setPapiPlaceholders(ignoredPlayer)
                .setPlaceholder(PLAYER_NAME.key(), ignoredPlayer.getName());

        var players = IgnoredPlayers.by(player);
        if (players.contains(ignoredPlayer.getUniqueId())) {
            IgnoredPlayers.remove(player, ignoredPlayer);
            builder.setFormat(Config.STOP_IGNORING);
        } else {
            IgnoredPlayers.add(player, ignoredPlayer);
            builder.setFormat(Config.START_IGNORING);
        }

        player.sendMessage(builder.build());
    }
}
