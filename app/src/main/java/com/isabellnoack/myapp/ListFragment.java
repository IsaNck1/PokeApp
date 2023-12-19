package com.isabellnoack.myapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isabellnoack.myapp.api.PokeAPI;
import com.isabellnoack.myapp.api.Pokemon;
import com.isabellnoack.myapp.databinding.FragmentListBinding;

import java.io.IOException;
import java.util.ArrayList;

public class ListFragment extends Fragment {

    //NEU
    ArrayList<RecyclerViewItem> recyclerViewItems;

    private FragmentListBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

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
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        Context context = view.getContext();

        new Thread(() -> {
            //Neue Array Liste aus RecyclerViewItem
            recyclerViewItems = new ArrayList<RecyclerViewItem>();

            //Array Liste neues Item hinzufügen
            try {
                //Schleife, Listen Eintrag für alle Pokemon ID von 1 bis 1017
                for (int id = 1; id <= 10; id++) {
                    recyclerViewItems.add(new RecyclerViewItem(id)); //Listen Eintrag
                }
                //To-Do: performantes laden!

            } catch (IOException ignored) {
                // todo send warning to user
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //set Fixed Size
                    binding.recyclerView.setHasFixedSize(true);

                    //Hier wird der GridLayoutManager mit 1,2 oder 3 Spalten erstellt
                    int numberOfColumns = 2;
                    GridLayoutManager layoutManager = new GridLayoutManager(context, numberOfColumns);
                    binding.recyclerView.setLayoutManager(layoutManager);

                    //Hier wird der Adapter für die RecyclerView gesetzt (Alle Variablen werden übergeben: recyclerViewItems,context,numberOfColumns)
                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(recyclerViewItems, context, numberOfColumns);
                    binding.recyclerView.setAdapter(recyclerViewAdapter);
                }
            });
        }).start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}