package org.tect.platform.jsan;

public interface ParsedJSANGroup {
    ParsedJSANGroup addArray(String name, long groupId);
    ParsedJSANGroup addObject(String name, long groupId);
    void setValue(String name, Object value);
    long getGroupId();
}
