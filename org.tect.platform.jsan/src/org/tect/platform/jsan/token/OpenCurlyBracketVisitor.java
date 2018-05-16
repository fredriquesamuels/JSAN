package org.tect.platform.jsan.token;

import java.util.List;

public class OpenCurlyBracketVisitor extends CharVisitor {

    @Override
    List<CharVisitor> getTransitions() {
        return createList(new GroupIdVisitor(new CloseCurlyBracketVisitor()),
                new CloseCurlyBracketVisitor());
    }

    @Override
    boolean matches(Character character) {
        return character=='{';
    }
}
