package ir.codefather.assistance.command.core;

public class CommandFlag {
    private String name;
    private String description;
    private String value;
    private boolean withValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isWithValue() {
        return withValue;
    }

    public void setWithValue(boolean withValue) {
        this.withValue = withValue;
    }
}
