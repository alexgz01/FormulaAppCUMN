package com.example.formulabasic;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CloudFavoritesService {
    @GET("favorites")
    Observable<List<FavoriteItem>> getCloudFavorites();

    @POST("favorites")
    Completable addCloudFavorite(@Body FavoriteItem favoriteItem);

    @DELETE("favorites/{itemId}")
    Completable deleteCloudFavorite(@Path("itemId") String itemId);
}
