package org.tect.platform.jsan;

import java.util.ArrayList;
import java.util.List;

public final class JSANMapper<AT extends JSANAttribute, KV extends JSANKeyValue> {

    private final JSANConverterContext context;
    private JSANKeyValueFactory<KV> keyValueFactory;
    private boolean hideGroupIds = false;

    public JSANMapper(JSANKeyValueFactory<KV> keyValueFactory) {
        this(keyValueFactory, new JSANConverterContext());
    }

    JSANMapper(JSANKeyValueFactory keyValueFactory, JSANConverterContext context) {
        this.keyValueFactory = keyValueFactory;
        this.context = context;
    }

    public final List<KV> toKeyValues(JSANNode<AT> node) {
        List<AT> attributes = node.getAttributes();
        return createKeyValues(attributes, context);
    }

    public final JSANMapper<AT, KV> hideGroupIds() {
        this.hideGroupIds = true;
        return this;
    }


    private List<KV> toKeyValues(JSANGroupAttribute g) {
        return createKeyValues(g.getAttributes(), context);
    }

    private List<KV> createKeyValues(List<AT> attributes, JSANConverterContext context) {
        List<KV> keyValues = new ArrayList<>();

        attributes.forEach( a -> {
            String encodedName = NameEncoder.encode(a.getName());

            if(a instanceof JSANGroupAttribute) {
                keyValues.addAll(groupAttributeToJSAN(context, (JSANGroupAttribute) a, encodedName));
                return;
            }

            KV kv = keyValueFactory.create(context.createName(encodedName), a.getValue(), context.getGroupIds());
            keyValues.add(kv);
        });

        return keyValues;
    }

    private List<KV> groupAttributeToJSAN(JSANConverterContext context, JSANGroupAttribute a, String nullCheckedName) {
        JSANGroupAttribute g = a;
        long id = g.getGroupId();
        String finalName = null;
        JSANGroupAttribute parentAttribute = context.getParentAttribute();

        finalName = getGroupFinalName(context, nullCheckedName, g, id, finalName, parentAttribute);

        JSANConverterContext newContext = context.setParentAttribute(g)
                .setPrefix(finalName)
                .addGroupId(id);

        JSANMapper<AT, KV> childConverter = new JSANMapper<>(keyValueFactory, newContext);
        if(hideGroupIds) {
            childConverter.hideGroupIds();
        }

        return childConverter
                .toKeyValues(g);
    }

    private String getGroupFinalName(JSANConverterContext context, String nullCheckedName, JSANGroupAttribute g, long id, String finalName, JSANGroupAttribute parentAttribute) {
        if(g.isElement()) {
            finalName = context.createName(nullCheckedName);
            if(parentAttribute!=null && parentAttribute.isArray()) {
                finalName = context.createName(String.format("{%d}", id));
                if(hideGroupIds) {
                    finalName = context.createName(String.format("{}"));
                }
            }
        }

        if(g.isArray()) {
            finalName = context.createName(String.format("%s[%d]", nullCheckedName, id));
            if(hideGroupIds) {
                finalName = context.createName(String.format("%s[]", nullCheckedName));
            }
        }
        return finalName;
    }
}
