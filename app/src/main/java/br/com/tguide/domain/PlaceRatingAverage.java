package br.com.tguide.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class PlaceRatingAverage implements Comparable<PlaceRatingAverage> {

    private LatLng latLng;
    private String placeName;
    private float value = 0f;

    public PlaceRatingAverage(@NonNull LatLng latLng, @NonNull String placeName, float value) {
        this.latLng = latLng;
        this.placeName = placeName;
        this.value = value;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getPlaceName() {
        return placeName;
    }

    public float getValue() {
        return value;
    }

    @Override
    public int compareTo(@NonNull PlaceRatingAverage o) {
        return Float.compare(o.value, value);
    }

    @Override
    public String toString() {
        return placeName + " -> " + value;
    }
}
