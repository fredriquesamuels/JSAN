package org.tect.platform.jsan;

public abstract class JSANNode<AT extends JSANAttribute> extends JSANObject<AT> {

    JSANNode() {
        super(-1);
    }

    @Override
    public final String getName() {
        return "";
    }

    @Override
    public final Object getValue() {
        return null;
    }

    public static JSANNode fromJSON(String jsonString) {
        return new JSONNode(jsonString);
    }
}
