package org.tect.platform.jsan.token;

import java.util.List;

public class JSANTokenizer {

    public JSANTokens tokenize(String name, List<Long> groupIds) {
        return new JSANTokensImpl().tokenize(name, groupIds);
    }

}
