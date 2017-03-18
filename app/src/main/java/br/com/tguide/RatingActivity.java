package br.com.tguide;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.PointOfInterest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.tguide.domain.WeatherRepository;

public class RatingActivity extends AppCompatActivity {

    public static final int RATING_RESULT = 2;
    public static final String RATING_EXTRA_KEY = "rating";
    public static final String POI_EXTRA_KEY = "poi";
    public static final String COMMENT_EXTRA_KEY = "comment";

    private PointOfInterest poi;
    private WeatherRepository weatherRepository = new WeatherRepository();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        poi = getIntent().getParcelableExtra("poi");

        TextView placeName = (TextView) findViewById(R.id.placeName);
        placeName.setText(poi.name);

        TextView weather = (TextView) findViewById(R.id.weather);
        weather.setText(weatherRepository.getWeatherForPosition(null));

        TextView dateTime = (TextView) findViewById(R.id.dateTime);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        dateTime.setText(dateFormat.format(Calendar.getInstance().getTime()));
    }

    public void doneRating(View view) {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        TextView comments = (TextView) findViewById(R.id.comments);

        Intent data = new Intent()
                .putExtra(POI_EXTRA_KEY, poi)
                .putExtra(RATING_EXTRA_KEY, ratingBar.getRating())
                .putExtra(COMMENT_EXTRA_KEY, comments.getText());

        setResult(RATING_RESULT, data);
        finish();
    }
}
