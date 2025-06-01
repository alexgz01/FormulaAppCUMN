package com.example.formulabasic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button; // Import Button
import android.widget.TextView;
import android.widget.Toast; // Import Toast

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriverDetailActivity extends AppCompatActivity {
    private TextView driverName, driverNationality, driverDOB, driverUrl;
    private Button favoriteButton;
    private DriverDataBaseService service;
    private FavoritesManager favoritesManager;
    private String currentDriverId;
    private String currentDriverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);

        driverName = findViewById(R.id.driver_name);
        driverNationality = findViewById(R.id.driver_nationality);
        driverDOB = findViewById(R.id.driver_dob);
        driverUrl = findViewById(R.id.driver_url);
        favoriteButton = findViewById(R.id.favorite_button);

        favoritesManager = new FavoritesManager(this);

        currentDriverId = getIntent().getStringExtra("driverId");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ergast.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        service = retrofit.create(DriverDataBaseService.class);
        service.getDriverDetails(currentDriverId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            Driver driver = response.MRData.DriverTable.Drivers.get(0);
                            currentDriverName = driver.givenName + " " + driver.familyName;
                            driverName.setText(currentDriverName);
                            driverNationality.setText("Nacionalidad: " + driver.nationality);
                            driverDOB.setText("Fecha de Nacimiento: " + driver.dateOfBirth);
                            driverUrl.setText("Más detalles: " + driver.url);

                            driverUrl.setOnClickListener(v -> {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(driver.url));
                                startActivity(browserIntent);
                            });

                            updateFavoriteButtonState();
                        },
                        error -> Log.e("API_ERROR", "Error en la API: " + error.getMessage())
                );

        favoriteButton.setOnClickListener(v -> toggleFavorite());
    }

    private void updateFavoriteButtonState() {
        if (favoritesManager.isFavorite(currentDriverId)) {
            favoriteButton.setText("Eliminar de Favoritos");
        } else {
            favoriteButton.setText("Añadir a Favoritos");
        }
    }

    private void toggleFavorite() {
        if (favoritesManager.isFavorite(currentDriverId)) {
            if (favoritesManager.removeFavorite(currentDriverId)) {
                Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (favoritesManager.addFavorite(currentDriverId, "driver", currentDriverName)) {
                Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al añadir", Toast.LENGTH_SHORT).show();
            }
        }
        updateFavoriteButtonState();
    }
}