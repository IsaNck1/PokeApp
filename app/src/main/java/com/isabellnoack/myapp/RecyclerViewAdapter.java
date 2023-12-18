package com.isabellnoack.myapp;

import android.content.Context;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
//Adapter als Übersetzer von der RecyclingView zu meinen Daten und zu Layout

    //Deklarierung von zwei private Variablen: recyclerViewItems ist eine Liste für die anzuzeigenden Daten,
    //und context wird für den Zugriff auf Android-Ressourcen verwendet
    private ArrayList<RecyclerViewItem> recyclerViewItems; //ArrayList<typ> variablen-name
    private Context context;

    //Konstruktor initialisiert die Klassenvariablen mit den übergebenen Werten
    public RecyclerViewAdapter(ArrayList<RecyclerViewItem> recyclerViewItems, Context context) {
        this.recyclerViewItems = recyclerViewItems;
        this.context = context;
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
        holder.imageView.setImageBitmap(recyclerViewItems.get(position).image);
        holder.textViewID.setText(recyclerViewItems.get(position).id.toString());
        holder.textViewName.setText(recyclerViewItems.get(position).name);
    }


    // Grösse der Liste
    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    // Definition ViewHolder Klasse
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewID;
        TextView textViewName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_pokemon_image); //Layout finden
            textViewName = itemView.findViewById(R.id.list_pokemon_name);
            textViewID = itemView.findViewById(R.id.list_pokemon_id);
        }
    }
}
