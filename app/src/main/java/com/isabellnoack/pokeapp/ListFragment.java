package com.isabellnoack.pokeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.isabellnoack.pokeapp.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

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

        //Button "Pokemon" Detail
        binding.buttonSecond.setOnClickListener(view1 ->
                NavHostFragment.findNavController(ListFragment.this)
                        .navigate(R.id.action_listFragment_to_detailFragment));

        //Button "Columns"
        binding.buttonNumberOfColumns.setOnClickListener(view12 -> {
            if (numberOfColumns == 3) {
                numberOfColumns = 1;
            } else {
                numberOfColumns++;
            }
            updateRecyclerView();
        });
        updateRecyclerView();
    }

    // Methode zur Aktualisierung der RecyclerView (mit der neuen Anzahl von Spalten)
    private void updateRecyclerView() {
        //set Fixed Size
        binding.recyclerView.setHasFixedSize(true);

        //Hier wird der GridLayoutManager mit 1,2 oder 3 Spalten erstellt
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        binding.recyclerView.setLayoutManager(layoutManager);

        //Hier wird der Adapter für die RecyclerView gesetzt (Alle Variablen werden übergeben: recyclerViewItems,context,numberOfColumns)
        // = Zuweisung ListAdapter zu RecyclerView
        ListAdapter listAdapter = new ListAdapter(numberOfColumns, getActivity());
        binding.recyclerView.setAdapter(listAdapter);
    }
}