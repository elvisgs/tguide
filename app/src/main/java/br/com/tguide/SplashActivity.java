package br.com.tguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import br.com.tguide.domain.PlaceRating;
import br.com.tguide.domain.PlaceRatingRepository;
import br.com.tguide.service.OnDataLoaded;

public class SplashActivity extends AppCompatActivity {

    private PlaceRatingRepository repository = PlaceRatingRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        repository.loadCache(new OnDataLoaded<List<PlaceRating>>() {
            @Override
            public void dataLoaded(List<PlaceRating> data, Throwable t) {
                if (t == null) {
                    startActivity(new Intent(SplashActivity.this, TabActivity.class));
                    finish();
                } else {
                    String msg = "Não foi possível carregar os dados do servidor remoto. Por favor, verifique sua conexão com a Internet.";
                    Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
