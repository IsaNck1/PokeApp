package com.isabellnoack.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.isabellnoack.myapp.api.PokeAPI;
import com.isabellnoack.myapp.api.Pokemon;
import com.isabellnoack.myapp.databinding.FragmentFirstBinding;

import java.io.IOException;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    int pokemonId = 1;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.nextPokemonButton.setOnClickListener((view1) -> {
            // wir laden das nächste Pokemon
            pokemonId++;
            loadPokemon();
        });

        // wenn wir laden, laden wir Pokemon mit ID 1
        loadPokemon();

    }

    void loadPokemon() {
        //Neuer Thread da Hauptthread nicht blockiert werden darf
        new Thread(() -> {
            try {
                Pokemon bulbasaur = new PokeAPI().requestPokemon(pokemonId); //Funktion der neuen Instanz pokeAPI aufrufen
                getActivity().runOnUiThread(() -> {
                    // Hier UI verändern: show text on UI/button
                    String name = bulbasaur.name;
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1); //Erster Character groß geschrieben :(
                    binding.pokemonName.setText("Name: " + name);
                    binding.pokemonExperience.setText("Experience: " + bulbasaur.baseExperience);
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