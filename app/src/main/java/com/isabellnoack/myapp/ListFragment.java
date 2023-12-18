package com.isabellnoack.myapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
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

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ListFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        Context context = view.getContext();

        new Thread(() -> {

            //1 Neue Array Liste aus RecyclerViewItem
            recyclerViewItems = new ArrayList<RecyclerViewItem>();

            //2 Array Liste neues Item hinzufügen
            try {
                recyclerViewItems.add(new RecyclerViewItem(3)); //Listen Eintrag
                recyclerViewItems.add(new RecyclerViewItem(4)); //Listen Eintrag
                recyclerViewItems.add(new RecyclerViewItem(5)); //Listen Eintrag
                recyclerViewItems.add(new RecyclerViewItem(6)); //Listen Eintrag
                recyclerViewItems.add(new RecyclerViewItem(7)); //Listen Eintrag
            } catch (IOException ignored) {
                // todo send warning to user
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //NEU - was tut das?
                    binding.recyclerView.setHasFixedSize(true);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(context)); //wofür?

                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(recyclerViewItems,context);
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