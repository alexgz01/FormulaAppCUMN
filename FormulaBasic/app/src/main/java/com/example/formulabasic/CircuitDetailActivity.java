package com.example.formulabasic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CircuitDetailActivity extends AppCompatActivity {
    private TextView circuitName, circuitLocation, circuitLatLon, circuitUrl;
    private Button favoriteButton;
    private CircuitDataBaseService service;
    private FavoritesManager favoritesManager;
    private String currentCircuitId;
    private String currentCircuitName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit_detail);

        circuitName = findViewById(R.id.circuit_name);
        circuitLocation = findViewById(R.id.circuit_location);
        circuitLatLon = findViewById(R.id.circuit_lat_lon);
        circuitUrl = findViewById(R.id.circuit_url);
        favoriteButton = findViewById(R.id.favorite_button);

        favoritesManager = new FavoritesManager(this);

        currentCircuitId = getIntent().getStringExtra("circuitId");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ergast.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        service = retrofit.create(CircuitDataBaseService.class);
        service.getCircuitDetails(currentCircuitId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            Circuit circuit = response.MRData.CircuitTable.Circuits.get(0);
                            currentCircuitName = circuit.circuitName; // Store the name
                            circuitName.setText(currentCircuitName);
                            circuitLocation.setText(circuit.Location.locality + ", " + circuit.Location.country);
                            circuitLatLon.setText("Latitud: " + circuit.Location.lat + ", Longitud: " + circuit.Location.lon);
                            circuitUrl.setText("Detalles del circuito: " + circuit.url);

                            circuitUrl.setOnClickListener(v -> {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(circuit.url));
                                startActivity(browserIntent);
                            });

                            updateFavoriteButtonState();
                        },
                        error -> Log.e("API_ERROR", "Error en la API: " + error.getMessage())
                );

        favoriteButton.setOnClickListener(v -> toggleFavorite());
    }

    private void updateFavoriteButtonState() {
        if (favoritesManager.isFavorite(currentCircuitId)) {
            favoriteButton.setText("Eliminar de Favoritos");
        } else {
            favoriteButton.setText("Añadir a Favoritos");
        }
    }

    private void toggleFavorite() {
        if (favoritesManager.isFavorite(currentCircuitId)) {
            if (favoritesManager.removeFavorite(currentCircuitId)) {
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (favoritesManager.addFavorite(currentCircuitId, "circuit", currentCircuitName)) {
                Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al añadir", Toast.LENGTH_SHORT).show();
            }
        }
        updateFavoriteButtonState();
    }
}