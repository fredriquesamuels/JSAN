package org.tect.platform.jsan.token;

import java.util.List;

public class PeriodVisitor extends CharVisitor {
    @Override
    void visit(JSANTokensImpl tokens, Context context) {
        tokens.createObject(context);
    }

    @Override
    List<CharVisitor> getTransitions() {
        return createList(new AnyUnicodeExceptSCVisitor(), new EscapeVisitor());
    }

    @Override
    boolean matches(Character character) {
        return character=='.';
    }
}
