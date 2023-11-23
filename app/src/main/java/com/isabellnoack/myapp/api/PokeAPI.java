package com.isabellnoack.myapp.api;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @noinspection SwitchStatementWithTooFewBranches
 */
public class PokeAPI {

    //1
    public Pokemon requestPokemon(int id) throws IOException {
        //Wo lesen wir unsere Daten her
        String url = "https://pokeapi.co/api/v2/pokemon/" + id; //Datenquelle
        JsonReader reader;
        reader = requestJsonReader(url); //Interpretiert Text aus Internet als JSON
        return readPokemon(reader); //Funktion für JSON in Pokemon Instanz
    }

    //2
    JsonReader requestJsonReader(String url) throws IOException { //JSON lesen
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); //Datenstrom als UTF-8 lesen
        return new JsonReader(bufferedReader); //Text/JSON als Antwort
    }

    public Berry requestBerry(int id) throws IOException {
        return readBerry(requestJsonReader("https://pokeapi.co/api/v2/berry/" + id));
    }

    //3
    Pokemon readPokemon(JsonReader reader) throws IOException {
        Pokemon pokemon = new Pokemon(); //Pokemon erstellen (Instanz der Klasse Pokemon)
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "name":
                    pokemon.name = reader.nextString(); //Formatierung: Strg+Alt+O Strg+Alt+L
                    break;

                case "base_experience":
                    if (reader.peek() == JsonToken.NUMBER) { //Überprüft ob Token eine Zahl ist
                        pokemon.baseExperience = reader.nextInt();
                    } else reader.skipValue();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return pokemon;
    }

    Berry readBerry(JsonReader reader) throws IOException {
        Berry berry = new Berry();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "name":
                    berry.name = reader.nextString();
                    break;
                case "size":
                    berry.size = reader.nextInt();
                    break;
                case "smoothness":
                    berry.smoothness = reader.nextInt();
                    break;
                case "flavors":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        berry.flavors.add(readFlavorWithPotency(reader));
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return berry;
    }

    BerryFlavor readFlavorWithPotency(JsonReader reader) throws IOException {
        BerryFlavor flavorWithPotency = new BerryFlavor();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "potency":
                    flavorWithPotency.potency = reader.nextInt();
                    break;
                case "flavor":
                    flavorWithPotency.flavor = readNameWithURL(reader);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return flavorWithPotency;
    }

    NameWithURL readNameWithURL(JsonReader reader) throws IOException {
        NameWithURL instance = new NameWithURL();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "name":
                    instance.name = reader.nextString();
                    break;
                case "url":
                    instance.url = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return instance;
    }


}
