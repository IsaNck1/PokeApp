package com.isabellnoack.myapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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

    int pokemonId = 1;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Button next page
        binding.nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PokemonFragment.this)
                        .navigate(R.id.action_pokemonFragment_to_listFragment);
            }
        });

        //Button NEXT POKEMON
        binding.nextPokemonButton.setOnClickListener((view1) -> {
            // laden des nächsten Pokemon
            if (pokemonId == 1017) { //1017 ist das letzte Pokemon
                pokemonId = 1;
            } else {
                pokemonId++;
            }
            loadPokemon();
        });

        //Button PREVIOUS POKEMON
        binding.previousPokemonButton.setOnClickListener((view1) -> {
            // laden des vorherigen Pokemon
            if (pokemonId == 1) {
                pokemonId = 1017;
            } else {
                pokemonId--;
            }

            loadPokemon();
        });

        // Pokemon anhand ID wird geladen
        loadPokemon();

    }

    @SuppressLint("SetTextI18n") //Nicht highlighten

    void loadPokemon() {
        //Neuer Thread da Hauptthread nicht blockiert werden darf
        new Thread(() -> {
            try {
                Pokemon pokemon = new PokeAPI().requestPokemon(pokemonId); //Funktion der neuen Instanz pokeAPI aufrufen
                getActivity().runOnUiThread(() -> {

                    //UI
                    String name = pokemon.name;
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1); //Erster Character groß geschrieben :(
                    binding.pokemonName.setText("Name: " + name);
                    binding.pokemonExperience.setText("Experience: " + pokemon.baseExperience);
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
                                    final Bitmap bitmap = PokeAPI.ImageLoader.loadImageFromUrl(pokemon.imageUrl);

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.pokemonImage.setImageBitmap(bitmap);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace(); //Fehlermeldung
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            } catch (IOException ignored) {
                // todo send warning to user
            }
        }).start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}