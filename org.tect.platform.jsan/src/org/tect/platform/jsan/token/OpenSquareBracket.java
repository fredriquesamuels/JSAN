package org.tect.platform.jsan.token;

import java.util.List;

class OpenSquareBracket extends CharVisitor {

    @Override
    protected List<CharVisitor> getTransitions() {
        return createList(new GroupIdVisitor(new CloseSquareBracket()), new CloseSquareBracket());
    }

    @Override
    boolean matches(Character character) {
        if(character=='[') {
            return true;
        }
        return false;
    }

    @Override
    boolean isTerminal() {
            return false;
        }
}