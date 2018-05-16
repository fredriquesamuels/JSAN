package org.tect.platform.jsan.token;

import java.util.List;

public class EscapeVisitor extends CharVisitor {
    @Override
    List<CharVisitor> getTransitions() {
        return createList(new AnyUnicodeVisitor());
    }

    @Override
    boolean matches(Character character) {
        return character=='\\';
    }
}
