package com.isabellnoack.myapp.api;

import static com.isabellnoack.myapp.api.PokeAPI.USER_AGENT;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader {

    public static Bitmap loadImageFromUrl(String imageUrl) throws IOException {
        if(imageUrl.isEmpty()) return null;
        HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);

        // Öffne einen InputStream zum Lesen der Bildressource
        InputStream inputStream = connection.getInputStream();

        // Dekodiere den InputStream in ein Bitmap
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // Schließe den InputStream
        inputStream.close();

        return bitmap;
    }
}