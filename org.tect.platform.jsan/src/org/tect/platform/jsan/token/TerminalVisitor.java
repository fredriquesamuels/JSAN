package org.tect.platform.jsan.token;

import java.util.ArrayList;
import java.util.List;

class TerminalVisitor extends CharVisitor {

    @Override
    public void visit(JSANTokensImpl tokens, Context context) {
        if(context.name.length()==context.lastTokenStartIndex) {
            return;
        }
        tokens.createToken(context, context.index);
    }

    @Override
    List<CharVisitor> getTransitions() {
        ArrayList<CharVisitor> list = new ArrayList<>();
        list.add(new CloseSquareBracket());
        return list;
    }

    @Override
    boolean isTerminal() {
        return false;
    }
}