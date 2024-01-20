package com.isabellnoack.myapp;

import static com.isabellnoack.myapp.DetailFragment.capitalize;

import android.graphics.Bitmap;

import com.isabellnoack.myapp.api.ImageLoader;
import com.isabellnoack.myapp.api.PokeAPI;
import com.isabellnoack.myapp.api.Pokemon;

import java.io.IOException;

public class ViewItem {

    public String name;
    public Bitmap image;
    public Pokemon pokemon;

    //Konstruktor um Pokemon Werte an Instanz von RecyclerViewItem zu geben
    public ViewItem(int id) throws IOException {
        this.pokemon = new PokeAPI().requestPokemon(id);
        this.name = pokemon.name;
        this.name = capitalize(name);
        this.image = ImageLoader.loadImageFromUrl(pokemon.imageUrl);
    }
}
