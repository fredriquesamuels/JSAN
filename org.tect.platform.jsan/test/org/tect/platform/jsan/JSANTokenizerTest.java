package org.tect.platform.jsan;

import org.junit.Before;
import org.junit.Test;
import org.tect.platform.jsan.token.JSANToken;
import org.tect.platform.jsan.token.JSANTokenizer;
import org.tect.platform.jsan.token.JSANTokens;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSANTokenizerTest {

    private JSANTokenizer tokenizer;

    @Before
    public void setUp() throws Exception {
        this.tokenizer = new JSANTokenizer();
    }

    @Test
    public void testListName() {
        assertTokens("name[]", asList(3L), resultList("name[]", 3L, "name"));
    }

    @Test
    public void testListNameDecode() {
        assertTokens("\\.\\[\\]\\{\\}\\[[]", asList(3L), resultList("\\.\\[\\]\\{\\}\\[[]", 3L, ".[]{}["));
    }

    @Test
    public void testListWithIdName() {
        assertTokens("name[3]", asList(3L), resultList("name[3]", 3L, "name"));
    }

    @Test
    public void testObjectName() {
        assertTokens("empl.first", asList(3L, 4L), resultObject("empl.", 3L, "empl"), result("first"));
    }

    @Test
    public void testObjectWithIdName() {
        assertTokens("list[]{33}.first", asList(2L, 3L, 4L), resultList("list[]", 2L, null), resultObject("{33}.", 33L, ""), result("first"));
    }

    @Test
    public void testNameTokenize() {
        assertTokens("name", Collections.emptyList(), resultValue("name", "name"));
        assertTokens("name[]", asList(3L), resultList("name[]", 3L, "name"));
        assertTokens("name[2]", asList(3L), resultList("name[2]", 2L, "name"));
        assertTokens("name[][6]", asList(3L, 5L), resultList("name[]", 3L, null), resultList("[6]", 6L, null));
        assertTokens("name.first",asList(2L), resultObject("name.", 2L, null),result("first"));
        assertTokens("name{33}.first",asList(2L), resultObject("name{33}.", 33L, null),result("first"));

        assertTokens("name[]{}.first", asList(2L, 3L),
                resultList("name[]", 2L, null),
                resultObject("{}.", 3L, null),
                result("first"));

        assertTokens("name[][]{}.first", asList(2L, 3L, 5L),
                resultList("name[]", 2L, null),
                resultList("[]", 3L, null),
                resultObject("{}.", 5L, null),
                result("first"));

        assertTokens("name[3][4]{55}.first",
                Collections.emptyList(),
                resultList("name[3]", 3L, null),
                resultList("[4]", 4L, null),
                resultObject("{55}.", 55L, null),
                result("first"));
    }

    private List<Long> asList(Long ... i) {
        return Stream.of(i).collect(Collectors.toList());
    }

    private void assertTokens(String name, List<Long> groupIds, TestToken...tokens) {
        JSANTokens jsanTokens = tokenizer.tokenize(name, groupIds);

        int size = jsanTokens.size();
        assertEquals("Expected token size does not match.", tokens.length, size);

        for(int i=0;i<size;i++) {
            TestToken expected = tokens[i];
            JSANToken actual = jsanTokens.get(i);
            String raw = actual.getRaw();

            assertEquals(String.format("Token[%d] mismatch", i), expected.token, raw);
            if(expected.id!=null) {
                assertEquals(String.format("Token[%d] group id mismatch",i), expected.id.longValue(), actual.getGroupId().longValue());
            }

            if(expected.groupType==1) {
                assertTrue(String.format("Token[%d] '%s' should be a list item", i, raw), actual.isList());
            } else if (expected.groupType==2) {
                assertTrue(String.format("Token[%d] '%s' should be a object",i, raw), actual.isObject());
            }

            if(expected.name!=null) {
                assertEquals(String.format("Token[%d] '%s' name mismatch",i, raw), expected.name, actual.getName());
            }
        }
    }

    private TestToken result(String raw) {
        return result(raw, null, null);
    }

    private TestToken resultValue(String raw, String name) {
        return result(raw, null, name);
    }

    private TestToken resultList(String raw, long id, String name) {
        return new TestToken(raw, id, 1, name);
    }

    private TestToken resultObject(String raw, long id, String name) {
        return new TestToken(raw, id, 2, name);
    }

    private TestToken result(String raw, Long id, String name) {
        return new TestToken(raw, id, -1, name);
    }

    private class TestToken {
        String token;
        private Long id;
        private int groupType;
        private String name;

        public TestToken(String token, Long id, int groupType, String name) {
            this.token = token;
            this.id = id;
            this.groupType = groupType;
            this.name = name;
        }


    }
}
