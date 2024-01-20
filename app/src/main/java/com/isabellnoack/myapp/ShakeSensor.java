package com.isabellnoack.myapp;

import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.isabellnoack.myapp.databinding.FragmentPokemonBinding;

public class ShakeSensor implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 20.0f; //g-Beschleunigung
    private final FragmentPokemonBinding binding;
    private long lastShakeTime;

    public ShakeSensor(FragmentPokemonBinding binding) {
        this.binding = binding;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Beschleunigungssensors Werte
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Absolute Beschleunigung berechnen (Betrag der Differenz zur Erdbeschleunigung)
            // .abs = absoluter Betrag: heißt es geht in + oder -
            float acceleration = Math.abs(x) + Math.abs(y) + Math.abs(z) - (SensorManager.GRAVITY_EARTH); //Gravitation 9,81 m/s², auf Z wenn es flach auf Tisch liegt

            // Die aktuelle Zeit in Millisekunden
            long currentTime = System.currentTimeMillis();

            // Überprüfe, ob die absolute Beschleunigung den Schwellenwert für ein Schütteln überschreitet
            if (acceleration > SHAKE_THRESHOLD) {

                // Cool-Down: Überprüfe, ob seit dem letzten Schütteln eine bestimmte Zeit vergangen ist
                if (currentTime > lastShakeTime + 1000) {       //Momentane Zeit ist größer als letzteShake Zeit + 1sek
                    onShakeDetected();                          //Wenn ja, wird Methode aufgerufen
                    lastShakeTime = currentTime;
                }
            }
        }
    }

    //Schütteln erkannt
    private void onShakeDetected() {
        //Hin und Her Rotations-Animation
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(binding.pokemonImage, "rotation", 0f, -20f, 20f, -20f, 0f);
        rotationAnimator.setDuration(1000);
        rotationAnimator.start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
