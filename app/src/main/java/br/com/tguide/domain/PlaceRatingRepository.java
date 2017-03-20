package br.com.tguide.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.tguide.service.OnDataLoaded;
import br.com.tguide.service.SimpleCallback;
import br.com.tguide.service.TGuideApi;
import br.com.tguide.service.TGuideApiServiceBuilder;
import retrofit2.Call;
import retrofit2.Response;

public class PlaceRatingRepository {

    private static PlaceRatingRepository instance;
    private final TGuideApi api;
    private List<PlaceRating> ratingsCache = new ArrayList<>();
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

        ratingsCache.add(placeRating);

        addToGroup(placeRating);
    }

    private void addToGroup(PlaceRating placeRating) {
        LatLng latLng = placeRating.getLatLng();
        if (!groupedByLatLng.containsKey(latLng)) {
            groupedByLatLng.put(latLng, new ArrayList<PlaceRating>());
        }

        groupedByLatLng.get(latLng).add(placeRating);
    }

    private void groupAll() {
        for (PlaceRating rating : ratingsCache)
            addToGroup(rating);
    }

    private void saveRemotely(PlaceRating placeRating) {
        api.saveRating(placeRating).enqueue(new SimpleCallback<Void>());
    }

    public void loadCache(final OnDataLoaded<List<PlaceRating>> onDataLoaded) {
        if (!ratingsCache.isEmpty()) {
            if (onDataLoaded != null)
                onDataLoaded.dataLoaded(Collections.unmodifiableList(ratingsCache));

            return;
        }

        api.findAllRatings().enqueue(new SimpleCallback<List<PlaceRating>>() {
            @Override
            public void onResponse(Call<List<PlaceRating>> call, Response<List<PlaceRating>> response) {
                super.onResponse(call, response);
                ratingsCache = response.body();
                groupAll();

                if (onDataLoaded != null)
                    onDataLoaded.dataLoaded(Collections.unmodifiableList(ratingsCache));
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
        for (PlaceRating rating : ratingsCache) {
            LatLng placeLatLng = rating.getLatLng();
            if (placeLatLng.latitude == latLng.latitude && placeLatLng.longitude == latLng.longitude)
                items.add(rating);
        }

        return items;
    }
}
