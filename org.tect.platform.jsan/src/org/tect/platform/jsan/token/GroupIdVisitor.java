package org.tect.platform.jsan.token;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupIdVisitor extends CharVisitor {
    private static final List<Character> VALUES = Stream.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            .collect(Collectors.toList());
    private CharVisitor[] terminals;

    public GroupIdVisitor(CharVisitor... terminals) {
        super();
        this.terminals = terminals;
    }

    @Override
    void visit(JSANTokensImpl tokens, Context context) {
        context.groupIdStr+=context.getChar();
    }

    @Override
    boolean matches(Character character) {
        return VALUES.contains(character);
    }

    @Override
    List<CharVisitor> getTransitions() {
        List<CharVisitor> list = new ArrayList<>();
        list.addAll(createList(terminals));
        list.add(new GroupIdVisitor(terminals));
        return list;
    }
}
