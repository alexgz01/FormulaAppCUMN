package com.example.formulabasic;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncManager {
    private Context context;
    private FavoritesManager favoritesManager;
    private CloudFavoritesService cloudFavoritesService;

    public SyncManager(Context context) {
        this.context = context;
        this.favoritesManager = new FavoritesManager(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        cloudFavoritesService = retrofit.create(CloudFavoritesService.class);
    }

    public void uploadFavoritesToCloud() {
        List<FavoriteItem> localFavorites = favoritesManager.getAllFavorites();
        Completable.fromAction(() -> {
                    for (FavoriteItem item : localFavorites) {
                        cloudFavoritesService.addCloudFavorite(item)
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                        () -> Log.d("SYNC", "Uploaded: " + item.itemName),
                                        error -> Log.e("SYNC_ERROR", "Upload error for " + item.itemName + ": " + error.getMessage())
                                );
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Toast.makeText(context, "Sincronización a la nube iniciada", Toast.LENGTH_SHORT).show(),
                        error -> Toast.makeText(context, "Error en la sincronización a la nube: " + error.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    public void downloadFavoritesFromCloud() {
        cloudFavoritesService.getCloudFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cloudFavorites -> {
                            for (FavoriteItem cloudItem : cloudFavorites) {
                                favoritesManager.addFavorite(cloudItem.itemId, cloudItem.itemType, cloudItem.itemName);
                            }
                            Toast.makeText(context, "Sincronización desde la nube completada", Toast.LENGTH_SHORT).show();
                            // In a real app, you might want to refresh the UI after sync
                        },
                        error -> Toast.makeText(context, "Error al descargar favoritos de la nube: " + error.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
