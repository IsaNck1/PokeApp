package com.isabellnoack.myapp;

import android.graphics.Bitmap;

import com.isabellnoack.myapp.api.PokeAPI;
import com.isabellnoack.myapp.api.Pokemon;

import java.io.IOException;

public class RecyclerViewItem {

    public String name;
    public Integer id;
    public Bitmap image;


    //Konstruktor um Pokemon Werte an Instanz von RecyclerViewItem zu geben
    public RecyclerViewItem(int id) throws IOException {
        Pokemon pokemon = new PokeAPI().requestPokemon(id);

        this.name = pokemon.name;
        this.name = Character.toUpperCase(name.charAt(0)) + name.substring(1); //Erster Character groß geschrieben

        this.id = id;
        this.image = PokeAPI.ImageLoader.loadImageFromUrl(pokemon.imageUrl);

    }
}
