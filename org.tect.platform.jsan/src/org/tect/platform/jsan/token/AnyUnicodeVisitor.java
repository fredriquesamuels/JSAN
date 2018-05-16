package org.tect.platform.jsan.token;

public class AnyUnicodeVisitor extends CharVisitor {
    
    @Override
    boolean matches(Character character) {
        return true;
    }

    @Override
    boolean isTerminal() {
        return true;
    }
}
