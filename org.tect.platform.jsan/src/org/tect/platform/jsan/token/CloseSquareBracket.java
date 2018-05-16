package org.tect.platform.jsan.token;

import java.util.List;

class CloseSquareBracket extends CharVisitor {

    @Override
    void visit(JSANTokensImpl tokens, Context context) {
        tokens.createList(context);
    }

    @Override
    boolean matches(Character character) {
        if(character==']') {
            return true;
        }
        return false;
    }

    @Override
    List<CharVisitor> getTransitions() {
        return createList(new OpenCurlyBracketVisitor(), new OpenSquareBracket(),new PeriodVisitor());
    }

    @Override
    boolean isTerminal() {
        return true;
    }
}