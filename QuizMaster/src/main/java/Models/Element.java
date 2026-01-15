package Models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // use "type" field to identify subclass
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Radiogroup.class, name = "radiogroup"),
        @JsonSubTypes.Type(value = Boolean.class, name = "boolean")
})

//https://blogs.oracle.com/javamagazine/post/java-json-serialization-jackson

public abstract class Element {
    public String name;
    public Type type;
    public String title;
    public boolean isRequired;

    public Element() {}
    public Element(String name, Type type, String title, boolean isRequired) {
        this.name = name;
        this.type = type;
        this.title = title;
        this.isRequired = isRequired;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }
}
