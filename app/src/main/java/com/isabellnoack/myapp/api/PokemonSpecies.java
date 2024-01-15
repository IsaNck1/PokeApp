package com.isabellnoack.myapp.api;

import java.util.ArrayList;

public class PokemonSpecies {
    public PokemonSpecies() {
    }

    public String name = "Unknown";

    public ArrayList<FlavorTextEntry> flavorTextEntries = new ArrayList<>();

    @Override
    public String toString() {
        return "PokemonSpecies{" +
                "name='" + name + '\'' +
                ", flavorTextEntries=" + flavorTextEntries +
                '}';
    }
}
