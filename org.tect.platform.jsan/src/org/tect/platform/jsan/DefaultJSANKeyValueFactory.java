package org.tect.platform.jsan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultJSANKeyValueFactory implements JSANKeyValueFactory<JSANKeyValue> {

    @Override
    public JSANKeyValue create(String name, Object value, List<Long> groupIds) {
        return new JSANKeyValueImpl(name, value, groupIds);
    }

    private class JSANKeyValueImpl implements JSANKeyValue {

        private final String name;
        private final Object value;
        private final List<Long> groupIds = new ArrayList<>();

        public JSANKeyValueImpl(String name, Object value, List<Long> groupIds) {
            this.name = name;
            this.value = value;
            this.groupIds.addAll(groupIds);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public List<Long> getGroupIds() {
            return Collections.unmodifiableList(groupIds);
        }
    }
}
