package org.tect.platform.jsan.token;

import java.util.List;

public class CloseCurlyBracketVisitor extends CharVisitor {

    @Override
    List<CharVisitor> getTransitions() {
        return createList(new PeriodVisitor());
    }

    @Override
    boolean matches(Character character) {
        return character=='}';
    }
}
