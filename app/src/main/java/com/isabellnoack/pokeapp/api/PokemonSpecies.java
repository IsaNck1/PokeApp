package com.isabellnoack.pokeapp.api;

import java.util.ArrayList;

public class PokemonSpecies {

    public String name = "Unknown";
    public ArrayList<FlavorTextEntry> flavorTextEntries = new ArrayList<>();
    public ArrayList<Variety> varieties = new ArrayList<>();

    @Override
    public String toString() {
        return "PokemonSpecies{" +
                "name='" + name + '\'' +
                ", flavorTextEntries=" + flavorTextEntries +
                '}';
    }
}
