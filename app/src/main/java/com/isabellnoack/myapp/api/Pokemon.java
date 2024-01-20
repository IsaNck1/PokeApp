package com.isabellnoack.myapp.api;

import java.util.ArrayList;

public class Pokemon {

    public String name = "Unknown";
    public int baseExperience = 0;
    public String imageUrl = "";
    public int height = 0;
    public int weight = 0;
    public ArrayList<NameWithURL> types = new ArrayList<>();
    public ArrayList<Ability> abilities = new ArrayList<>();

    // toString() Funktion für "print-debugging"
    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", baseExperience=" + baseExperience +
                ", imageUrl='" + imageUrl + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", types=" + types +
                '}';
    }
}
