package com.isabellnoack.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.isabellnoack.myapp.databinding.FragmentFirstBinding;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Ability;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;

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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });


        new Thread(() -> {
            PokeApi pokeApi = new PokeApiClient(); //Instanz einer Klasse erstellen
            PokemonSpecies bulbasaur = pokeApi.getPokemonSpecies(1); //Funktion der neuen Instanz pokeAPI aufrufen
            Ability a = pokeApi.getAbility(1);
            System.out.println(a);
            getActivity().runOnUiThread(() -> {
                // Hier UI verändern: show text on UI/button
                binding.textviewFirst.setText(bulbasaur.toString());
            });
        }).start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}