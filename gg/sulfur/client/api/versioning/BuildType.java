package gg.sulfur.client.api.versioning;

public enum BuildType {

    NORMAL("Release"), BETA("Beta"), DEV("Developer");

    private final String name;

    public String getName() {
        return name;
    }

    BuildType(String name) {
        this.name = name;
    }
}
