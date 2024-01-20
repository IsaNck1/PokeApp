package com.isabellnoack.pokeapp.api;

public class Variety {

    public NameWithURL pokemon;
    public boolean isDefault;

    @Override
    public String toString() {
        return "Variety{" +
                "pokemon=" + pokemon +
                ", isDefault=" + isDefault +
                '}';
    }
}
