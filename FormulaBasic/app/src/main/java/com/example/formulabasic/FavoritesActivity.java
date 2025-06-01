package com.example.formulabasic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private FavoritesManager favoritesManager;
    private FavoriteAdapter adapter;
    private ArrayList<FavoriteItem> favoriteItems = new ArrayList<>();
    private SyncManager syncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesManager = new FavoritesManager(this);
        syncManager = new SyncManager(this);

        RecyclerView favoritesList = findViewById(R.id.lista_favorites);
        adapter = new FavoriteAdapter(favoriteItems, v -> {
            FavoriteItem item = (FavoriteItem) v.getTag();
            if ("driver".equals(item.itemType)) {
                Intent intent = new Intent(FavoritesActivity.this, DriverDetailActivity.class);
                intent.putExtra("driverId", item.itemId);
                startActivity(intent);
            } else if ("circuit".equals(item.itemType)) {
                Intent intent = new Intent(FavoritesActivity.this, CircuitDetailActivity.class);
                intent.putExtra("circuitId", item.itemId);
                startActivity(intent);
            }
        });
        favoritesList.setLayoutManager(new LinearLayoutManager(this));
        favoritesList.setAdapter(adapter);

        Button uploadButton = findViewById(R.id.upload_to_cloud_button);
        Button downloadButton = findViewById(R.id.download_from_cloud_button);

        uploadButton.setOnClickListener(v -> syncManager.uploadFavoritesToCloud());
        downloadButton.setOnClickListener(v -> {
            syncManager.downloadFavoritesFromCloud();
            loadFavorites();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        List<FavoriteItem> currentFavorites = favoritesManager.getAllFavorites();
        favoriteItems.clear();
        favoriteItems.addAll(currentFavorites);
        adapter.notifyDataSetChanged();
    }
}