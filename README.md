# patchable
The Optional type for PATCH requests.

A `Patchable` is a opinionated class that mimics the Java 8 `Optional` with an extra field - `isSet`. That way we can know in 
a PATCH request if the client is actually sending a `null` or the value should simply be omitted.

## Example
Given the following request bean:
```java
public class UserPatch {
    private Patchable<String> name = Patchtable.notSet();
    private Patchable<Integer> age = Patchtable.notSet();
    
    public Patchable<String> getName() {
        return this.name;
    }
    
    public Patchable<Integer> getAge() {
        return this.age;
    }
}
```
we can check with `isSet` if the value was present in the JSON:
```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new PatchableModule());
UserPatch userPatch = objectMapper.readValue("{\"name\": \"Juan\"}", UserPatch.class);

userPatch.getName().isSet(); // true
userPatch.getAge().isSet();  // false
```

Furthermore, we can use the `ifSet` utility method:
```
// Read entity from database
User userEntity = repository.getUser();

// Apply changes only to the actually set fields from the PATCH request
userPatch.getName().ifSet(userEntity::setName)
userPatch.getAge().ifSet(userEntity::setAge)
```

## Features & Behavior
`Patchable` currently has Jackson support. To enable the serialization / deserialization behavior the `PatchableModule()`
must be registered to the Jackson `ObjectMapper`.
```java
ObjectMapper appMapepr = new ObjectMapper();
mapper.registerModule(new PatchableModule());
```
### Serialization
When serializing to JSON, the `Patchable`'s value is added to the result JSON only if it actually has
a set value.
```java
UserPatch patch = new UserPatch();
// patch.setName("..."); commented out
patch.setAge(10);

appMapper.writeValueAsString(patch); // {"age": 10}
```

### Deserialization
The bean we deserialize into should initialize all `Patchable<T>` fields as `Patchable.notSet()`. 
The value will be overriden by the deserializer only if the field value is present in the JSON.

### Validation
Supports Hibernate Validator. Validation only triggers if the Patchable has a set value. Example:
```java
public class UserPatch {

    private Patchable<@NotNull @Email String> email = Patchable.of("not_email");
}
```
