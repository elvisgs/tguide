package br.com.tguide.service;

import java.util.List;

import br.com.tguide.domain.PlaceRating;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TGuideApi {

    String RATINGS_PATH = "/ratings";

    @POST(RATINGS_PATH)
    Call<Void> saveRating(@Body PlaceRating placeRating);

    @GET(RATINGS_PATH)
    Call<List<PlaceRating>> findAllRatings();
}
