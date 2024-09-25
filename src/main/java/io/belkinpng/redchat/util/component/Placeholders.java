package io.belkinpng.redchat.util.component;

import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

public enum Placeholders {

    PLAYER_NAME("player_name"),
    SENDER("sender"),
    RECEIVER("receiver"),
    MESSAGE("message"),
    ADVANCEMENT("advancement"),
    PAPI("papi");

    private final String key;

    Placeholders(String key) {
        this.key = key;
    }

    @Subst("")
    @NotNull
    public String key() {
        return key;
    }
}
