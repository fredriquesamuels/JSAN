package org.tect.platform.jsan;

import java.util.List;

public interface JSANKeyValue {
    String getName();
    Object getValue();
    List<Long> getGroupIds();
}
