package org.tect.platform.jsan;

import mockit.Mocked;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JSANParserTest {

    private JSANParser<JSANKeyValue> parser;
    private JSANMapper jsanMapper;

    @Mocked
    ParsedJSANNode parsedJSANNode;

    @Before
    public void setUp() throws Exception {
        jsanMapper = new JSANMapper();
        parser = new JSANParser();
    }

    @Test
    public void parser() {
        //given
        JSANNode node = JSANNode.fromJSON(JSANTestUtils.PRIMITIVES_JSON);
        List<JSANKeyValue> keyValues = jsanMapper.toKeyValues(node);

        //when
        MappedParsedJSANNode parsedJSANNode = parser.parse(keyValues, MappedParsedJSANNode.class);

        //then
        assertNotNull(parsedJSANNode);

        Map<String, Object> map = parsedJSANNode.getMap();
        assertEquals(3, map.size());
        assertEquals("Jack", map.get("name"));
        assertEquals(Integer.valueOf(30), map.get("age"));
        assertEquals(Boolean.FALSE, map.get("married"));

    }

    @Test
    public void parserList() {
        //given
        JSANNode node = JSANNode.fromJSON(JSANTestUtils.LIST_JSON);
        List<JSANKeyValue> keyValues = jsanMapper.toKeyValues(node);

        //when
        MappedParsedJSANNode parsedJSANNode = parser.parse(keyValues, MappedParsedJSANNode.class);

        //then
        assertNotNull(parsedJSANNode);

        Map<String, Object> map = parsedJSANNode.getMap();
        assertEquals(1, map.size());
        assertEquals("Jack", ((List)map.get("names")).get(0));
        assertEquals("Summers", ((List)map.get("names")).get(1));

    }

    @Test
    public void parserSpecialChars() {
        //given
        JSANNode node = JSANNode.fromJSON(JSANTestUtils.SPECIAL_CHARS_JSON);
        List<JSANKeyValue> keyValues = jsanMapper.toKeyValues(node);

        //when
        MappedParsedJSANNode parsedJSANNode = parser.parse(keyValues, MappedParsedJSANNode.class);

        //then
        assertNotNull(parsedJSANNode);

        Map<String, Object> map = parsedJSANNode.getMap();
        assertEquals(1, map.size());
        assertEquals("Jack", ((Map)map.get("[]{}.")).get("name"));
    }

    @Test
    public void multiProps() {
        //given
        JSANNode node = JSANNode.fromJSON(JSANTestUtils.MULTI_PROPS_JSON);
        List<JSANKeyValue> keyValues = jsanMapper.toKeyValues(node);

        //when
        MappedParsedJSANNode parsedJSANNode = parser.parse(keyValues, MappedParsedJSANNode.class);

        //then
        assertNotNull(parsedJSANNode);

        Map<String, Object> map = parsedJSANNode.getMap();
        assertEquals("Jack", ((Map)map.get("names")).get("first"));
        assertEquals("Summers", ((Map)map.get("names")).get("last"));
    }
}
