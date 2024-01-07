package com.isabellnoack.myapp;

import static com.isabellnoack.myapp.MainActivity.pokemonIdToOpen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.isabellnoack.myapp.databinding.FragmentListBinding;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    ArrayList<RecyclerViewItem> recyclerViewItems;
    private int numberOfColumns = 1; // Anzahl der Spalten bei Start
    private FragmentListBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Layout erstellen/ inflaten
        binding = FragmentListBinding.inflate(inflater, container, false);


        return binding.getRoot();

    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //Button PREVIOUS PAGE
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ListFragment.this)
                        .navigate(R.id.action_listFragment_to_pokemonFragment);
            }


        });

        //Button Columns
        binding.buttonNumberOfColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfColumns == 3) {
                    numberOfColumns = 1;
                } else  {
                    numberOfColumns++;
                }
                updateRecyclerView();
            }

        });

        Context context = view.getContext();
        Activity activity = getActivity();

        new Thread(() -> {

            //Neue Array Liste aus RecyclerViewItem
            recyclerViewItems = new ArrayList<RecyclerViewItem>();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //set Fixed Size
                    binding.recyclerView.setHasFixedSize(true);

                    //Hier wird der GridLayoutManager mit 1,2 oder 3 Spalten erstellt
                    //int numberOfColumns = 2;
                    GridLayoutManager layoutManager = new GridLayoutManager(context, numberOfColumns);
                    binding.recyclerView.setLayoutManager(layoutManager);

                    //Hier wird der Adapter für die RecyclerView gesetzt (Alle Variablen werden übergeben: recyclerViewItems,context,numberOfColumns)
                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context, numberOfColumns, getActivity());
                    binding.recyclerView.setAdapter(recyclerViewAdapter);
                }
            });
        }).start();

    }

    // Methode zur Aktualisierung der RecyclerView mit der neuen Anzahl von Spalten
    private void updateRecyclerView() {
        // Adapter für die RecyclerView erneut setzen
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), numberOfColumns, getActivity());
        binding.recyclerView.setAdapter(recyclerViewAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        binding.recyclerView.setLayoutManager(layoutManager);

        //Zu Item scrollen - funktioniert nicht
        //layoutManager.scrollToPosition(pokemonIdToOpen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}