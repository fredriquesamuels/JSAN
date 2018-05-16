package org.tect.platform.jsan;

import org.tect.platform.jsan.token.JSANToken;
import org.tect.platform.jsan.token.JSANTokenizer;
import org.tect.platform.jsan.token.JSANTokens;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class JSANParser<KV extends JSANKeyValue> {

    private JSANTokenizer tokenizer = new JSANTokenizer();

    public final <T extends ParsedJSANNode> T parse(List<KV> keyValues, Class<T> aClass) {
        T newInstance = createNewInstance(aClass);
        keyValues.forEach( kv -> parseKeyValue(newInstance, kv));
        return newInstance;
    }

    public final <T extends ParsedJSANNode> T parse(List<KV> keyValues, T node) {
        keyValues.forEach( kv -> parseKeyValue(node, kv));
        return node;
    }

    public static ParsedJSANGroup getParsedJSANGroupByIdOrNull(long groupId, Collection<Object> values) {
        return (ParsedJSANGroup) values.stream()
                .filter( v -> v instanceof ParsedJSANGroup && ((ParsedJSANGroup)v).getGroupId()==groupId)
                .findFirst()
                .orElse(null);
    }

    public static String parseToJson(List<JSANKeyValue> values) {
        return new JSANParser<>().parse(values, MappedParsedJSANNode.class).toJson();
    }

    public static Map<String, Object> parseToMap(List<JSANKeyValue> values) {
        return new JSANParser<>().parse(values, MappedParsedJSANNode.class).getMap();
    }

    private void parseKeyValue(ParsedJSANNode node, JSANKeyValue kv) {
        JSANTokens tokens = tokenizer.tokenize(kv.getName(), kv.getGroupIds());

        ParsedJSANGroup group = node;
        int size = tokens.size();
        for (int i = 0; i< size; i++) {
            JSANToken token = tokens.get(i);
            if(token.isList()) {
                group = group.addArray(token.getName(), token.getGroupId());
                if(i==size -1) {
                    group.setValue(null, kv.getValue());
                }
            } else if(token.isObject()) {
                group = group.addObject(token.getName(), token.getGroupId());
            } else {
                group.setValue(token.getName(), kv.getValue());
            }
        }

    }

    private <T extends ParsedJSANNode> T createNewInstance(Class<T> aClass) {
        T node;
        try {
            node = aClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return node;
    }
}
