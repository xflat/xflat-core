package cn.xflat.core.spring;

public enum ConfigType {
    XML("xml"),
    JAVA_CONFIG("class");

    String value;

    ConfigType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}