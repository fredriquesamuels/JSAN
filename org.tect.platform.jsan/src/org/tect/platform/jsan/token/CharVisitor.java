package org.tect.platform.jsan.token;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CharVisitor {

    List<CharVisitor> getTransitions()  {
        return createList(new AnyUnicodeExceptSCVisitor(),
                new EscapeVisitor(),
                new OpenCurlyBracketVisitor(),
                new OpenSquareBracket(),
                new PeriodVisitor());
    }

    boolean isTerminal(){
        return false;
    }

    void visit(JSANTokensImpl tokens, Context context){}

    final CharVisitor getTransition(Character character) {
        CharVisitor charVisitor = getTransitions()
                .stream()
                .filter( v -> v.matches(character))
                .findFirst()
                .orElse(null);
        return charVisitor;
    }

    final List<CharVisitor> createList(CharVisitor... vs) {
        return Stream.of(vs).collect(Collectors.toList());
    }

    boolean matches(Character character) {
        return false;
    }
}