package com.isabellnoack.myapp;

import static com.isabellnoack.myapp.MainActivity.pokemonIdToOpen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.isabellnoack.myapp.api.Ability;
import com.isabellnoack.myapp.api.NameWithURL;
import com.isabellnoack.myapp.api.PokeAPI;
import com.isabellnoack.myapp.api.Pokemon;
import com.isabellnoack.myapp.databinding.FragmentPokemonBinding;

import java.io.IOException;

public class PokemonFragment extends Fragment {

    private FragmentPokemonBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentPokemonBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    int pokemonId = pokemonIdToOpen;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Button NEXT POKEMON
        binding.nextPokemonButton.setOnClickListener((view1) -> {
            // laden des nächsten Pokemon
            if (pokemonId == 1025) { //1017 ist das letzte Pokemon
                pokemonId = 1;
                pokemonIdToOpen = 1;
            } else {
                pokemonId++;
                pokemonIdToOpen++;
            }
            loadPokemon();
        });

        //Button PREVIOUS POKEMON
        binding.previousPokemonButton.setOnClickListener((view1) -> {
            // laden des vorherigen Pokemon
            if (pokemonId == 1) {
                pokemonId = 1025;
                pokemonIdToOpen = 1025;
            } else {
                pokemonId--;
                pokemonIdToOpen--;
            }

            loadPokemon();
        });

        // Pokemon anhand ID wird geladen
        loadPokemon();

    }

    /**
     * Ersten Character groß schreiben
     */
    String capitalize(String name) {
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        return name;
    }

    /**
     * @noinspection StringConcatenationInLoop
     */
    @SuppressLint("SetTextI18n")
    //Nicht highlighten
    void loadPokemon() {
        //Neuer Thread da Hauptthread nicht blockiert werden darf

        Activity activity = getActivity();

        new Thread(() -> { //Download Thread
            try {
                Pokemon pokemon = new PokeAPI().requestPokemon(pokemonId); //Funktion der neuen Instanz pokeAPI aufrufen
                activity.runOnUiThread(() -> { //UI Thread

                    //UI Bindings
                    binding.pokemonName.setText("Name: " + capitalize(pokemon.name));

                    binding.pokemonId.setText("ID: " + pokemonId);

                    // Bild laden und in ImageView setzen
                    //try {
                    //    Bitmap bitmap = PokeAPI.ImageLoader.loadImageFromUrl(pokemon.imageUrlDreamWorld); // Hier wird der entsprechenden Bild-URL-Pfad übergeben
                    //    binding.pokemonImage.setImageBitmap(bitmap); //an Layout übergeben
                    //} catch (IOException e) {
                    //    e.printStackTrace(); //Fehlermeldung
                    //}

                    // Bild laden und in ImageView setzen
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (pokemon.imageUrl != "") { //wenn Sprite null > in RecyclerViewItem übersprungen > Standard Wert ""
                                        final Bitmap bitmap = PokeAPI.ImageLoader.loadImageFromUrl(pokemon.imageUrl);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.pokemonImage.setImageBitmap(bitmap);
                                            }
                                        });

                                    } else { //Hier statt "" dann empty.xml setzen
                                        binding.pokemonImage.setImageResource(R.drawable.empty);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace(); //Fehlermeldung
                                }
                            }

                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (pokemon.weight != 0) {
                        binding.pokemonWeight.setText("Weight: " + (pokemon.weight / 10.0f) + " kg"); //Angabe in Hectogramm // ausserdem mit float multiliziert, für Kommastellen
                    } else {
                        binding.pokemonWeight.setText("Weight: undefined");
                    }

                    if (pokemon.height != 0) {
                        binding.pokemonHeight.setText("Height: " + (pokemon.height / 10.0f) + " m"); //Angabe in Decimeter
                    } else {
                        binding.pokemonHeight.setText("Weight: undefined");
                    }

                    //pokemon.types, nur name aus dem array ausgeben
                    String types = "none";
                    for (NameWithURL type : pokemon.types) {
                        //for (Typ variablen-name : Datenquelle)
                        if (types.equals("none")) {
                            types = capitalize(type.name);                  //Wert überschreiben mit Pokemon Type
                        } else {
                            types = types + ", " + capitalize(type.name);   //Pokemon Type anhängen
                        }
                    }
                    binding.pokemonTypes.setText("Types: " + types);

                    //pokemon.abilities, nur name aus dem array ausgeben, und nach hidden filtern
                    String abilities = "none";
                    int numberOfAbilities = 0;
                    for (Ability ability : pokemon.abilities) {
                        NameWithURL nameWithURL = ability.nameWithURL;
                        if (ability.isHidden) {
                            continue; //macht mit dem for weiter, und ignoriert damit die Ability wenn isHidden true
                        }
                        //for (Typ variablen-name : Datenquelle)
                        numberOfAbilities++;
                        if (abilities.equals("none")) {
                            abilities = capitalize(nameWithURL.name);
                        } else {
                            abilities = abilities + ", " + capitalize(nameWithURL.name);
                        }
                    }
                    if (numberOfAbilities == 1) {
                        binding.pokemonAbilities.setText("Ability: " + abilities);
                    } else {
                        binding.pokemonAbilities.setText("Abilities: " + abilities);
                    }
                    
                });
            } catch (
                    IOException exception) { //Hier im Download Thread, um etwas anzeigen zu können in den UI Thread wechseln
                activity.runOnUiThread(() -> { //jetzt auf UI Thread
                    // send warning to user
                    Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG).show(); //Toast Klasse mit: context(so anzeigen: activity ; Fehler als String anzeigen; Wie lange angezeigt)
                });
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}