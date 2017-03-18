package br.com.tguide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.List;

import br.com.tguide.domain.PlaceRating;
import br.com.tguide.domain.PlaceRatingRepository;

public class DetailsActivity extends AppCompatActivity {

    private PlaceRatingRepository repository = PlaceRatingRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LatLng latLng = getIntent().getParcelableExtra(RatingsFragment.LAT_LNG_KEY);
        List<PlaceRating> ratings = repository.findByLatLng(latLng);
        Collections.sort(ratings);

        ListView list = (ListView) findViewById(R.id.details);
        DetailsListAdapter adapter = new DetailsListAdapter(this, ratings);
        list.setAdapter(adapter);
    }
}
