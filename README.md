# patchable
The Optional type for PATCH requests.

A `Patchable` is a opinionated class that mimics the Java 8 `Optional` with an extra field - `isSet`. That way we can know in 
a PATCH request if the client is actually sending a `null` or the value should simply be omitted.

## Example
Having the following JSON:
```json
{"name": "Pesho"}
```
...and the following DTO:
```java
public class UserPatch {
    public Patchable<String> name;
    public Patchable<Integer> age;
}
```
...we can check with `isSet` if the value was present in the JSON:
```java
UserPatch user = // ...deserialize
user.name.isSet() // true
user.age.isSet() // false

User userEntity = // ...read from DB
user.name.ifSet(userEntity::setName)
user.age.ifSet(userEntity::setAge)
```

## Features & Behavior
`Patchable` currently only has Jackson support.
### Serialization
The `Patchable` field is added to the result JSON only if `isSet=true`.

### Deserialization
A `Patchable` property will never be `null`, even if missing from the JSON string - in that 
case it's a `Patchable.notSet()` instance. It will never be null.

### Validation
Supports Hibernate Validator. Validation only triggers if the Patchable has a set value.
```java
public class UserToValidate {

    private Patchable<@NotNull @Email String> email = Patchable.of("not_email");
}
```
