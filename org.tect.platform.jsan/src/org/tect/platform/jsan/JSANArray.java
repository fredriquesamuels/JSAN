package org.tect.platform.jsan;

public abstract class JSANArray extends JSANGroupAttribute {

    public JSANArray(long groupId) {
        super(groupId);
    }

    @Override
    public final boolean isArray() {
        return true;
    }

    @Override
    public final boolean isElement() {
        return false;
    }
}
