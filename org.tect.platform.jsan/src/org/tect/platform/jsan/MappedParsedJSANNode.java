package org.tect.platform.jsan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MappedParsedJSANNode implements ParsedJSANNode {

    private final ObjectGroupImpl objectGroup;

    public MappedParsedJSANNode() {
        objectGroup = new ObjectGroupImpl(-1);
    }

    @Override
    public ParsedJSANGroup addArray(String name, long groupId) {
        return objectGroup.addArray(name, groupId);
    }

    @Override
    public ParsedJSANGroup addObject(String name, long groupId) {
        return objectGroup.addObject(name, groupId);
    }

    @Override
    public void setValue(String name, Object value) {
        objectGroup.setValue(name, value);
    }

    @Override
    public long  getGroupId() {
        return -1;
    }

    public Map<String, Object> getMap() {
        return Collections.unmodifiableMap(objectGroup);
    }

    public final String toJson() {
        return writeToString(objectGroup);
    }

    String writeToString(ObjectGroupImpl objectGroup) {
        ObjectMapper var1 = new ObjectMapper();
        try {
            return var1.writeValueAsString(objectGroup);
        } catch (JsonProcessingException var3) {
            throw new JSONProcessingError(var3);
        }
    }

    class ListImpl extends ArrayList<Object> implements ParsedJSANGroup {


        private long groupId;

        public ListImpl(long groupId) {
            this.groupId = groupId;
        }

        @Override
        public ParsedJSANGroup addArray(String name, long groupId) {
            Supplier<ParsedJSANGroup> groupSupplier = () -> new ListImpl(groupId);
            return getParsedJSANGroup(groupId, groupSupplier);
        }

        @Override
        public ParsedJSANGroup addObject(String name, long groupId) {
            Supplier<ParsedJSANGroup> groupSupplier = () -> new ObjectGroupImpl(groupId);
            return getParsedJSANGroup(groupId, groupSupplier);
        }

        @Override
        public void setValue(String name, Object value) {
            add(value);
        }

        @Override
        public long getGroupId() {
            return groupId;
        }

        private ParsedJSANGroup getParsedJSANGroup(long groupId, Supplier<ParsedJSANGroup> groupSupplier) {
            ParsedJSANGroup g = findForGroupId(groupId);
            if(g==null) {
                ParsedJSANGroup newGroup = groupSupplier.get();
                add(newGroup);
                return newGroup;
            }
            return g;
        }

        private ParsedJSANGroup findForGroupId(long groupId) {
            return JSANParser.getParsedJSANGroupByIdOrNull(groupId, this);
        }
    }

    private class ObjectGroupImpl extends LinkedHashMap<String, Object> implements ParsedJSANGroup {

        private long groupId;

        public ObjectGroupImpl(long groupId) {
            super();
            this.groupId = groupId;
        }

        @Override
        public ParsedJSANGroup addArray(String name, long groupId) {
            Supplier<ParsedJSANGroup> groupSupplier = () -> new ListImpl(groupId);
            return getParsedJSANGroup(name, groupId, groupSupplier);
        }

        @Override
        public ParsedJSANGroup addObject(String name, long groupId) {
            Supplier<ParsedJSANGroup> groupSupplier = () -> new ObjectGroupImpl(groupId);
            return getParsedJSANGroup(name, groupId, groupSupplier);
        }

        @Override
        public void setValue(String name, Object value) {
            put(name, value);
        }

        @Override
        public long getGroupId() {
            return groupId;
        }

        private ParsedJSANGroup findForGroupId(long groupId) {
            return JSANParser.getParsedJSANGroupByIdOrNull(groupId, values());
        }

        private ParsedJSANGroup getParsedJSANGroup(String name, long groupId, Supplier<ParsedJSANGroup> groupSupplier) {
            ParsedJSANGroup g = findForGroupId(groupId);
            if(g==null) {
                ParsedJSANGroup newGroup = groupSupplier.get();
                put(name, newGroup);
                return newGroup;
            }
            return g;
        }

    }

}
