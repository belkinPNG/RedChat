package io.belkinpng.redchat.util.component;

import io.belkinpng.redchat.RedChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {
    private String format;
    private final List<TagResolver> tagResolvers = new ArrayList<>();

    public MessageBuilder(String format) {
        this.format = format;
    }

    public MessageBuilder() {}

    public MessageBuilder setPapiPlaceholders(@NotNull Player player) {
        if (RedChat.papiAvailable) {
            tagResolvers.add(PapiParser.papiTag(player));
        }
        return this;
    }

    public MessageBuilder setFormat(String format) {
        this.format = format;
        return this;
    }

    public MessageBuilder setPapiRelationalPlaceholders(@NotNull Player firstPlayer, @NotNull Player secondPlayer) {
        if (RedChat.papiAvailable) {
            tagResolvers.add(PapiParser.papiRelationalTag(firstPlayer, secondPlayer));
        }
        return this;
    }

    public MessageBuilder setPlaceholder(@Subst("") @NotNull String key, @NotNull Component value) {
        tagResolvers.add(Placeholder.component(key, value));
        return this;
    }

    public MessageBuilder setPlaceholder(@Subst("") @NotNull String key, @NotNull String value) {
        tagResolvers.add(Placeholder.parsed(key, value));
        return this;
    }

    public MessageBuilder setUnparsedPlaceholder(@Subst("") @NotNull String key, @NotNull String value) {
        tagResolvers.add(Placeholder.unparsed(key, value));
        return this;
    }

    public boolean hasEmptyFormat() {
        return format.isEmpty();
    }

    public Component build() {
        return MiniMessage.miniMessage().deserialize(format, TagResolver.resolver(tagResolvers));
    }
}
