package org.tect.platform.jsan;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JSANMapperTest {
    private JSANMapper converter;

    @Before
    public void setUp() throws Exception {
        this.converter = new JSANMapper(new DefaultJSANKeyValueFactory());
    }

    @Test
    public void convertPrimitive() {
        //given
        JSANNode node = JSANNode.fromJSON(JSANTestUtils.PRIMITIVES_JSON);

        //when
        List<JSANKeyValue> keyValues = converter.toKeyValues(node);

        //then
        assertEquals(3 ,keyValues.size());
        assertKeyValue(keyValues, 0, "name", "Jack");
        assertKeyValue(keyValues, 1, "age", 30);
        assertKeyValue(keyValues, 2, "married", false);
    }

    @Test
    public void convertRootedList() {
        //given
        JSANNode node = JSANNode.fromJSON("{\n" +
                "    \"names\" : [\"Jack\", \"Summers\"],\n" +
                "    \"cars\" : []\n" +
                "}");

        //when
        List<JSANKeyValue> keyValues = converter.toKeyValues(node);

        //then
        assertEquals(2 ,keyValues.size());
        assertKeyValue(keyValues, 0, "names[1]", "Jack", 1L);
        assertKeyValue(keyValues, 1, "names[1]", "Summers", 1L);
    }

    @Test
    public void objects() {
        //given
        JSANNode node = JSANNode.fromJSON("{\n" +
                "    \"names\" : {\n" +
                "        \"first\" : \"Jack\",\n" +
                "        \"last\" : \"Summers\"\n" +
                "    },\n" +
                "    \"cars\" : {}\n" +
                "}");

        //when
        List<JSANKeyValue> keyValues = converter.toKeyValues(node);

        //then
        assertEquals(2 ,keyValues.size());
        assertKeyValue(keyValues, 0, "names.first", "Jack", 1L);
        assertKeyValue(keyValues, 1, "names.last", "Summers", 1L);
    }

    @Test
    public void objectsNested() {
        //given
        JSANNode node = JSANNode.fromJSON("{\n" +
                "    \"invites\"  :  [\n" +
                "            {\n" +
                "                \"first\"  :  \"Jack\",\n" +
                "                \"last\" : \"Summers\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"first\"  :  \"Lara\",\n" +
                "                \"last\" : \"Queens\"\n" +
                "            }\n" +
                "        ]\n" +
                "}");

        //when
        List<JSANKeyValue> keyValues = converter.toKeyValues(node);

        //then
        assertEquals(4 ,keyValues.size());
        assertKeyValue(keyValues, 0, "invites[1]{2}.first", "Jack", 1L, 2L);
        assertKeyValue(keyValues, 1, "invites[1]{2}.last", "Summers", 1L, 2L);
        assertKeyValue(keyValues, 2, "invites[1]{3}.first", "Lara", 1L, 3L);
        assertKeyValue(keyValues, 3, "invites[1]{3}.last", "Queens", 1L, 3L);

    }

    @Test
    public void arraysInObjects() {

        //given
        JSANNode node = JSANNode.fromJSON("{\n" +
                "    \"employee\"  :  {\n" +
                "        \"names\"  :  [\"Jack\", \"Summers\"]\n" +
                "    }\n" +
                "}");

        //when
        List<JSANKeyValue> keyValues = converter.toKeyValues(node);

        //then
        assertEquals(2 ,keyValues.size());
        assertKeyValue(keyValues, 0, "employee.names[2]", "Jack", 1L, 2L);
        assertKeyValue(keyValues, 1, "employee.names[2]", "Summers", 1L, 2L);

    }

    @Test
    public void convertNestedList() {
        //given
        JSANNode node = JSANNode.fromJSON("{\"names\":[\"Jack\", [\"Summers\"]]}");

        //when
        List<JSANKeyValue> keyValues = converter.toKeyValues(node);

        //then
        assertEquals(2 ,keyValues.size());
        assertKeyValue(keyValues, 0, "names[1]", "Jack", 1L);
        assertKeyValue(keyValues, 1, "names[1][2]", "Summers", 1L, 2L);
    }

    @Test
    public void everything() {

        //given
        JSANNode node = JSANNode.fromJSON("{\n" +
                "    \"name1\"  :  {\n" +
                "        \"name2\"  :  [ \"A\", {\"name3\"  :  \"B\"}, {\"name3\"  :  \"C\"}]\n" +
                "    },\n" +
                "    \"name4\"  :  [ [\"D\", [\"E\", \"F\"]],  \"G\", [\"H\"] ]\n" +
                "}");


        //when
        List<JSANKeyValue> keyValues = converter.toKeyValues(node);

        //then
        assertEquals(8 ,keyValues.size());
        assertKeyValue(keyValues, 0, "name1.name2[3]", "A", 1L, 3L);
        assertKeyValue(keyValues, 1, "name1.name2[3]{4}.name3", "B", 1L, 3L, 4L);
        assertKeyValue(keyValues, 2, "name1.name2[3]{5}.name3", "C", 1L, 3L, 5L);
        assertKeyValue(keyValues, 3, "name4[2][6]", "D", 2L, 6L);
        assertKeyValue(keyValues, 4, "name4[2][6][8]", "E", 2L, 6L, 8L);
        assertKeyValue(keyValues, 5, "name4[2][6][8]", "F", 2L, 6L, 8L);
        assertKeyValue(keyValues, 6, "name4[2]", "G", 2L);
        assertKeyValue(keyValues, 7, "name4[2][7]", "H", 2L, 7L);
    }



    @Test
    public void everythingNoIds() {
        //given
        JSANNode node = JSANNode.fromJSON("{\n" +
                "    \"name1\"  :  {\n" +
                "        \"name2\"  :  [ \"A\", {\"name3\"  :  \"B\"}, {\"name3\"  :  \"C\"}]\n" +
                "    },\n" +
                "    \"name4\"  :  [ [\"D\", [\"E\", \"F\"]],  \"G\", [\"H\"] ]\n" +
                "}");


        //when
        converter.hideGroupIds();
        List<JSANKeyValue> keyValues = converter.toKeyValues(node);

        //then
        assertEquals(8 ,keyValues.size());
        assertKeyValue(keyValues, 0, "name1.name2[]", "A", 1L, 3L);
        assertKeyValue(keyValues, 1, "name1.name2[]{}.name3", "B", 1L, 3L, 4L);
        assertKeyValue(keyValues, 2, "name1.name2[]{}.name3", "C", 1L, 3L, 5L);
        assertKeyValue(keyValues, 3, "name4[][]", "D", 2L, 6L);
        assertKeyValue(keyValues, 4, "name4[][][]", "E", 2L, 6L, 8L);
        assertKeyValue(keyValues, 5, "name4[][][]", "F", 2L, 6L, 8L);
        assertKeyValue(keyValues, 6, "name4[]", "G", 2L);
        assertKeyValue(keyValues, 7, "name4[][]", "H", 2L, 7L);
    }

    @Test
    public void reservedCharacters() {
        //given
        JSANNode node = JSANNode.fromJSON(JSANTestUtils.SPECIAL_CHARS_JSON);

        //when
        List<JSANKeyValue> keyValues = converter.toKeyValues(node);

        //then
        assertEquals(1 ,keyValues.size());
        assertKeyValue(keyValues, 0, "\\[\\]\\{\\}\\..name", "Jack", 1L);
    }


    private void assertKeyValue(List<JSANKeyValue> keyValues, int index, String expectedName, Object expectedValue, Long ... groupIds) {
        JSANKeyValue keyValue = keyValues.get(index);
        assertEquals(expectedName, keyValue.getName());
        assertEquals(expectedValue, keyValue.getValue());

        List<Long> actualGroups = keyValue.getGroupIds();
        int length = groupIds.length;

        assertEquals("Group ID List size assert ..", length, actualGroups.size());
        for(int i=0;i<length;i++) {
            assertEquals(groupIds[i], actualGroups.get(i));
        }
    }
}