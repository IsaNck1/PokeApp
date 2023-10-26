package com.isabellnoack.myapp;

import com.isabellnoack.myapp.api.PokeAPI;

import java.io.IOException;

public class PokeAPITest {

    @org.junit.Test
    public void testBerry1() throws IOException {
        System.out.println(new PokeAPI().requestBerry(1));
    }

    @org.junit.Test
    public void testPokemon1() throws IOException {
        System.out.println(new PokeAPI().requestPokemon(1));
    }

}
