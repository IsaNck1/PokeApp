package com.isabellnoack.myapp.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

    public PokemonSpecies requestPokemonSpecies(int id) throws IOException {
        String url = "https://pokeapi.co/api/v2/pokemon-species/" + id; //Datenquelle
        JsonReader reader;
        reader = requestJsonReader(url); //Interpretiert Text aus Internet als JSON
        return readPokemonSpecies(reader); //Funktion für JSON in Pokemon Instanz
    }

    //2
    JsonReader requestJsonReader(String url) throws IOException { //JSON lesen
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); //Datenstrom als UTF-8 lesen
        return new JsonReader(bufferedReader); //Text/JSON als Antwort
    }

    public static class ImageLoader {

        public static Bitmap loadImageFromUrl(String imageUrl) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");

            // Öffne einen InputStream zum Lesen der Bildressource
            InputStream inputStream = connection.getInputStream();

            // Dekodiere den InputStream in ein Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Schließe den InputStream
            inputStream.close();

            return bitmap;
        }
    }


    //3
    Pokemon readPokemon(JsonReader reader) throws IOException {
        Pokemon pokemon = new Pokemon(); //Pokemon erstellen (Instanz der Klasse Pokemon)
        reader.beginObject();   //Erste geschwungene Klammer geöffnet
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

                case "sprites": //Darunter weitere geschungene Klammer, daher neues BeginObjekt

                    reader.beginObject(); //BeginObjekt hat immer Reader.has Next
                    while (reader.hasNext()) {
                        switch (reader.nextName()) {
                            case "other": //Darunter weitere geschungene Klammer, daher neues BeginObjekt

                                reader.beginObject();
                                while (reader.hasNext()) {
                                    switch (reader.nextName()) {
                                        case "official-artwork": //Darunter weitere geschungene Klammer, daher neues BeginObjekt

                                            reader.beginObject();
                                            while (reader.hasNext()) {
                                                switch (reader.nextName()) {
                                                    case "front_default":
                                                        if (reader.peek() == JsonToken.STRING) {
                                                            pokemon.imageUrl = reader.nextString();
                                                        } else reader.skipValue();
                                                        break; // Weil ganz unten, bitte verlasse das switch
                                                    default:
                                                        reader.skipValue();
                                                }
                                            }
                                            reader.endObject();
                                            break;
                                        default:
                                            reader.skipValue();
                                    }
                                }
                                reader.endObject();
                                break;
                            default:
                                reader.skipValue();
                        }
                    }
                    reader.endObject();
                    break;


                case "weight":
                    if (reader.peek() == JsonToken.NUMBER) {
                        pokemon.weight = reader.nextInt();
                    } else reader.skipValue();
                    break;

                case "height":
                    if (reader.peek() == JsonToken.NUMBER) {
                        pokemon.height = reader.nextInt();
                    } else reader.skipValue();
                    break;

                case "types":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        while (reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "type":
                                    pokemon.types.add(readNameWithURL(reader));
                                    break;
                                default:
                                    reader.skipValue();
                            }
                        }
                        reader.endObject();
                    }
                    reader.endArray();
                    break;

                case "abilities":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        pokemon.abilities.add(readAbility(reader));
                    }
                    reader.endArray();
                    break;


                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return pokemon;
    }

    Ability readAbility(JsonReader reader) throws IOException { //Rückgabewert Ability
        Ability ability = new Ability();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "ability":
                    ability.nameWithURL = readNameWithURL(reader);
                    break;
                case "is_hidden":
                    ability.isHidden = reader.nextBoolean();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return ability;
    }

    PokemonSpecies readPokemonSpecies(JsonReader reader) throws IOException {
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "name":
                    pokemonSpecies.name = reader.nextString();
                    break;

                case "flavor_text_entries":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        FlavorTextEntry flavorTextEntry = readFlavorTextEntry(reader);
                        if (flavorTextEntry.language.name.equals("en")) {   // nur englische Texte speichern
                            pokemonSpecies.flavorTextEntries.add(flavorTextEntry);
                        }
                    }
                    reader.endArray();
                    break;

                case "varieties":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        Variety variety = readVariety(reader);
                        if (!variety.isDefault) { //falls is_default ist false:
                            pokemonSpecies.varieties.add(variety);
                        }
                    }
                    reader.endArray();
                    break;

                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return pokemonSpecies;
    }

    private FlavorTextEntry readFlavorTextEntry(JsonReader reader) throws IOException {
        FlavorTextEntry flavorTextEntry = new FlavorTextEntry();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "flavor_text":
                    flavorTextEntry.flavorText = reader.nextString();
                    break;
                case "language":
                    flavorTextEntry.language = readNameWithURL(reader);
                    break;
                case "version":
                    flavorTextEntry.version = readNameWithURL(reader);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return flavorTextEntry;
    }

    private Variety readVariety(JsonReader reader) throws IOException {
        Variety variety = new Variety();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "is_default":
                    variety.isDefault = reader.nextBoolean();
                    break;
                case "pokemon":
                    variety.pokemon = readNameWithURL(reader);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return variety;
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
