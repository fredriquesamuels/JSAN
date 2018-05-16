package org.tect.platform.jsan;

public abstract class JSANObject<AT extends JSANAttribute> extends JSANGroupAttribute<AT> {

    public JSANObject(long groupId) {
        super(groupId);
    }

    @Override
    public final boolean isArray() {
        return false;
    }

    @Override
    public final boolean isElement() {
        return true;
    }
}
