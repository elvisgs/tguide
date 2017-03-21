package br.com.tguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.List;

import br.com.tguide.domain.PlaceRating;
import br.com.tguide.domain.PlaceRatingRepository;

public class DetailsActivity extends AppCompatActivity {

    public static final int GO_LOCATION_RESULT = 1;
    private PlaceRatingRepository repository = PlaceRatingRepository.getInstance();
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        latLng = getIntent().getParcelableExtra(RatingsFragment.LAT_LNG_KEY);
        List<PlaceRating> ratings = repository.findByLatLng(latLng);
        Collections.sort(ratings);

        ListView list = (ListView) findViewById(R.id.details);
        DetailsListAdapter adapter = new DetailsListAdapter(this, ratings);
        list.setAdapter(adapter);
    }

    public void goToLocation(View view) {
        Intent data = new Intent()
                .putExtra(RatingsFragment.LAT_LNG_KEY, latLng);

        setResult(GO_LOCATION_RESULT, data);
        finish();
    }
}
