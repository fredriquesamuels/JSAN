package org.tect.platform.jsan;

import java.util.List;

public interface JSANKeyValueFactory<KV extends JSANKeyValue> {
    KV create(String name, Object value, List<Long> groupIds);
}
