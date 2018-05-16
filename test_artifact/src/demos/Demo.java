package demos;

import org.tect.platform.jsan.JSANKeyValue;
import org.tect.platform.jsan.JSANMapper;
import org.tect.platform.jsan.JSANParser;

import java.util.List;

public class Demo {

    public static final String TEST_JSON = "{\"names\": { \"first\":\"jack\", \"last\":\"black\" }, \"age\":5 }";

    public static void main(String[] args) {
        //convert to key and value pairs
        List<JSANKeyValue> keyValues = JSANMapper.toKeyValues(TEST_JSON);

        //print values
        System.out.println("====JSAN KEY VALUES====");
        for (JSANKeyValue kv : keyValues) {
            System.out.println(kv.getName() + " " + kv.getValue());
        }

        // convert values back to a json string
        System.out.println("====JSAN TO JSON====");
        String json = JSANParser.parseToJson(keyValues);
        System.out.println(json);
    }
}
