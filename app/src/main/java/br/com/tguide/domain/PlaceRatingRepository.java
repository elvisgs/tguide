package br.com.tguide.domain;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.tguide.service.TGuideApi;
import br.com.tguide.service.TGuideApiServiceBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRatingRepository {

    private static PlaceRatingRepository instance;
    private final TGuideApi api;
    private List<PlaceRating> placeRatings = new ArrayList<>();
    private Map<LatLng, List<PlaceRating>> groupedByLatLng = new HashMap<>();

    private PlaceRatingRepository() {
        api = new TGuideApiServiceBuilder().build();
    }

    public static PlaceRatingRepository getInstance() {
        if (instance == null)
            instance = new PlaceRatingRepository();

        return instance;
    }

    public void save(PlaceRating placeRating) {
        saveRemotely(placeRating);

        placeRatings.add(placeRating);

        LatLng latLng = placeRating.getLatLng();
        if (!groupedByLatLng.containsKey(latLng)) {
            groupedByLatLng.put(latLng, new ArrayList<PlaceRating>());
        }

        groupedByLatLng.get(latLng).add(placeRating);
    }

    private void saveRemotely(PlaceRating placeRating) {
        api.save(placeRating).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("API", call.request().url().toString() + " called without errors");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API", "Call to " + call.request().url() + " failed", t);
            }
        });
    }

    public List<PlaceRatingAverage> getAveragesBetween(@NonNull LatLngBounds latLngBounds) {
        List<PlaceRatingAverage> items = new ArrayList<>();

        for (LatLng latLng : groupedByLatLng.keySet()) {
            if (!latLngBounds.contains(latLng)) continue;

            int count = 0;
            float sum = 0;

            for (PlaceRating rating : groupedByLatLng.get(latLng)) {
                count++;
                sum += rating.getValue();
            }

            float avg = sum / count;
            String placeName = groupedByLatLng.get(latLng).get(0).getPlaceName();
            items.add(new PlaceRatingAverage(latLng, placeName, avg));
        }

        Collections.sort(items);

        return items;
    }

    public List<PlaceRating> findByLatLng(LatLng latLng) {
        List<PlaceRating> items = new ArrayList<>();
        for (PlaceRating rating : placeRatings) {
            LatLng placeLatLng = rating.getLatLng();
            if (placeLatLng.latitude == latLng.latitude && placeLatLng.longitude == latLng.longitude)
                items.add(rating);
        }

        return items;
    }
}
