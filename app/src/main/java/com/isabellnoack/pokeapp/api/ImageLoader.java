package com.isabellnoack.pokeapp.api;

import static com.isabellnoack.pokeapp.api.PokeAPI.USER_AGENT;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader {

    public static Bitmap loadImageFromUrl(String imageUrl) throws IOException {
        if (imageUrl.isEmpty()) return null;
        HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);

        // Öffne einen InputStream zum Lesen der Bildressource
        try (InputStream inputStream = connection.getInputStream()) { // "try with resource" schließt InputStream automatisch
            // Dekodiere den InputStream in ein Bitmap
            return BitmapFactory.decodeStream(inputStream);
        }
    }
}