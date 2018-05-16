# JAVASCRIPT ATTRIBUTE NOTATION (JSAN)


## Description

**JSAN** aims to standardize the decomposition of a JSON object into the values that are contained in the object.
The purpose of this structure is to simplify the inspection of the JSON values.

You can read the specification docs [here](https://github.com/fredriquesamuels/JSAN/wiki)

## API

### Test JSON

```java
String TEST_JSON = "{\"names\": { \"first\":\"jack\", \"last\":\"black\" }, \"age\":5 }"
```

### Convert JSON to JSAN

**Java**
```java
//convert to key and value pairs
List<JSANKeyValue> keyValues = JSANMapper.toKeyValues(TEST_JSON);

//print values
for (JSANKeyValue kv : keyValues) {
    System.out.println(kv.getName() + " " + kv.getValue());
}
```
**console**
```
names.first jack
names.last black
age 5
```

### Convert JSAN to JSON

**Java**
```java
String json = JSANParser.parseToJson(keyValues);
System.out.println(json);
```

**console**
```
{"names":{"first":"jack","last":"black"},"age":5}
```

