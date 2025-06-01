package com.example.formulabasic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriversActivity extends AppCompatActivity {
    private DriverAdapter adapter;
    private ArrayList<Driver> datos = new ArrayList<>();
    private DriverDataBaseService service;
    private int currentPage = 0;
    private final int LIMIT = 20;
    private RecyclerView lista;
    private Button previousButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drivers);

        lista = findViewById(R.id.lista_drivers);
        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);

        adapter = new DriverAdapter(datos, v -> {
            String driverId = (String) v.getTag();
            Intent intent = new Intent(DriversActivity.this, DriverDetailActivity.class);
            intent.putExtra("driverId", driverId);
            startActivity(intent);
        });
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ergast.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        service = retrofit.create(DriverDataBaseService.class);

        previousButton.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                loadDrivers();
            }
        });

        nextButton.setOnClickListener(v -> {
            currentPage++;
            loadDrivers();
        });

        loadDrivers();

        Button viewFavoritesButton = findViewById(R.id.view_favorites_button);
        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(DriversActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
    }

    private void loadDrivers() {
        int offset = currentPage * LIMIT;
        service.listDrivers(LIMIT, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            datos.clear();
                            datos.addAll(response.MRData.DriverTable.Drivers);
                            adapter.notifyDataSetChanged();
                            updatePaginationButtons(response.MRData.total);
                        },
                        error -> Log.e("API_ERROR", "Error en la API: " + error.getMessage())
                );
    }

    private void updatePaginationButtons(String totalDriversString) {
        try {
            int totalDrivers = Integer.parseInt(totalDriversString);
            previousButton.setEnabled(currentPage > 0);
            nextButton.setEnabled((currentPage + 1) * LIMIT < totalDrivers);
        } catch (NumberFormatException e) {
            Log.e("PAGINATION_ERROR", "Invalid total drivers string: " + totalDriversString);
            previousButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }
}
