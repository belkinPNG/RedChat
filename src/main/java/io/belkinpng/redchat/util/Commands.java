package io.belkinpng.redchat.util;

import io.belkinpng.redchat.command.MainCommandExecutor;
import io.belkinpng.redchat.command.MainTabCompleter;
import io.belkinpng.redchat.command.PlayerListTabCompleter;
import io.belkinpng.redchat.command.ignore.IgnoreCommandExecutor;
import io.belkinpng.redchat.command.whisper.ReplyCommandExecutor;
import io.belkinpng.redchat.command.whisper.WhisperCommandExecutor;

import static io.belkinpng.redchat.RedChat.getPlugin;

public class Commands {
    public static void register() {
        registerMainCommand();
        registerWhisperCommand();
        registerIgnoreCommand();
        registerReplyCommand();
    }

    private static void registerMainCommand() {
        var command = getPlugin().getCommand("redchat");
        if (command == null) return;
        command.setExecutor(new MainCommandExecutor());
        command.setTabCompleter(new MainTabCompleter());
    }

    private static void registerWhisperCommand() {
        var command = getPlugin().getCommand("msg");
        if (command == null) return;
        command.setExecutor(new WhisperCommandExecutor());
        command.setTabCompleter(new PlayerListTabCompleter());
    }

    private static void registerIgnoreCommand() {
        var command = getPlugin().getCommand("ignore");
        if (command == null) return;
        command.setExecutor(new IgnoreCommandExecutor());
        command.setTabCompleter(new PlayerListTabCompleter());
    }

    private static void registerReplyCommand() {
        var command = getPlugin().getCommand("reply");
        if (command == null) return;
        command.setExecutor(new ReplyCommandExecutor());
    }
}
