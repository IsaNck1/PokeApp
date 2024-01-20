package com.isabellnoack.pokeapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ListItem_ViewHolder extends RecyclerView.ViewHolder {

    /**
     * Konstruktor
     * damit Bindings vereinfach werden - statt immer itemView.findViewById(R.id.list_pokemon_image) jetzt imageView
     * (Unterklasse von ViewHolder)
     */
    public ListItem_ViewHolder(@NonNull View itemView) {
        super(itemView); // sorgt dafür, dass die ViewHolder-Instanz ordnungsgemäß initialisiert wird, indem der Konstruktor der Elternklasse aufgerufen wird

        //Variablen Wert zuweisen (Initialisierung)
        imageView = itemView.findViewById(R.id.list_pokemon_image);
        textViewName = itemView.findViewById(R.id.list_pokemon_name);
        textViewID = itemView.findViewById(R.id.list_pokemon_id);
        layoutChanged = itemView.findViewById(R.id.layoutchanged); // für Layout Orientation horizontal und vertikal
        cardView = itemView.findViewById(R.id.cardView); //Für den Onclick Listener
    }

    //Variablen der Klasse
    public ImageView imageView;
    public TextView textViewID;
    public TextView textViewName;
    public LinearLayout layoutChanged;
    public CardView cardView;

}