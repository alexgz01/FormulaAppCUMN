package com.example.formulabasic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class CircuitsActivity extends AppCompatActivity {
    private CircuitAdapter adapter;
    private ArrayList<Circuit> datos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_circuits);

        RecyclerView lista = findViewById(R.id.lista_circuitos);
        adapter = new CircuitAdapter(datos, v -> {
            String circuitId = (String) v.getTag();
            Intent intent = new Intent(CircuitsActivity.this, CircuitDetailActivity.class);
            intent.putExtra("circuitId", circuitId);
            startActivity(intent);
        });
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        Button viewFavoritesButton = findViewById(R.id.view_favorites_button);
        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(CircuitsActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ergast.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        CircuitDataBaseService service = retrofit.create(CircuitDataBaseService.class);
        service.listCircuits()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            datos.addAll(response.MRData.CircuitTable.Circuits);
                            adapter.notifyDataSetChanged();
                        },
                        error -> Log.e("API_ERROR", "Error en la API: " + error.getMessage())
                );
    }
}
