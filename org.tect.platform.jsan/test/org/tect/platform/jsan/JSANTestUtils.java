package org.tect.platform.jsan;

public class JSANTestUtils {
    static final String PRIMITIVES_JSON = "{\n" +
            "    \"name\" : \"Jack\",\n" +
            "    \"age\" :  30,\n" +
            "    \"married\" : false\n" +
            "}";

    static final String SPECIAL_CHARS_JSON = "{\n" +
            "    \"[]{}.\" : {\"name\" : \"Jack\"} "+
            "}";

    static final String MULTI_PROPS_JSON = "{\n" +
            "    \"names\" : {\"first\" : \"Jack\", \"last\" : \"Summers\"} "+
            "}";

    static final String LIST_JSON = "{\n" +
            "    \"names\" : [ \"Jack\", \"Summers\"] "+
            "}";


}
