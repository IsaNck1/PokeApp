package com.isabellnoack.myapp.api;

public class Pokemon {
    public String name;
    public int baseExperience;

   //magic mit: tostring, drei runter und enter (generate via wizard)
    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", baseExperience=" + baseExperience +
                '}';
    }
}
