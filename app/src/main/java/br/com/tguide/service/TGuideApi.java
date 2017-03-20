package br.com.tguide.service;

import br.com.tguide.domain.PlaceRating;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TGuideApi {

    @POST("/save")
    Call<Void> save(@Body PlaceRating placeRating);
}
