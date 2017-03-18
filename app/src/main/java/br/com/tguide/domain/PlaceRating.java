package br.com.tguide.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;

import java.io.Serializable;
import java.util.Date;

public class PlaceRating implements Parcelable, Comparable<PlaceRating> {

    private LatLng latLng;
    private float value;
    private String placeName, comment;
    private Date collectedAt;

    public PlaceRating() {
    }

    public PlaceRating(Parcel in) {
        latLng = (LatLng) in.readValue(LatLng.class.getClassLoader());
        value = in.readFloat();
        placeName = in.readString();
        comment = in.readString();
        long tmpCollectedAt = in.readLong();
        collectedAt = tmpCollectedAt != -1 ? new Date(tmpCollectedAt) : null;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(latLng);
        dest.writeFloat(value);
        dest.writeString(placeName);
        dest.writeString(comment);
        dest.writeLong(collectedAt != null ? collectedAt.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PlaceRating> CREATOR = new Parcelable.Creator<PlaceRating>() {
        @Override
        public PlaceRating createFromParcel(Parcel in) {
            return new PlaceRating(in);
        }

        @Override
        public PlaceRating[] newArray(int size) {
            return new PlaceRating[size];
        }
    };

    public static PlaceRating fromPoI(PointOfInterest poi) {
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
