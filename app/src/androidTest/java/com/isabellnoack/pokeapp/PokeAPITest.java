package com.isabellnoack.pokeapp;

import com.isabellnoack.pokeapp.api.PokeAPI;

import java.io.IOException;

public class PokeAPITest {

    @org.junit.Test
    public void testPokemon1() throws IOException {
        System.out.println(new PokeAPI().requestPokemon(1));
    }

}
