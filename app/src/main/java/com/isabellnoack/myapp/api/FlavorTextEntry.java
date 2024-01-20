package com.isabellnoack.myapp.api;

public class FlavorTextEntry {

    public String flavorText;
    public NameWithURL language;
    public NameWithURL version;

    @Override
    public String toString() {
        return "FlavorTextEntry{" +
                "flavorText='" + flavorText + '\'' +
                ", language=" + language +
                ", version=" + version +
                '}';
    }
}
