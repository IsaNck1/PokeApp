package com.isabellnoack.myapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder Klasse
 */
public class ListItemHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textViewID;
    TextView textViewName;
    LinearLayout layoutChanged;
    CardView cardView;

    public ListItemHolder(@NonNull View itemView) {
        super(itemView); // sorgt dafür, dass die ViewHolder-Instanz ordnungsgemäß initialisiert wird, indem der Konstruktor der Elternklasse aufgerufen wird

        imageView = itemView.findViewById(R.id.list_pokemon_image); //Layout finden
        textViewName = itemView.findViewById(R.id.list_pokemon_name);
        textViewID = itemView.findViewById(R.id.list_pokemon_id);
        layoutChanged = itemView.findViewById(R.id.layoutchanged); // für Layout Orientation horizontal und vertikal
        cardView = itemView.findViewById(R.id.cardView); //Für den Onclick Listener

    }

    // Methode openPokemonFragment für ListFragment zu PokemonFragment
    public void openPokemonFragment() {
        // Navigation zum PokemonFragment mit der übergebenen Pokemon-ID
        NavController navController = Navigation.findNavController(itemView);
        navController.navigate(R.id.action_listFragment_to_pokemonFragment);
    }
}