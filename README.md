# JAVASCRIPT ATTRIBUTE NOTATION (JSAN)

This article documents original work.

## Purpose

**JSAN** aims to standardize the decomposition of a JSON object into the values that are contained in the object.
The purpose of this structure is to simplify the inspection of the JSON values.


## Terms

TERM | DESCRIPTION
------------ | -------------
Node | Refers to the entire JSON object being converted.
Object | The json structure denoted by **{}** syntax
Array | The json structure denoted by **[]** syntax
Group | A synonym for the **Object**s and **Array**s types.
Attribute | A key value pair contained in an **Object**s or single value as in the case of **List**s
Primitive | A value of types **string**, **number**, **decimal**, **true** and **false**. See [JSON](https://www.json.org/).
Value | An entry in either a **Array** or **Object** that is a **Primitive**.


## Symbols

SYMBOLS | DESCRIPTION
------------ | -------------
[] | Denotes an **Array** **Attribute**
\[2\] | Denotes an **Array** **Attribute** with an **id** of 2
{} | Denotes an **Object** **Attribute**
{1} | Denotes an **Object** **Attribute** with an **id** of 1
. | Denotes separation of an **Object** and a child **Attribute**


## Reserved Characters

The following characters are reserved for the notation and should be encoded
using the following mapping.

CHARACTER | Escaped
------------ | -------------
{ | \\{
} | \\}
\[ | \\[
\] | \\]
\ | \\\\
. | \\.
" | \\"



## Parsing a JSAN Key

### NOTATIONS

---

#### Node Level Attributes

###### Primitives

JSON
```json
{
    "name" : "Jack",
    "age" :  30,
    "married" : false
}
```

**JSAN**
```
"name" : "Jack"
"age" : 30
"married" : false
```

###### Arrays

**JSON**
```json
{
    "names" : ["Jack", "Summers"],
    "cars" : []
}
```

**JSAN**
```
"names[]" : "Jack"
"names[]" : "Summers"
"cars[]" : null
```


###### Objects

**JSON**
```json
{
    "names" : {
        "first" : "Jack",
        "last" : "Summers"
    },
    "cars" : {}
}
```

**JSAN**
```
"names.first" : "Jack"
"names.last" : "Summers"
"cars{}" : null
```

The following is also accepted **JSAN** syntax :

**JSAN**
```
"names{}.first" : "Jack"
"names{}.last" : "Summers"
"cars{}" : null
```

---

#### What is a Group ID?

A group **ID** is an identifier, usually an integer, assigned to a **Group** that
is unique within the scope of the **Node** it resides in.
While not needed for simple structures as shown above, all groups should
ideally have an **ID**. This **ID** is used to help convert complex **Groups** back into **JSON**.

#### Group ID Notation


##### Array with Group ID

**JSON**
```json
{
    "names" : ["Jack", "Summers"],
    "cars" : []
}
```

**JSAN**
```
"names[1]" : "Jack"
"names[1]" : "Summers"
"cars[2]" : null
```

##### Object with Group ID

**JSON**
```json
{
    "names" : {
        "first"  :  "Jack",
        "last" : "Summers"
    },
    "cars" : {}
}
```

**JSAN**
```
"names{1}.first" : "Jack"
"names{1}.last" : "Summers"
"cars{2}" : null
```

---

##### Nested Groups

###### Arrays of Arrays

**JSON**
```json
{
    "invites"  :  [
            ["Jack", "Summers"]
        ]
}
```

**JSAN**
```
"invites[][]" : "Jack"
"invites[][]" : "Summers"
```

In the above example the **ID** for the nested **Array** was omitted as there is only one,
however *invites[][1]* is also accepted.


---

**JSON**
```json
{
    "invites"  :  [
            ["Jack", "Summers"],
            ["Lara", "Queens"]
        ]
}
```

**JSAN**
```
"invites[][1]" : "Jack"
"invites[][1]" : "Summers"
"invites[][2]" : "Lara"
"invites[][2]" : "Queens"
```

---

###### Array of Objects

**JSON**
```json
{
    "invites"  :  [
            {
                "first"  :  "Jack",
                "last" : "Summers"
            }
        ]
}
```

**JSAN**
```
"invites[]{}.first" : "Jack"
"invites[]{}.last" : "Summers"
```

In the above example the **ID** for the nested **Object** was omitted as there is only one,
however, *invites[]{1}* is also accepted.

---

**JSON**
```json
{
    "invites"  :  [
            {
                "first"  :  "Jack",
                "last" : "Summers"
            },
            {
                "first"  :  "Lara",
                "last" : "Queens"
            }
        ]
}
```

**JSAN**
```
"invites[]{1}.first" : "Jack"
"invites[]{1}.last" : "Summers"
"invites[]{2}.first" : "Lara"
"invites[]{2}.last" : "Queens"
```


Note that I did not assign an **ID** to *invites[]* as it is a named value.
When converting back to JSON there will be no ambiguity as to which list we are referring to,
however, *invites[]{3}* is also accepted.


###### Array in Objects

**JSON**
```json
{
    "employee"  :  {
        "names"  :  ["Jack", "Summers"]
    }
}
```

**JSAN**
```
"employee.names[]" : "Jack"
"employee.names[]" : "Summers"
```

---

###### Everything!

**JSON**
```json
{
    "name1"  :  {
        "name2"  :  [ "A", {"name3"  :  "B"}, {"name3"  :  "C"}]
    },
    "name4"  :  [ ["D", ["E", "F"]],  "G", ["H"] ]
}
```

**JSAN**
```
"name1.name2[]" : "A"
"name1.name2[]{1}.name3"  :  "B"
"name1.name2[]{2}.name3" : "C"
"name4[][3]" : "D"
"name4[][3][4]" : "E"
"name4[][3][4]" : "F"
"name4[]" : "G"
"name4[][5]" : "H"
```