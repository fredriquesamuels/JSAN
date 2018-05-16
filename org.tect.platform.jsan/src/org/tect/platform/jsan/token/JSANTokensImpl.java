package org.tect.platform.jsan.token;


import org.tect.platform.jsan.NameEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class JSANTokensImpl implements JSANTokens {

    final List<JSANToken> tokens;

    public JSANTokensImpl() {
        this.tokens = new ArrayList<>();
    }

    public JSANTokens tokenize(String name, List<Long> groupIds) {

        Context context = new Context(0, name, groupIds);
        CharVisitor visitor = new StartVisitor();

        do {
            visitor = visitor.getTransition(context.getChar());
            if(visitor==null) {
                throw new UnexpectedCharacter(context.index, context.getChar());
            }
            visitor.visit(this, context);
            context = context.nextChar();
        } while (context.index < name.length());

        if(!visitor.isTerminal()) {
            throw new UnexpectedEOF();
        }

        TerminalVisitor terminalVisitor = new TerminalVisitor();
        terminalVisitor.visit(this, context);

        return this;
    }

    @Override
    public int size() {
        return tokens.size();
    }

    @Override
    public void forEach(Consumer<JSANToken> consumer) {
        tokens.forEach(consumer);
    }

    @Override
    public JSANToken get(int index) {
        return tokens.get(index);
    }

    public void createToken(Context context) {
        createToken(context, context.index+1);
    }

    public void createToken(Context context, int endIndex) {
        final int startIndex = context.lastTokenStartIndex;
        context.lastTokenStartIndex = endIndex;

        ValueToken token = new ValueToken(context, startIndex, endIndex);
        tokens.add(token);
    }

    public void createList(Context context) {
        createList(context, context.index+1);
    }

    public void createList(Context context, int endIndex) {
        final int startIndex = context.lastTokenStartIndex;
        context.lastTokenStartIndex = endIndex;
        ListToken token = new ListToken(context, startIndex, endIndex, context.groupCount);
        tokens.add(token);
        context.groupCount++;
        context.groupIdStr="";
    }

    public void createObject(Context context) {
        createObject(context, context.index+1);
    }

    public void createObject(Context context, int endIndex) {
        final int startIndex = context.lastTokenStartIndex;
        context.lastTokenStartIndex = endIndex;
        ObjectToken token = new ObjectToken(context, startIndex, endIndex, context.groupCount);
        tokens.add(token);
        context.groupCount++;
        context.groupIdStr="";
    }

    class ValueToken implements JSANToken {

        protected final Context context;
        protected final int startIndex;
        protected final int endIndex;

        public ValueToken(Context context, int startIndex, int endIndex) {
            this.context = context;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public String getName() {
            return context.name.substring(startIndex, endIndex);
        }

        @Override
        public String getRaw() {
            return context.name.substring(startIndex, endIndex);
        }

        @Override
        public Long getGroupId() {
            return null;
        }

        @Override
        public boolean isList() {
            return false;
        }

        @Override
        public boolean isObject() {
            return false;
        }
    }

    private class ListToken extends ValueToken{
        private final String groupIdStr;
        private int groupIndex;

        public ListToken(Context context, int startIndex, int endIndex, int groupIndex) {
            super(context, startIndex, endIndex);
            this.groupIndex = groupIndex;
            this.groupIdStr = context.groupIdStr;
        }

        @Override
        public String getName() {
            String raw = getRaw();
            int i = raw.lastIndexOf('[');
            String substring = raw.substring(0, i);
            return NameEncoder.decode(substring);
        }

        @Override
        public Long getGroupId() {
            if(groupIdStr.isEmpty()) {
                if(context.groupIds.isEmpty()) {
                    return null;
                }
                return context.groupIds.get(groupIndex);
            }
            return Long.valueOf(groupIdStr);
        }

        @Override
        public boolean isList() {
            return true;
        }
    }

    private class ObjectToken extends ValueToken{
        private final String groupIdStr;
        private int groupIndex;

        public ObjectToken(Context context, int startIndex, int endIndex, int groupIndex) {
            super(context, startIndex, endIndex);
            this.groupIndex = groupIndex;
            this.groupIdStr = context.groupIdStr;
        }

        @Override
        public String getName() {
            String raw = getRaw();
            String substring = raw.substring(0, raw.length()-1);
            if(raw.matches(".*\\{*\\d*\\}\\.$")) {
                int i = raw.lastIndexOf('{');
                substring = raw.substring(0, i);
            }
            return NameEncoder.decode(substring);
        }

        @Override
        public Long getGroupId() {
            if(groupIdStr.isEmpty()) {
                if(context.groupIds.isEmpty()) {
                    return null;
                }
                return context.groupIds.get(groupIndex);
            }
            return Long.valueOf(groupIdStr);
        }

        @Override
        public boolean isObject() {
            return true;
        }
    }
}