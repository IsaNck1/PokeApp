package com.isabellnoack.myapp.api;

public class Pokemon {
    public Pokemon() { //Konstruktor, Funktion wird aufgerufen bei Erstellung von neuer Instanz von Pokemon Klasse (Zum mitgeben von Werten etc.)
    }

    public String name = "Unknown";
    public int baseExperience = 0;

   //magic mit: tostring, drei runter und enter (generate via wizard)
    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", baseExperience=" + baseExperience +
                '}';
    }
}
