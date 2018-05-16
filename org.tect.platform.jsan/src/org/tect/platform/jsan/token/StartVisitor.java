package org.tect.platform.jsan.token;

import java.util.List;

public class StartVisitor extends CharVisitor {

    @Override
    protected List<CharVisitor> getTransitions() {
        return createList(new AnyUnicodeExceptSCVisitor(),
                new EscapeVisitor());
    }
}