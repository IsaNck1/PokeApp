package com.isabellnoack.pokeapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.isabellnoack.pokeapp.api.Ability;
import com.isabellnoack.pokeapp.api.FlavorTextEntry;
import com.isabellnoack.pokeapp.api.ImageLoader;
import com.isabellnoack.pokeapp.api.NameWithURL;
import com.isabellnoack.pokeapp.api.PokeAPI;
import com.isabellnoack.pokeapp.api.Pokemon;
import com.isabellnoack.pokeapp.api.PokemonSpecies;
import com.isabellnoack.pokeapp.api.Variety;
import com.isabellnoack.pokeapp.databinding.FragmentDetailBinding;

import java.io.IOException;

@SuppressLint("SetTextI18n") //Hard Coded Text (Warnung ignorieren)
public class DetailFragment extends Fragment {

    static final int FIRST_POKEMON_ID = 1;
    static final int LAST_POKEMON_ID = 1025;

    public static int pokemonIdToOpen = FIRST_POKEMON_ID;

    private FragmentDetailBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Layout Inflater
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Sensor
        try {
            //Zugriff auf Sensoren über SensorManager
            SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE); // getActivity gibt mir die Main Activity zurück, damit hat man auf vieles Zugriff
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(new ShakeSensor(binding), sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (NullPointerException ignored) {
            // wenn NullPointerException d.h. Sensoren nicht gegeben, dann passiert einfach nichts
        }

        // Button NEXT POKEMON
        binding.nextPokemonButton.setOnClickListener((view1) -> {
            // laden des nächsten Pokemon
            if (pokemonIdToOpen == LAST_POKEMON_ID) {
                pokemonIdToOpen = FIRST_POKEMON_ID;
            } else {
                pokemonIdToOpen++;
            }
            loadPokemon();
            loadPokemonSpecies();
        });

        // Button PREVIOUS POKEMON
        binding.previousPokemonButton.setOnClickListener((view1) -> {
            // laden des vorherigen Pokemon
            if (pokemonIdToOpen == FIRST_POKEMON_ID) {
                pokemonIdToOpen = LAST_POKEMON_ID;
            } else {
                pokemonIdToOpen--;
            }

            loadPokemon();
            loadPokemonSpecies();
        });

        // Pokemon anhand ID wird geladen
        loadPokemon();
        // PokemonSpecies anhand ID wird geladen
        loadPokemonSpecies();

    }

    /**
     * Ersten Character groß schreiben
     */
    public static String capitalize(String name) {
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        return name;
    }

    /**
     * @noinspection StringConcatenationInLoop
     */
    @SuppressLint("SetTextI18n")
    // Hard Coded Text nicht highlighten

