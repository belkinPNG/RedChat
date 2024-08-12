package io.belkinpng.redchat.command;

import io.belkinpng.redchat.util.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            runWrongArguments(sender);
        } else {
            switch (args[0]) {
                case "reload" -> runConfigReload(sender);
                default -> runWrongArguments(sender);
            }
        }
        return true;
    }

    public void runConfigReload(@NotNull CommandSender sender) {
        Config.load();
        sender.sendMessage(Config.RELOAD_MESSAGE);
    }

    public void runWrongArguments(@NotNull CommandSender sender) {
        sender.sendMessage(Config.TOO_FEW_ARGS);
    }
}
