package ir.codefather.assistance.command.core;

import java.util.List;

public class CommandParameter {
    private String name;
    private String description;
    private String value;
    private boolean withValue;
    private List<CommandFlag> flags;


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

    public List<CommandFlag> getFlags() {
        return flags;
    }

    public void setFlags(List<CommandFlag> flags) {
        this.flags = flags;
    }

    /**
     * check that flag is exists or not
     *
     * @param name name of flag
     * @return boolean
     */
    public boolean hasFlag(String name) {
        return flags.stream()
                .anyMatch((flag) -> flag.getName().equals(name));
    }


    /**
     * get flag with its name if not exist return null
     *
     * @param name name of flag
     * @return CommandFlag
     */
    public CommandFlag getFlag(String name) {
        return flags.stream()
                .filter((flag) -> flag.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
