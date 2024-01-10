package com.isabellnoack.myapp.api;

import java.util.ArrayList;

public class Pokemon {
    public Pokemon() { //Konstruktor, Funktion wird aufgerufen bei Erstellung von neuer Instanz von Pokemon Klasse (Zum mitgeben von Werten etc.)
    }
    public String name = "Unknown";
    public int baseExperience = 0;
    public String imageUrl = "";


    public int height = 0;
    public int weight = 0;
    public ArrayList<NameWithURL> types = new ArrayList<>();


   //wizard magic mit: funktion löschen, "tostring" hier drunter eingeben, und enter (generate via wizard)

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
