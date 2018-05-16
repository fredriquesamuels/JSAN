package org.tect.platform.jsan.token;

import java.util.List;

class Context {
    final String name;
    final List<Long> groupIds;
    int index;
    int lastTokenStartIndex = 0;
    int groupCount = 0;
    String groupIdStr="";

    public Context(int index, String name, List<Long> groupIds) {
        this.index = index;
        this.name = name;
        this.groupIds = groupIds;
    }

    Context nextChar() {
        Context context = new Context(++index, name, groupIds);
        context.lastTokenStartIndex=this.lastTokenStartIndex;
        context.groupCount=this.groupCount;
        context.groupIdStr=this.groupIdStr;
        return context;
    }

    Context setTokenIndex() {
        lastTokenStartIndex = index;
        return this;
    }

    Character getChar() {
        return name.charAt(index);
    }
}
