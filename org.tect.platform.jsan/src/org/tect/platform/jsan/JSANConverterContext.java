package org.tect.platform.jsan;

import java.util.ArrayList;
import java.util.List;

public class JSANConverterContext {
    private JSANGroupAttribute parentAttribute;
    private List<Long> groupIds = new ArrayList<>();
    private String prefix;

    JSANConverterContext() {
    }

    JSANConverterContext(JSANConverterContext context) {
        this.parentAttribute = context.parentAttribute;
        this.groupIds.addAll(context.groupIds);
        this.prefix = context.prefix;
    }

    public JSANGroupAttribute getParentAttribute() {
        return parentAttribute;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    public String createName(String name) {
        if(parentAttribute==null)  {
            return name;
        }
        if(parentAttribute.isArray()) {
            return prefix + name;
        }
        return prefix + "." + name;
    }

    final JSANConverterContext setParentAttribute(JSANGroupAttribute parentAttribute) {
        JSANConverterContext context = copy();
        context.parentAttribute = parentAttribute;
        return context;
    }

    final JSANConverterContext setPrefix(String prefix) {
        JSANConverterContext context = copy();
        context.prefix = prefix;
        return context;
    }

    final JSANConverterContext addGroupId(long id) {
        JSANConverterContext context = copy();
        context.groupIds.add(id);
        return context;
    }

    private JSANConverterContext copy() {
        return new JSANConverterContext(this);
    }


}
