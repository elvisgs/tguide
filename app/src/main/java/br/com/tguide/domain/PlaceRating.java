package br.com.tguide.domain;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Date;

public class PlaceRating implements Comparable<PlaceRating> {

    @JsonUnwrapped
    private LatLng latLng;
    private float value;
    private String placeName, comment;
    private Date collectedAt;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(Date collectedAt) {
        this.collectedAt = collectedAt;
    }

    public static PlaceRating fromPoI(@NonNull PointOfInterest poi) {
        PlaceRating placeRating = new PlaceRating();
        placeRating.setLatLng(poi.latLng);
        placeRating.setPlaceName(poi.name);
        return placeRating;
    }

    @Override
    public int compareTo(@NonNull PlaceRating o) {
        return o.collectedAt.compareTo(collectedAt);
    }
}
