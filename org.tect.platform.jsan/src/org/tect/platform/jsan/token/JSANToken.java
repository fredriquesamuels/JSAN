package org.tect.platform.jsan.token;

public interface JSANToken {
    String getName();
    String getRaw();
    Long getGroupId();
    boolean isList();
    boolean isObject();
}
