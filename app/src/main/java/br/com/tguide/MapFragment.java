package br.com.tguide;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnPoiClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.Calendar;

import br.com.tguide.domain.PlaceRating;
import br.com.tguide.domain.PlaceRatingRepository;

public class MapFragment extends Fragment
        implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
            OnPoiClickListener {

    public static final int RATING_REQUEST = 1;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private PlaceRatingRepository repository = PlaceRatingRepository.getInstance();
    private Location myLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_styles));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnPoiClickListener(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();
        }

        String tip = getResources().getString(R.string.tip_map_action);
        Snackbar.make(getView(), tip, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng latLngMyLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .zoom(18)
                .target(latLngMyLocation)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        map.addMarker(new MarkerOptions()
                .position(latLngMyLocation)
                .title("Sua posição atual")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        );
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        showRatingActivity(poi);
    }

    private void showRatingActivity(PointOfInterest poi) {
        Intent intent = new Intent(this.getActivity().getApplicationContext(), RatingActivity.class);
        intent.putExtra("poi", poi);
        intent.putExtra("myLocation", myLocation);
        startActivityForResult(intent, RATING_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RatingActivity.RATING_RESULT) {
            PointOfInterest poi = data.getParcelableExtra(RatingActivity.POI_EXTRA_KEY);
            float ratingValue = data.getFloatExtra(RatingActivity.RATING_EXTRA_KEY, 0f);
            String comment = data.getCharSequenceExtra(RatingActivity.COMMENT_EXTRA_KEY).toString();

            PlaceRating placeRating = PlaceRating.fromPoI(poi);
            placeRating.setValue(ratingValue);
            placeRating.setComment(comment);
            placeRating.setCollectedAt(Calendar.getInstance().getTime());

            repository.save(placeRating);

            String message = getResources().getString(R.string.success_place_rating,
                    placeRating.getPlaceName(), placeRating.getValue());
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    public VisibleRegion getVisibleRegion() {
        return map.getProjection().getVisibleRegion();
    }

    public Location getMyLocation() {
        return myLocation;
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
