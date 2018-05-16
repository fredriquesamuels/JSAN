package org.tect.platform.jsan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class JSONNode extends JSANNode {

    private final Map<String, Object> map;
    private long groupIdSeed = 0;

    JSONNode(String jsonString) {
        super();
        this.map = parseToMap(jsonString);
    }

    @Override
    public List<JSANAttribute> getAttributes() {
        return map.entrySet().stream().map(es -> convertToAttribute(es.getKey(), es.getValue())).collect(Collectors.toList());
    }

    static Map<String, Object> parseToMap(String jsonString) {
        try {
            ObjectMapper var1 = new ObjectMapper();
            return (Map) var1.readValue(jsonString, new TypeReference<Map<String, Object>>() {
            });
        } catch (FileNotFoundException var2) {
            var2.printStackTrace();
            throw new JSONParseError(var2);
        } catch (IOException var3) {
            throw new JSONParseError(var3);
        }
    }

    private JSANAttribute convertToAttribute(String name, Object value) {
        if(value instanceof Map) {
            return new ObjectAttributeImpl(++groupIdSeed, name, value);
        }

        if(value instanceof Collection) {
            return new ArrayAttributeImpl(++groupIdSeed, name, value);
        }

        return new AttributeImpl(name, value);
    }


    class AttributeImpl implements JSANAttribute {

        private final String name;
        private final Object value;

        public AttributeImpl(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getValue() {
            return value;
        }
    }

    private class ArrayAttributeImpl extends JSANArray {
        private final String name;
        private final List<Object> value;

        public ArrayAttributeImpl(long groupId, String name, Object value) {
            super(groupId);
            this.name = name;
            this.value = (List<Object>) value;
        }

        @Override
        public List<JSANAttribute> getAttributes() {
            return value.stream()
                    .map( e -> convertToAttribute(null, e))
                    .collect(Collectors.toList());
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getValue() {
            return value;
        }
    }

    private class ObjectAttributeImpl extends JSANObject {

        private final String name;
        private final Map<String, Object> value;

        public ObjectAttributeImpl(long groupId, String name, Object value) {
            super(groupId);
            this.name = name;
            this.value = (Map<String, Object>) value;
        }

        @Override
        public List<JSANAttribute> getAttributes() {
            return value.entrySet()
                    .stream()
                    .map( e -> convertToAttribute(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getValue() {
            return value;
        }
    }

}
