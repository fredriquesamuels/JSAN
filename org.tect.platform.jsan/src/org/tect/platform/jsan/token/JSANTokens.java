package org.tect.platform.jsan.token;

import java.util.function.Consumer;

public interface JSANTokens {
    int size();
    void forEach(Consumer<JSANToken> consumer);
    JSANToken get(int index);
}
