package org.tect.platform.jsan;

import java.util.List;

public abstract class JSANGroupAttribute<AT extends JSANAttribute> implements JSANAttribute {

    private long groupId;

    public JSANGroupAttribute(long groupId) {
        this.groupId = groupId;
    }

    public final long getGroupId(){
        return groupId;
    }

    public abstract List<AT> getAttributes();
    public abstract boolean isArray();
    public abstract boolean isElement();
}
