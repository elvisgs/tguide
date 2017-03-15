package br.com.tguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.PointOfInterest;

public class RatingActivity extends AppCompatActivity {

    public static final int RATING_RESULT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        final PointOfInterest poi = intent.getParcelableExtra("poi");
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Avalie o local \"" + poi.name + "\"");

        Button doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
                TextView comments = (TextView) findViewById(R.id.comments);

                Intent data = new Intent()
                        .putExtra("poi", poi)
                        .putExtra("rating", ratingBar.getRating())
                        .putExtra("comments", comments.getText());

                setResult(RATING_RESULT, data);
                finish();
            }
        });
    }
}
