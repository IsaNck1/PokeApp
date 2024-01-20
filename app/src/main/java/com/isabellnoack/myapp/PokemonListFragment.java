package com.isabellnoack.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.isabellnoack.myapp.databinding.FragmentListBinding;

public class PokemonListFragment extends Fragment {

    private int numberOfColumns = 1; // Anzahl der Spalten bei Start
    private FragmentListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Layout erstellen/ inflaten
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Button PREVIOUS PAGE
        binding.buttonSecond.setOnClickListener(view1 ->
                NavHostFragment.findNavController(PokemonListFragment.this)
                        .navigate(R.id.action_listFragment_to_pokemonFragment));

        //Button Columns
        binding.buttonNumberOfColumns.setOnClickListener(view12 -> {
            if (numberOfColumns == 3) {
                numberOfColumns = 1;
            } else {
                numberOfColumns++;
            }
            updateRecyclerView();
        });

        //set Fixed Size
        binding.recyclerView.setHasFixedSize(true);

        //Hier wird der GridLayoutManager mit 1,2 oder 3 Spalten erstellt
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), numberOfColumns);
        binding.recyclerView.setLayoutManager(layoutManager);

        //Hier wird der Adapter für die RecyclerView gesetzt (Alle Variablen werden übergeben: recyclerViewItems,context,numberOfColumns)
        PokemonListAdapter pokemonListAdapter = new PokemonListAdapter(numberOfColumns, getActivity());
        binding.recyclerView.setAdapter(pokemonListAdapter);

    }

    // Methode zur Aktualisierung der RecyclerView mit der neuen Anzahl von Spalten
    private void updateRecyclerView() {
        // Adapter für die RecyclerView erneut setzen
        PokemonListAdapter pokemonListAdapter = new PokemonListAdapter(numberOfColumns, getActivity());
        binding.recyclerView.setAdapter(pokemonListAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        binding.recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}