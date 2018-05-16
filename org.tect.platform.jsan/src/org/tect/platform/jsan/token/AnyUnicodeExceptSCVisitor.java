package org.tect.platform.jsan.token;

import java.util.ArrayList;
import java.util.List;

class AnyUnicodeExceptSCVisitor extends CharVisitor {

    private static List<Character> NON_ASCII;

    static  {
        NON_ASCII = new ArrayList<>();
        NON_ASCII.add('\\');
        NON_ASCII.add('{');
        NON_ASCII.add('}');
        NON_ASCII.add('[');
        NON_ASCII.add(']');
        NON_ASCII.add('.');
    }

    @Override
    boolean matches(Character character) {
        return !NON_ASCII.contains(character);
    }

    @Override
    boolean isTerminal() {
        return true;
    }
}