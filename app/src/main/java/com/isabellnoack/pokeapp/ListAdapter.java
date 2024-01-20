package com.isabellnoack.pokeapp;

import static com.isabellnoack.pokeapp.DetailFragment.FIRST_POKEMON_ID;
import static com.isabellnoack.pokeapp.DetailFragment.LAST_POKEMON_ID;
import static com.isabellnoack.pokeapp.DetailFragment.capitalize;
import static com.isabellnoack.pokeapp.DetailFragment.pokemonIdToOpen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.isabellnoack.pokeapp.api.ImageLoader;
import com.isabellnoack.pokeapp.api.PokeAPI;
import com.isabellnoack.pokeapp.api.Pokemon;

import java.io.IOException;

public class ListAdapter extends RecyclerView.Adapter<ListItem_ViewHolder> {
// Adapter als Übersetzer von der RecyclerView zu meinen Daten und zu Layout
// Füllt Daten für RecyclerView

    private final int numberOfColumns;
    private final Activity activity;

    //Konstruktor
    public ListAdapter(int numberOfColumns, Activity activity) {
        this.numberOfColumns = numberOfColumns;
        this.activity = activity; // Für Toast anzeigen und für RunOnUIThread (Thread wechseln)
    }

    /**
     * ViewHolder erstellen
     * (anhand unserer PokemonListItemHolder (ViewHolder) Klasse)
     * Funktion onCreateViewHolder erstellt eine Ansicht (View) indem es das Layout recyclerview_item inflated
     */
    @NonNull
    @Override
    public ListItem_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false); //neues recyclerview_item
        ListItem_ViewHolder holder = new ListItem_ViewHolder(inflatedView);

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

        return holder;
    }

    /**
     * Daten in ViewHolder einfüllen
     * Für den ViewHolder werden an der position (An welcher Stelle in der Liste) die Daten abgefragt
     */
    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull ListItem_ViewHolder holder, int position) {

        int id = FIRST_POKEMON_ID + position;

        new Thread(() -> { // Daten laden auf Download-Thread
            try {
                Pokemon pokemon = new PokeAPI().requestPokemon(id);
                Bitmap image = ImageLoader.loadImageFromUrl(pokemon.imageUrl);

                activity.runOnUiThread(() -> { // Daten darstellen auf UI-Thread
                    holder.textViewID.setText("ID: " + id);
                    holder.textViewName.setText(capitalize(pokemon.name));
                    if (image != null) {
                        holder.imageView.setImageBitmap(image);
                    } else {
                        holder.imageView.setImageResource(R.drawable.empty);
                    }
                });
            } catch (IOException exception) {
                // Hier im Download Thread, um etwas anzeigen zu können in den UI Thread wechseln
                activity.runOnUiThread(() -> { // wieder UI Thread
                    Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG).show(); // Toast Klasse mit: context(so anzeigen: activity ; Fehler als String anzeigen; Wie lange angezeigt)
                });
            }
        }).start();

        // Von ListFragment zu DetailFragment
        holder.cardView.setOnClickListener(e -> {
            pokemonIdToOpen = id;
            // Navigation zum DetailFragment
            NavController navController = Navigation.findNavController(holder.itemView);
            navController.navigate(R.id.action_listFragment_to_detailFragment);
        });
    }

    /**
     * Größe der Liste / Wie viele Datenelemente
     */
    @Override
    public int getItemCount() {
        return LAST_POKEMON_ID;
    }
}
