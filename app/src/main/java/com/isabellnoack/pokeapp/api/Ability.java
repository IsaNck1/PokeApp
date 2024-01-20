package com.isabellnoack.pokeapp.api;

public class Ability {

    public NameWithURL nameWithURL;
    public boolean isHidden;

    @Override
    public String toString() {
        return "Ability{" +
                "nameWithURL=" + nameWithURL +
                ", isHidden=" + isHidden +
                '}';
    }
}