    void loadPokemon() {
        // Neuer Thread da Hauptthread nicht blockiert werden darf

        Activity activity = getActivity();
        assert activity != null;

        new Thread(() -> { // Download Thread
            try {
                // load pokemon
                Pokemon pokemon = new PokeAPI().requestPokemon(pokemonIdToOpen); // Funktion der neuen Instanz pokeAPI aufrufen

                activity.runOnUiThread(() -> { //UI Thread
                    binding.pokemonName.setText("Name: " + capitalize(pokemon.name));
                    binding.pokemonId.setText("ID: " + pokemonIdToOpen);

                    if (pokemon.weight != 0) {
                        binding.pokemonWeight.setText("Weight: " + (pokemon.weight / 10.0f) + " kg"); // Angabe in Hectogramm // ausserdem mit float multiliziert, für Kommastellen
                    } else {
                        binding.pokemonWeight.setText("Weight: undefined");
                    }

                    if (pokemon.height != 0) {
                        binding.pokemonHeight.setText("Height: " + (pokemon.height / 10.0f) + " m"); // Angabe in Decimeter
                    } else {
                        binding.pokemonHeight.setText("Height: undefined");
                    }

                    // pokemon.types, nur name aus dem array ausgeben
                    String types = "none";
                    for (NameWithURL type : pokemon.types) {
                        if (types.equals("none")) {
                            types = capitalize(type.name);                  // Wert überschreiben mit Pokemon Type
                        } else {
                            types = types + ", " + capitalize(type.name);   // Pokemon Type anhängen
                        }
                    }
                    binding.pokemonTypes.setText("Types: " + types);

                    // pokemon.abilities, nur name aus dem array ausgeben, und nach hidden filtern
                    String abilities = "none";
                    int numberOfAbilities = 0;
                    for (Ability ability : pokemon.abilities) {
                        NameWithURL nameWithURL = ability.nameWithURL;
                        if (ability.isHidden) {
                            continue; // macht mit dem for weiter, und ignoriert damit die Ability wenn isHidden true
                        }
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
                }); // UI Thread Ende

                // Download Thread
                // load pokemon image
                if (!pokemon.imageUrl.isEmpty()) { // wenn Sprite null > in RecyclerViewItem übersprungen > Standard Wert ""
                    final Bitmap bitmap = ImageLoader.loadImageFromUrl(pokemon.imageUrl);
                    getActivity().runOnUiThread(() -> binding.pokemonImage.setImageBitmap(bitmap));
                } else {
                    binding.pokemonImage.setImageResource(R.drawable.empty); // Hier statt "" empty.xml setzen
                }
            } catch (IOException exception) {
                // Zu UI Thread wechseln (um was anzeigen zu können)
                activity.runOnUiThread(() -> {
                    Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG).show(); // Toast Klasse mit: context(so anzeigen: activity ; Fehler als String anzeigen; Wie lange angezeigt)
                });
            }
        }).start();
    }

    void loadPokemonSpecies() {
        Activity activity = getActivity();
        assert activity != null;

        new Thread(() -> {
            try {
                PokemonSpecies pokemonSpecies = new PokeAPI().requestPokemonSpecies(pokemonIdToOpen); // Funktion der neuen Instanz pokeAPI aufrufen
                activity.runOnUiThread(() -> { // UI Thread

                    // PokemonSpecies.flavorTextEntries
                    flavorTextIndex = 0;
                    showNextFlavorText(pokemonSpecies);
                    binding.pokemonSpeciesFlavorText.setOnClickListener(v -> {
                        showNextFlavorText(pokemonSpecies);
                    });

                    // PokemonSpecies.variety, schauen wie viele es gibt
                    String varieties = "none";
                    int numberOfVarieties = 0;
                    for (Variety variety : pokemonSpecies.varieties) {
                        NameWithURL pokemon = variety.pokemon;
                        if (varieties.equals("none")) {
                            varieties = capitalize(pokemon.name);
                        } else {
                            varieties = varieties + ", " + capitalize(pokemon.name);
                        }
                        numberOfVarieties++;
                    }
                    if (numberOfVarieties == 1) {
                        binding.pokemonSpeciesVarieties.setText("Variety: " + varieties);
                    } else {
                        binding.pokemonSpeciesVarieties.setText("Varieties: " + varieties);
                    }
                });
            } catch (IOException exception) {
                activity.runOnUiThread(() -> {
                    Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    int flavorTextIndex = 0;

    /**
     * Flavor Text wechseln und Versionen anzeigen
     */
    void showNextFlavorText(PokemonSpecies pokemonSpecies) {
        if (pokemonSpecies.flavorTextEntries.isEmpty()) {
            binding.pokemonSpeciesFlavorText.setText("");
            return;
        }
        FlavorTextEntry flavorTextEntry = pokemonSpecies.flavorTextEntries.get(flavorTextIndex); // Eintrag mit ID
        String flavorText1 = flavorTextEntry.flavorText;
        binding.pokemonSpeciesFlavorText.setText("'" +
                flavorText1
                        .replace("\n", " ")
                        .replace("\f", " ") +
                "'" + "\n Version " + flavorTextEntry.version.name);
        if (++flavorTextIndex == pokemonSpecies.flavorTextEntries.size()) { // Index erhöhen
            flavorTextIndex = 0;
        }
    }
}