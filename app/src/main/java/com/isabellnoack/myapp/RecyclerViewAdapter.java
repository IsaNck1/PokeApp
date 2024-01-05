package com.isabellnoack.myapp;

import static com.isabellnoack.myapp.MainActivity.pokemonIdToOpen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isabellnoack.myapp.api.PokeAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
//Adapter als Übersetzer von der RecyclingView zu meinen Daten und zu Layout

    private Context context; //
    private int numberOfColumns; // NEU: Variable für die Anzahl der Spalten hinzufügen

    private Activity activity;

    //Konstruktor initialisiert die Klassenvariablen mit den übergebenen Werten
    public RecyclerViewAdapter(Context context, int numberOfColumns, Activity activity) {
        this.context = context;
        this.numberOfColumns = numberOfColumns; // NEU: Anzahl der Spalten setzen
        this.activity = activity;
    }

    @NonNull
    @Override

    // Instanz von View gebaut
    // Funktion wird aufgerufen um neuen ViewHolder zu erstellen. Die Funktion erstellt eine Ansicht (View) durch Aufblasen (inflate) des Layouts recyclerview_item
    // ViewHolder (mit View) wird zurück gegeben (anhand der ViewHolder Klasse die unten definiert wurde)
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false); //View anhand des Layouts in meinen Ressourcen
        return new ViewHolder(view);
    }

    // Für den ViewHolder werden an der Position position (An welcher Stelle in der Liste) die Daten abgefragt
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        int id = position + 1;

        Thread t = new Thread(() -> {
            try {
                final RecyclerViewItem item = new RecyclerViewItem(id);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //holder.imageView.setImageURI(Uri.parse(item.pokemon.imageUrl));
                        if (item.image != null) {
                            holder.imageView.setImageBitmap(item.image);
                        } else {
                            holder.imageView.setImageResource(R.drawable.empty);
                        }

                        holder.textViewID.setText("ID: " + item.id.toString()); //Sting mit "ID:" + ID
                        holder.textViewName.setText(item.name);
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        t.start();


        // COLUMNS BUTTON - Anpassung der Anordnung der TextViews basierend auf der Anzahl der Spalten
        if (numberOfColumns == 1) {
            // Wenn es nur eine Spalte gibt, setze die TextViews nebeneinander und das Layout horizontal
            holder.textViewName.getLayoutParams().width = 0; // layout_width auf 0dp setzen
            holder.textViewID.getLayoutParams().width = 0; // layout_width auf 0dp setzen
            holder.layoutChanged.setOrientation(LinearLayout.HORIZONTAL);
            holder.imageView.setAdjustViewBounds(false);

        } else {
            // Wenn es mehr als eine Spalte gibt, setze die TextViews untereinander und das Layout vertikal
            holder.textViewName.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT; // layout_width auf match_parent setzen
            holder.textViewID.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT; // layout_width auf match_parent setzen
            holder.layoutChanged.setOrientation(LinearLayout.VERTICAL);
            holder.imageView.setAdjustViewBounds(true);
        }

        // Von ListFragment zu PokemonFragment
        holder.cardView.setOnClickListener(e -> {
            // Bei einem Klick auf das CardView-Element in der RecyclerView wird dieser Listener aufgerufen.
            // Hier wird die Position des geklickten Elements im RecyclerView-Adapter abgerufen und verwendet,
            // um die entsprechende Pokemon-ID zu bestimmen.

            pokemonIdToOpen = position + 1;
            holder.openPokemonFragment(pokemonIdToOpen); //Methode OpenPokemonFragment vom Holder wird aufgerufen mit dem int pokemonIdToOpen
        });
    }






    // Grösse der Liste
    @Override
    public int getItemCount() {
        return  1025;
        //return recyclerViewItems.size();
    }


    // Definition ViewHolder Klasse
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewID;
        TextView textViewName;
        LinearLayout layoutChanged;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView); //super(itemView) sorgt dafür, dass die ViewHolder-Instanz ordnungsgemäß initialisiert wird, indem der Konstruktor der Elternklasse aufgerufen wird

            imageView = itemView.findViewById(R.id.list_pokemon_image); //Layout finden
            textViewName = itemView.findViewById(R.id.list_pokemon_name);
            textViewID = itemView.findViewById(R.id.list_pokemon_id);
            layoutChanged = itemView.findViewById(R.id.layoutchanged); // für Layout Orientation horizontal und vertikal
            cardView = itemView.findViewById(R.id.cardView); //Für den Onclick Listener

        }

        //Methode openPokemonFragment für ListFragment zu PokemonFragment
        public void openPokemonFragment(int pokemonId) {
            //Hier wird die Navigation zum PokemonFragment mit der übergebenen Pokemon-ID durchgeführt
            NavController navController = Navigation.findNavController(itemView);
            Bundle bundle = new Bundle();
            bundle.putInt("pokemonId", pokemonId);
            navController.navigate(R.id.action_listFragment_to_pokemonFragment, bundle);
        }
    }
}
